

---
标准形式转到环境变量三步骤：
1，使用下划线替换点号"."
2，去掉短横线"-"
3，转换为大写

例如：
demo.test.ab-cd-ef 转换为：DEMO_TEST_ABCDEF
---
环境变量的表示形式：
demo.test[0].ab-cd-ef 映射为：DEMO_TEST_0_ABCDEF
demo.test[1].ab-cd-ef 映射为：DEMO_TEST_1_ABCDEF

---
Kebab-case: my-app.database.url in application.properties can be bound by an environment variable named MY_APP_DATABASE_URL.
Camel-case: myApp.database.url in @ConfigurationProperties can also be bound by MY_APP_DATABASE_URL.
Underscore notation: my_app.database.url can be bound by MY_APP_DATABASE_URL.



