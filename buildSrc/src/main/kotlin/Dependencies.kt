object Versions {
    const val boot = "2.2.6.RELEASE"
    const val swagger2 = "2.9.2"
    const val slf4j = "1.7.30"
    const val logback = "1.2.3"
    const val postgresql = "42.2.11"
    const val h2 = "1.4.200"
    const val jooq = "3.13.1"
    const val modelmapper = "2.3.7"
    const val modelmapperJooq = "2.3.0"
    const val jackson = "2.11.0.rc1"
    const val w3j = "4.5.16"
    const val flyway = "6.4.1"
    const val commonsLang3 = "3.10"
    const val commonsCollections4 = "4.4"
    const val lombok = "1.18.12"
    const val guava = "29.0-jre"
    const val okhttp = "4.6.0"
    const val failsafe = "2.3.5"       // retry and circuit breakers
    const val snakeyaml = "1.26"
    const val bouncycastle = "1.65"
    const val jwt="3.10.3"
    const val apacheCommonsText="1.9"
    const val xxlJob="2.3.0"
    const val aspectj="1.9.6"

}

object Deps {

    const val aspectj ="org.aspectj:aspectjrt:${Versions.aspectj}"
    const val xxlJob ="com.xuxueli:xxl-job-core:${Versions.xxlJob}"
    const val jwt ="com.auth0:java-jwt:${Versions.jwt}"
    const val bouncycastle = "org.bouncycastle:bcprov-jdk15on:${Versions.bouncycastle}"
    const val springBootStarterAmqp = "org.springframework.boot:spring-boot-starter-amqp:${Versions.boot}"
    const val springBootStarterDataJdbc = "org.springframework.boot:spring-boot-starter-data-jdbc:${Versions.boot}"
    const val springBootStarterWeb = "org.springframework.boot:spring-boot-starter-web:${Versions.boot}"
    const val springBootConfigurationProcessor = "org.springframework.boot:spring-boot-configuration-processor:${Versions.boot}"
    const val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test:${Versions.boot}"
    const val springfoxSwagger2 = "io.springfox:springfox-swagger2:${Versions.swagger2}"
    const val springfoxSwaggerUI = "io.springfox:springfox-swagger-ui:${Versions.swagger2}"
    const val slf4j = "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val logbackClassic = "ch.qos.logback:logback-classic:${Versions.logback}"
    const val logbackCore = "ch.qos.logback:logback-core:${Versions.logback}"
    const val jooq = "org.jooq:jooq:${Versions.jooq}"
    const val jooqCodegen = "org.jooq:jooq-codegen:${Versions.jooq}"
    const val postgresql = "org.postgresql:postgresql:${Versions.postgresql}"
    const val h2 = "com.h2database:h2:${Versions.h2}"
    const val modelmapper = "org.modelmapper:modelmapper:${Versions.modelmapper}"
    const val modelmapperJooq = "org.modelmapper.extensions:modelmapper-jooq:${Versions.modelmapperJooq}"
    const val jacksonCore = "com.fasterxml.jackson.core:jackson-core:${Versions.jackson}"
    const val jacksonDatabind = "com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}"
    const val jacksonAnnotations = "com.fasterxml.jackson.core:jackson-annotations:${Versions.jackson}"
    const val jacksonDatatypeJdk8 = "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Versions.jackson}"
    const val jacksonDatatypeJsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}"
    const val snakeyaml = "org.yaml:snakeyaml:${Versions.snakeyaml}"
    const val w3j = "org.web3j:core:${Versions.w3j}"
    const val w3jContract = "org.web3j:contracts:${Versions.w3j}"
    const val flyway = "org.flywaydb:flyway-core:${Versions.flyway}"
    const val commonsLang3 = "org.apache.commons:commons-lang3:${Versions.commonsLang3}"
    const val commonsCollections4 = "org.apache.commons:commons-collections4:${Versions.commonsCollections4}"
    const val lombok = "org.projectlombok:lombok:${Versions.lombok}"
    const val guava = "com.google.guava:guava:${Versions.guava}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttpUrlConnection = "com.squareup.okhttp3:okhttp-urlconnection:${Versions.okhttp}"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val failsafe = "net.jodah:failsafe:${Versions.failsafe}"
    const val apacheCommonsText = "org.apache.commons:commons-text:${Versions.apacheCommonsText}"
}
