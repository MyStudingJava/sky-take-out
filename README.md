# sky-take-out

# day-01-08-开发环境搭建_后端环境搭建_前后端联调
## 报错
### 1. 编译项目报错
```shell
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.10.1:compile (default-compile) on project sky-common: Fatal error compiling: java.lang.NoSuchFieldError: Class com.sun.tools.javac.tree.JCTree$JCImport does not have member field 'com.sun.tools.javac.tree.JCTree qualid' -> [Help 1]
```
#### 错误原因:JDK和Lombok版本不对应
#### 解决方式
保持jdk21,修改lombok版本是1.18.30
> 在Maven项目的pom.xml文件中，配置Lombok插件。确保插件的版本与你的Lombok版本和JDK版本兼容。以下是一个示例配置：
```diff
...
<properties>
    <mybatis.spring>2.2.0</mybatis.spring>
-    <lombok>1.18.20</lombok>
+    <lombok>1.18.30</lombok>
...
```

### 2.登录接口没反应
```shell
ERROR 5440 --- [eate-1513124396] com.alibaba.druid.pool.DruidDataSource   : create connection SQLException, url: jdbc:mysql://localhost:3306/sky_take_out?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true, errorCode 1045, state 28000
```
#### 错误原因:数据库连接错误,这里是密码错误
> 在/sky-server/src/main/resources/application-dev.yml文件中,修改数据库密码
> **一定要加"""**
```diff
sky:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: "xxxx"
```
