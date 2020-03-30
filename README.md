## TW Wallet backend
### This project provide backend service for TW Wallet.

#### RunTime
* Java 1.8
* PostgreSQL
* RabbitMQ
* Gradle

#### Prerequisites
* Install `PostgreSQL` at your `local` environment
```
cd docker
docker-compose up -d  
./gradlew -Dflyway.configFiles=flyway.conf flywayMigrate
```

* Install `RabbitMQ` at your `local` environment
```
docker pull rabbitmq:management
docker run -d --name rabbitmq --publish 5671:5671  --publish 5672:5672 --publish 4369:4369 --publish 25672:25672 --publish 15671:15671 --publish 15672:15672 rabbitmq:management
```
> 可在 `http://localhost:15672/` 页面,通过 `guest` 和 `guest`(用户名/密码) 登陆 rabbitMQ 的终端页面.
> RabbitMQ 的配置文件位于: `java/com/thoughtworks/wallet/config/SenderConf.java` 

#### QuickStart
* `./gradlew clean` : clean `build` folder
* `./gradlew build` : build application
* `./gradlew bootRun` : run application
* `./gradlew -Dflyway.configFiles=flyway.conf flywayMigrate`: create table
* `./gradlew -Dflyway.configFiles=flyway.conf flywayClean`: delete table
* `./gradlew generateWalletJooqSchemaSource`:  generate jooq 

#### Conduct of commit
> format: type: content
> eg: feat: add getUserInfo api
> type can be one of the following item:
* feat: new feature 
* fix: fix the bug 
* doc: about documentation 
* style: just change the style 
* refactor: refactor the code with no behavior changed
* chore: changes about utilization and tools, no business logic
* revert: revert the previous commit
