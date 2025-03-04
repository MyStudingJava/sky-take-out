# sky-take-out

# 报错
## day-01-08-开发环境搭建_后端环境搭建_前后端联调
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

## day-02-07-员工分页查询_代码开发
### 1. mapper文件没有报错---需要安装mybatisx插件
![mapper文件没有报错](https://i-blog.csdnimg.cn/blog_migrate/f78f777f9f014e6460d5791afe018d2f.png)
![mapper文件没有报错](https://i-blog.csdnimg.cn/blog_migrate/cfcce1f1f6a5e89b295b9cfd76836642.png)
![mapper文件没有报错](https://i-blog.csdnimg.cn/blog_migrate/372830982faadf7d77b34bea4300ce6b.png)

# 新东西
### 1.idea 进入下一层和进入底层快捷键
> ctrl+鼠标左键：进入接口，它是进入下一层
> ctrl+alt+鼠标左键：进入实现方法，它是进入最底层

### 2. 认识项目
![认识项目](https://i-blog.csdnimg.cn/direct/7d801129f08a451ca1047d72fd998f18.png)

### 3.什么是持久层?
> 持久层Mapper是介于业务层和数据访问层之间的一种层，它对数据访问层进行了封装，实现了对数据访问的抽象，从而实现了对业务逻辑的隔离。
> 不是service,service是业务逻辑层

### 4.JWT的流程
![JWT的流程](https://i-blog.csdnimg.cn/direct/b8bf46b45a224d6c9b4fbc9f409624cf.png)

### 5.ThreadLocal
##### 解析出登陆员工id后，如何传递给Service的save方法？
![ThreadLocal](https://i-blog.csdnimg.cn/direct/6a5f046f513640918497de7a0cefdd18.png)

##### 使用条件
> 客户端每一次发出的请求，tomcat都会为其分配一个单独的线程，在这个线程会执行不同的代码功能，比如拦截器，controller，service等等，它们都属于同一个线程，满足这个条件，就可以使用ThreadLocal，然后将数据存进去，在对应的地方取出来，所以通过ThreadLocal这个存储空间，即可达到相应的效果

##### 验证：每一次请求都会对应一个单独的线程
> 点击保存执行一次
![线程](https://i-blog.csdnimg.cn/direct/f95566a67d4643d78223545a2b8727b9.png)
![线程](https://i-blog.csdnimg.cn/direct/eccc2395dd4547ea8fa47549a647e1ed.png)


##### 再次点击保存，重新发起请求即重新获取一个单独的线程
![线程](https://i-blog.csdnimg.cn/direct/4a07fb9aeaf444a194120e6095ffe1ce.png)

#### ThreadLocal常用方法
![ThreadLocal常用方法](https://i-blog.csdnimg.cn/direct/d0f2f0f0c0e34f0c8e0a0f0b0c0f0f0f.png)

##### 注意：
> 在我们使用ThreadLocal时，往往对其进行封装成一个工具类，便于使用
![ThreadLocal常用方法](https://i-blog.csdnimg.cn/direct/062da7b7058f4850946bed3b7c03f393.png)

##### 实现思路
> 在已经解析出用户id的地方，进行调用ThreadLocal.set方法，将id存进去
![ThreadLocal常用方法](https://i-blog.csdnimg.cn/direct/10743e9440ec47d095a6d736e75ba0e8.png)
> 然后在service中进行取出
![ThreadLocal常用方法](https://i-blog.csdnimg.cn/direct/9b0525fd220547919228bf6bcc43a14a.png)

> 扩展：
> 在解析出登录员工id后如何传递给Service的save方法？
> 答：通过ThreadLocal，它是Thread的局部变量，为每个线程提供单独一份的存储空间，具有线程隔离的效果，只有在线程内才能获取到对应的值，在线程外则不能访问。 
> 可以通过在controller、service和拦截器中输出线程的id来看是否单次请求是同一个线程，经实验验证是同一个线程。
> 
> 小技巧：选中要计算的表达式，然后右键，选择Evaluate Expression，然后点击Evaluate即可。
> ![小技巧](https://i-blog.csdnimg.cn/blog_migrate/3f47bc877db2f3f274a2ff7ab2d20247.png)

### 6. sql语句的模糊匹配用like,concat进行拼接字符串

# TODO
1. [ ] Navicat设计界面展示UNIQUE 和 DEFAULT的值  
2. [ ] Swagger新增接口请求参数设置不显示id
3. [x] ~~idea的控制台的错误信息是否能设置的更显眼~~ 
4. [ ] insert语句,需要用mybatis去完成 
5. [ ] 处理SQL异常,之后用数据库校验报错信息返回前端
6. [ ] 深入了解ThreadLocal是什么?还有Thread是什么?ps:是关于多线程的
7. [ ] ThreadLocal类似于session?
8. [ ] mybatis和mybatis plus的区别,后续写第二版
