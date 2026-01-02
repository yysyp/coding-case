## ydlj-gyjh, Do the right thing, YiDuiLaJi Not to waste Time. Focus TODO ##
#### Jasypt settting
- Create file src/main/resources/ignore/setting.conf and put the jasypt password into it. i.e. JASYPT_ENCRYPTOR_PASS=xxx
- Create file src/test/resources/application-jasypt.yml and put the jasypt password into it. i.e. JASYPT_ENCRYPTOR_PASS: xxx
#### SpringBoot JPA Demo
- deploy folder: only the script for deploy this Demo
- doc folder: the documents related to this Demo
- src/main/resources/script folder: other scripts
- src/main/resources/ignore folder: git ignored folder, it has files: setting.conf etc.

### There is a simple CRUD example using JPA: springboot-jpa-demo
- Solution 1:
- All Response should extends BaseSuccessResp.java
- All Error follows throw:
  [XxxException] --extends--> [BaseErrorException] --has--> [CodeEnum] --has--> [1, httpCode; 2, code; 3, msg]
- CustomGlobalExceptionHandler to convert BaseErrorException to BaseErrorResp. 

- Solution 2:
- All Response should extends BaseSuccessResp.java
- All Error follows throw:
- Your customized Exception class i.e. [CustErrorException] with annotation ExceptionMapping to specify the error code, msg, httpStatus
---
#### Run with docker:
- Make sure you mvn rebuild the latest jar!!!
- Please set NEXUS_USER & NEXUS_PASS if you'd like to connect to NEXUS.
- Go to folder: deployment-scripts/docker/ and open "Git bash here".
- ./runWithDocker.sh to deploy jar to docker.
- Input Nexus password if any
- Command to stop instance: docker stop xxxxx
---
- Visit Application via:
URLs:
- http://localhost:10001/api-docs
- http://localhost:10001/swagger-ui/index.html
- http://localhost:10001/api/books/
- http://localhost:10001/actuator
- http://localhost:10001/upload2.html
H2 DataBase:
- http://localhost:10001/h2-console
<pre>
    Driver Class: org.h2.Driver
    JDBC URL:     jdbc:h2:mem:springboot-jpa-demo;MODE=MYSQL;DB_CLOSE_DELAY=-1
    User Name:    sa
</pre>

Shell curl scripts:
```shell
#! /bin/bash
set -eu

curl -ikLv --request GET \
  --url http://localhost:10001/api-docs \
  --header 'User-Agent: insomnia/10.1.0'

curl -ikLv --request GET \
  --url http://localhost:10001/api/books/ \
  --header 'User-Agent: insomnia/10.1.0'

curl -ikLv --request GET \
  --url http://localhost:10001/actuator \
  --header 'User-Agent: insomnia/10.1.0'

curl -ikLv -X 'POST' \
  'http://localhost:10001/api/books/' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Books20241017",
  "author": "ps",
  "price": 170,
  "publishDate": "2024-10-17"
}'


echo ' '
read -p "Press any key..."
    
```

---
#### Run with kubernetes:
- Make sure you mvn rebuild the latest jar!!!
- Please set NEXUS_USER & NEXUS_PASS if you'd like to connect to NEXUS.
- docker push xxx.com/path/repo/springboot-jpa-demo
- If no NEXUS repo, comment out "docker push xxx.com/path/repo/springboot-jpa-demo" in dockerBuildPushAndGenDeployYaml.sh
- If no NEXUS repo, fix the: "xxx.com/path/repo/springboot-jpa-demo:THE_APP_VERSION" in deploy-template.yaml
- ./buildYamlAndDeploy2k8sCluster.sh to deploy jar to kubernetes.
- Command to view deployment in k8s: kubectl -n nsapp get all
- Command to remove the deployment: kubectl delete namespace nsapp
- OR: kubectl delete -f deploy.yaml
- Check event: kubectl -n nsapp get event
- Check log: kubectl -n nsapp logs -f ngx-5d7556ffd-d7jwt -c 1st
- OR: kubectl -n nsapp logs -f $(kubectl -n nsapp get pods --field-selector status.phase=Running --no-headers -o custom-columns=":metadata.name" | grep springboot-jpa-demo | head -1)
- OR: kubectl -n nsapp -it exec $(kubectl -n nsapp get pods --field-selector status.phase=Running --no-headers -o custom-columns=":metadata.name" | grep springboot-jpa-demo | head -1) -- bash -c "ls"
- Check container: kubectl -n nsapp -it exec podxxx -- bash 
- OR: kubectl -n nsapp -it exec ngx-65f68876c8-ksc74  -c 1st -- /bin/sh
--- 
- Visit Application via:
  URLs:
- http://localhost:30001/api-docs
- http://localhost:30001/swagger-ui/index.html
- http://localhost:30001/api/books/
- http://localhost:30001/actuator
-
H2 DataBase:
- http://localhost:30001/h2-console
<pre>
    Driver Class: org.h2.Driver
    JDBC URL:     jdbc:h2:mem:springboot-jpa-demo;MODE=MYSQL
    User Name:    sa
</pre>

---
####
SpringSecurity based on JWT access control.
1. Generate JWT token: curl -X POST -H "Content-Type: application/json" -d '{"username": "admin", "password": "{{YOUR_PASSWORD}}"}' http://localhost:8080/api/auth/login
2. Use the generated token to access the protected resources: curl -H "Authorization: Bearer {{YOUR_TOKEN}}" http://localhost:8080/api/books
   OR
3. Swagger UI: http://localhost:8080/swagger-ui/index.html to Authorize with JWT token.

---
#### Jasypt usage:
1, add pom dependency
2, add @EnableEncryptableProperties
3, update the password in application.properties

   1. mvn jasypt:encrypt -Djasypt.encryptor.password=${PASS} -Djasypt.encryptor.algorithm=PBEWithMD5AndDES
Or use JasyptTest.java to generate ENC(string)
   2. spring.datasource.password=ENC(your_encrypted_password)
   3. export JASYPT_ENCRYPTOR_PASSWORD=your_encryption_password

---
#### Run cucumber automation test:
mvn clean test "-Dspring.profiles.active=dev" "-Dtest=ps.demo.jpademo.cucumber.CucumberTestRunner" "-Djasypt.encryptor.password="
windows: mvn clean test "-Dtest=ps.demo.jpademo.cucumber.CucumberTestRunner"
Linux:   mvn clean test -Dtest=ps.demo.jpademo.cucumber.CucumberTestRunner -Dcucumber.options="--tags @test"

---
#### Cache usage:
// 存储用户令牌
cache.put("user:token:123", userToken, 30, TimeUnit.MINUTES, 1024);

// 存储配置信息
cache.put("config:system:timeout", 5000, 24, TimeUnit.HOURS, 8);

// 存储任意对象
MyComplexObject obj = new MyComplexObject();
cache.put("data:complex:1", obj, 1, TimeUnit.HOURS, calculateSize(obj));

// 安全获取（不会抛出异常）
Optional<MyComplexObject> result = cache.get("data:complex:1");
if (result.isPresent()) {
// 处理结果
}

// 监控缓存状态
CacheStats stats = cache.getStats();
if (stats.getUsageRate() > 0.8) {
log.warn("Cache usage is high: {}%", stats.getUsageRate() * 100);
}


---
#### Uri Pattern Matcher:
// 基本使用
boolean matches = UriPatternMatcher.match("api/users/*", "api/users/123");

// 创建匹配器实例
UriPatternMatcher matcher = UriPatternMatcher.getInstance();
matcher.addPattern("api/**");
matcher.addPattern("static/**");
matcher.addPattern("*.html");

// 批量添加
List<String> patterns = Arrays.asList(
"api/users/*",
"api/posts/**",
"static/**/*.css",
"static/**/*.js"
);
matcher.addPatterns(patterns);

// 匹配检查
if (matcher.matches("api/users/123")) {
System.out.println("URI匹配成功");
}

// 获取所有匹配的模式
List<String> matchingPatterns = matcher.findAllMatchingPatterns("static/css/main.css");

// 预编译模式（高性能场景）
UriPatternMatcher precompiled = UriPatternMatcher.precompile(patterns);
boolean result = precompiled.matches("api/posts/123/comments");


---
#### Gitbash k8s commands:
refer to: CmdRunK8sCRUD.java


---
#### WebDriverTool debugInteract:
refer to: WebDriverTool.debugInteract(driver);
"Input[open:url || [click|text]:[id|name|xpath|plink]]:xxx to continue: "
i.g.
open: https://baidu.com
by:id:xx
by:class:xx
by:name:xx
by:xpath:xx
by:plink:xx
click:id:xx
text:id:xx
javascript:xx
exit
q

