# 15. 基础设施，合约部署和事件同步

Date: 2022-03-31

## Status

2022-03-31 proposed

## Context
因为 aws 中国当前的 CA 将于 2022 年 6 月 1 日到期。在此日期之前，我们需要首先将新的CA证书添加到客户端应用程序中的受信任的存储中，然后将数据库实例上的证书更新为最新颁发的 rds-ca-rsa2048-g1 版本。

不过此前，我们在连接数据库过程中没有启用ssl。考虑到安全影响，需要在升级数据库实例证书版本的前提下，启用数据库连接的ssl功能。

由于我们使用的数据库是 postgres，它的 [jdbc driver](https://jdbc.postgresql.org/documentation/head/connect.html#connection-parameters)  文档上阐明了连接字符串可以拼接的参数，如`ssl,sslmode,sslrootcert,sslfactory,sslfactoryarg`等。大致支持启用 ssl 有三种模式，列出如下：

**注:** aws RDS 的根证书可以从官网下载，从这个[页面](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.SSL.html)下载[全球根证书](https://truststore.pki.us-gov-west-1.rds.amazonaws.com/global/global-bundle.pem)或者在这个[页面](https://docs.amazonaws.cn/en_us/AmazonRDS/latest/UserGuide/UsingWithRDS.SSL.html) 下载[中国地区根证书](https://rds-truststore.s3.cn-north-1.amazonaws.com.cn/cn-north-1/cn-north-1-bundle.pem)。

### 1. sslrootcert
```
jdbc:postgresql://{hostname}:{port}/{db}?sslmode=verify-full&sslrootcert=cnroot.pem
```
第一种是使用参数`sslrootcert`，这种设置方式常见用于psql的连接串上。如：
```
psql --host={host} --port={port} --ssl=true "user={user} dbname={db} sslrootcert=cnroot.pem sslmode=verify-full"
```
AWS文档中*Using SSL with a PostgreSQL DB instance* [章节](https://docs.amazonaws.cn/en_us/AmazonRDS/latest/UserGuide/PostgreSQL.Concepts.General.SSL.html)有详细描述。不过这种用法在 Java 应用程序中因为pem文件的路径（不论是相对还是绝对路径）难以设置，所以该方案不易实施。

### 2. DefaultJavaSSLFactory with trustStore
```
jdbc:postgresql://{hostname}:{port}/{db}?sslmode=verify-full&sslfactory=org.postgresql.ssl.DefaultJavaSSLFactory
```
第二种是使用 java 的 keystore 功能。，不过在尝试导入之前，我们需要对根证书pem文件进行分割整理。这是什么意思呢？我们打开 cn-north-1-bundle.pem 之后就可以发现这一捆证书，所以需要从 bundle 中分割出来独立的证书，具体参照[#Sample script for inporting certificates on macOS](https://docs.amazonaws.cn/en_us/AmazonRDS/latest/UserGuide/UsingWithRDS.SSL-certificate-rotation.html)。我这里也把代码贴出来了。
```

mydir=tmp/certs
if [ ! -e "${mydir}" ]
then
mkdir -p "${mydir}"
fi

truststore=${mydir}/rds-truststore.jks
storepassword=changeit

# global-bundle.pem 可以替换成 cn-northwest-1-bundle.pem 
curl -sS "https://rds-truststore.s3.cn-north-1.amazonaws.com.cn/global/global-bundle.pem" > ${mydir}/global-bundle.pem
split -p "-----BEGIN CERTIFICATE-----" ${mydir}/global-bundle.pem rds-ca-

# split 命令会分割然后生成rds-ca-a[a-z]的文件
for CERT in rds-ca-*; do
  alias=$(openssl x509 -noout -text -in $CERT | perl -ne 'next unless /Subject:/; s/.*(CN=|CN = )//; print')
  echo "Importing $alias"
  keytool -import -file ${CERT} -alias "${alias}" -storepass ${storepassword} -keystore ${truststore} -noprompt
  rm $CERT
done

rm ${mydir}/global-bundle.pem

echo "Trust store content is: "

keytool -list -v -keystore "$truststore" -storepass ${storepassword} | grep Alias | cut -d " " -f3- | while read alias 
do
   expiry=`keytool -list -v -keystore "$truststore" -storepass ${storepassword} -alias "${alias}" | grep Valid | perl -ne 'if(/until: (.*?)\n/) { print "$1\n"; }'`
   echo " Certificate ${alias} expires in '$expiry'" 
done
```
经过上面的分割、导入操作之后，我们就可以在应用程序启动的时候指定 trustStore 参数。
```
java -Djavax.net.ssl.trustStore=tmp/certs/rds-truststore.jks -Djavax.net.ssl.trustStorePassword=changeit -jar {your}.jar
```
但是指定路径参数一样会遇到第一种解决方案同样的问题。所以在 Java 世界里，可以用导入 JDK 的 cacerts 的方式解决依赖文件路径的问题。
```bash
# 尝试用 openssl 找到我们需要的根证书，即 RSA2048G1
# 我们假设找到的证书是 cnroot.pem
openssl x509 -noout -text -in cnroot.pem | perl -ne 'next unless /Subject:/; s/.*(CN=|CN = )//; print'
-> Amazon RDS cn-northwest-1 Root CA RSA2048 G1, L=Seattle

# 使用 Java 的 keytool 将证书导入 jdk 的 cacerts 文件
sudo keytool -import -alias rds-root -keystore /{path}/lib/security/cacerts -file cnroot.pem

# 查看导入的证书
keytool -list -v -keystore cacerts
```
导入成功后，启动应用就不再需要传入 trustStore 等参数。不过，这同样带来一个问题——如何在部署环境中导入证书？当然，通过构建 docker image 的同事就导入证书自然也是可行的。那么还有没有其他方法？


### 3. SingleCertValidatingFactory with rootcert pemfile
```
jdbc:postgresql://{hostname}:{port}/{db}?sslmode=verify-full&sslfactory=org.postgresql.ssl.SingleCertValidatingFactory&sslfactoryarg=classpath:cnroot.pem
```
第三种方案要简单很多。通过`SingleCertValidatingFactory` 和 `sslfactoryarg`参数，我们就可以充分利用 Java 独有的 classpath 机制来加载证书文件，只要把 cnroot.pem 文件放到项目的 `src/main/resources` 目录下即可。

## Decision
通过上述比较，我们采用第三种方案。

## Consequences
待续。
