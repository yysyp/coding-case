## ydlj-gyjh ##
## Jasypt settting
- Create file src/main/resources/ignore/setting.conf and put the jasypt password into it. i.e. JASYPT_ENCRYPTOR_PASS=xxx
- Create file src/test/resources/application-jasypt.yml and put the jasypt password into it. i.e. JASYPT_ENCRYPTOR_PASS: xxx

## aquick-poc-mpt
- deploy folder: only the script for deploy this Demo
- doc folder: the documents related to this Demo
- src/main/resources/script folder: other scripts
- src/main/resources/ignore folder: git ignored folder, it has files: setting.conf etc.

## Access Links

- **Swagger UI**: [http://localhost:20001/swagger-ui.html](http://localhost:20001/swagger-ui.html)
- **H2 Console**: [http://localhost:20001/h2-console](http://localhost:20001/h2-console)
    - JDBC URL: `jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1`
    - Username: `sa`
    - Password: 

## setting.conf
./src/main/resources/ignore/setting.conf
```text
username=default
[dev-group]
username=user1

[sit-group]
username=usersit

```
