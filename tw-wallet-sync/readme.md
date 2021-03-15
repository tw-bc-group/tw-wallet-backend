
# xxl-job配置


## mysql
	1. docker pull mysql:latest
	2. docker run -itd --name xxl-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql
	3.  docker cp ./doc/db/tables_xxl_job.sql xxl-mysql:/tmp
	4. docker exec -it xxl-mysql /bin/bash
	5. mysql -uroot -p123456
	6. mysql> source /tmp/tables_xxl_job.sql;
	7. mysql> show tables;

## xxl-job-admin
	1. docker pull xuxueli/xxl-job-admin:2.3.0
	2. docker ps -l
	CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
	82bdd506aa64        cfe34e8d6785        "sh -c 'java -jar ..."   47 seconds ago      Up 47 seconds       0.0.0.0:8080->8080/tcp   xxl-job-admin
	3. [root@localhost db]# docker stop 82bdd506aa64
	82bdd506aa64
	4. [root@localhost db]# docker rm 82bdd506aa64
	82bdd506aa64
	5. Run admin
	docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://192.168.0.100:3306/xxl_job?Unicode=true&characterEncoding=UTF-8 --spring.datasource.username=root  --spring.datasource.password=123456" -p 8080:8080 -v /tmp:/data/applogs --name xxl-job-admin  --privileged=true -d xuxueli/xxl-job-admin:2.3.0
	6. 查看状态
	docker logs -t -f --tail 200f xxl-job-admin

## JavaBean
	1. Mvn build 整个工程
	2. 进入到 /xxl-job/xxl-job-executor-samples/xxl-job-executor-sample-springboot，看到一个docker file，确认target中jar包已经生成
	3. build镜像
	docker build . -t tw-wallet-sync-0.0.1:v1.3 
	4. 启动刚才的镜像，可以启动多个
	docker run --env-file=".env.local" --name="tw-wallet-sync" --privileged=true -d -p 8085:8089 -p 9995:9999 tw-wallet-sync-0.0.1:v1.3
	docker run --privileged=true -d -p 8082:8080 -p 9998:9999 xxl-job-executor-sample-springboot-2.3.1:v1.2 
    5. docker logs tw-wallet-sync --tail 100 -f 来查看日志
    
## admin 中注册执行器
    1. 注意在网页中配置正确的ip地址，如果都是用docker来启动的，则需要用 docker inspect 查看正确的 ip 地址
    
    
 ## 重试机制
 1. 请看 retry package。
 2. 没有测试，只是搭建了框架。