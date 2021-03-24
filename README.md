### 极客时间课后作业

#### 第四周

##### 作业运行说明

```shell
mvn clean package -U
java -jar .\user-web\target\user-web-v1-SNAPSHOT-war-exec.jar
```

运行成功后，可访问 http://localhost:8080/config 获取 ServletContext 的配置参数

访问 http://localhost:8080/rest-config 可通过封装的ThreadLocal中的request获取配置参数，可扩展为支持存储多种属性的 RequestAttributes，待进一步扩展。

my-dependency-injection/my-configuration模块均已独立

可脱离 web.xml 配置实现ComponentContext 自动初始化



往期内容:

 http://localhost:8080/register-form 注册用户

 http://localhost:8080/login-form 进行登录

