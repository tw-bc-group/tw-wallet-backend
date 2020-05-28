# 7. checkin generate code

Date: 2020-05-27

## Status

2020-05-27 complete

## Context

1. We use jooq to manipulate pg.
2. JooqCodeGen plugin generate pojo, records, ADOs for us.
3. JooqCodeGen depends on Flyway Migrate.
4. We should config carefully to make CI work properly.

## Decision

1. run migrate in CI

```groovy
  //Jenkinsfile.groovy
  stage('Migration') {
      steps {
        sh './gradlew --no-daemon -Penv=dev flywayMigrate'
      }
    }
```

2. do not generate code in CI when build
```groovy
generateSchemaSourceOnCompilation = false
```

3. check in code to git
```groovy
jooq {
    version = '3.13.1'
    edition = 'OSS'
    generateSchemaSourceOnCompilation = false
    wallet(sourceSets.main) {
            target {
                packageName = 'com.thoughtworks.wallet.gen'
                directory = "src/generated/java"
            }
        }
    }
}
```

## Consequences

1. check in generate code.
2. do not generate code when build.
3. generate code by manual operation.
