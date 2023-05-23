# music-box (backend)

## Api文档路径
http://124.70.195.38:8000/doc.html   
(此端口由nginx提供反代理服务)   

## 项目前端入口
http://124.70.195.38:9000/#/login   

## 运行前须知
1. 需要在/src/main/resources下追新增一个文件: application-dev.yaml
2. 在这个文件中追加下面这段代码 并修改数据库信息

```yaml
server:
  port: 8080

spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource  # DRUID
    url: jdbc:mysql://ip:port/database_name  # MYSQL
    username: your_username
    password: yout_password
    
file-url:
  song-base-url:  D:/tmp/music/
  cover-base-url:  D:/tmp/cover/
  menu-cover-base-url: D:/tmp/menu-cover/
  avatar-base-url: D:/tmp/avatar/
```

## 部署须知
```说明可能写的有不完善之处，如果你碰到难以处理的问题或有bug，请及时提交issue告诉我，收到后会给出反馈```
1. 本项目提供DockerFile做容器化部署，DockerFile位置: src/main/resources/bash/docker-deployment/Dockerfile
2. 若不会使用Docker，请阅读并使用bash部署，位置: src/main/resources/bash/regular-deployment
3. DockerFile具体使用方法: 
```shell
# [必须] 先maven package得到music-box-0.0.1-SNAPSHOT.jar
mkdir -p /home/music-box/backend
cd  /home/music-box/backend
# [必须] 此时需要传输文件music-box-0.0.1-SNAPSHOT.jar与Dockerfile到/home/music-box/backend
docker build -t music-box-backend .
# [可选] run - main 如果你不需要文件上传下载服务 运行这个(记得调整端口号和容器名)
docker run -p 8080:8080 --name music-box-backend-main-01 -d music-box-backend
# [推荐] run - with file 如果你需要完整的文件上传下载服务 运行这个(记得调整端口号和容器名)
docker run -p 7070:8080 \
-v /home/music-box/resources/song:/home/music-box/resources/song \
-v /home/music-box/resources/cover:/home/music-box/resources/cover \
-v /home/music-box/resources/menu-cover:/home/music-box/resources/menu-cover \
-v /home/music-box/resources/avatar:/home/music-box/resources/avatar \
--name music-box-backend-file-01 \
-d music-box-backend
```
4. 如果你想实现如下图所示的分布式结构，请配置nginx。如果使用容器部署nginx，请务必这样创建nginx容器，以确保目录被挂载成功(你还需多创建几个music-box-backend容器，并将src/main/resources/nginx-conf中的配置文件复制到/home/nginx/conf/nginx.conf中：
```shell
docker run \
-p 9000:9000 \
-p 8000:8000 \
--name nginx-music-box \
-v /home/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
-v /home/nginx/conf/conf.d:/etc/nginx/conf.d \
-v /home/nginx/log:/var/log/nginx \
-v /home/nginx/html:/usr/share/nginx/html \
-v /home/music-box/frontend/dist:/home/music-box/frontend/dist \
-v /home/music-box/resources/cover:/home/music-box/resources/cover \
-v /home/music-box/resources/avatar:/home/music-box/resources/avatar \
-v /home/music-box/resources/menu-cover:/home/music-box/resources/menu-cover \
-v /home/music-box/logs:/home/music-box/logs \
-d nginx:latest
```
![G4XR%0EG`2N}E6E} SXXXA3](https://github.com/Luyabs/music-box/assets/74538732/e13259a6-b5df-44f1-8690-655bd27f5d81)

## 项目结构
| 项目结构           |                           |                        | 描述              | 功能                                                              |
|----------------|---------------------------|------------------------|-----------------|-----------------------------------------------------------------|
| \common        | 通用层                       |                        |                 |                                                                 |
| …              | \exception                |                        |                 |                                                                 |
| …              | …                         | GlobalExceptionHandler | 全局异常处理器         | 处理service层及以下的exception，并以Result形式返回给前端                         |
| …              | …                         | ServiceException       | 自定义业务层异常类       | service层抛掷用异常                                                   |
| …              | \interceptor              |                        |                 |                                                                 |
| …              | …                         | JwtInterceptor         | JWT拦截器          | 预处理请求中的token，并通过UserInfo从token中解析出userId存到UserInfo.threadLocal中 |
| …              | …                         | Knife4jInterceptor         | K4J拦截器          | 在非dev环境下拦截所有对api文档的请求                                           |
| …              | AutoFillMetaObjectHandler |                        | 元数据处理器          | MybatisPlus 填充公共字段                                              |
| …              | JwtUtils                  |                        | Token生成器(JWT)   | 生成与解析token                                                      |
| …              | NeedToken                 |                        | 自定义注解           | 标记哪些方法需要在请求头中传入token                                       |
| …              | Result                    |                        | 数据一致性处理         | 返回统一格式                                                          |
| …              | UserInfo                  |                        | 封装线程副本工具类       | 在处理token时保存一份userId到此类中                                         |
|                |                           |                        |                 |                                                                 |
| \config        | 配置层(配置/提供Bean)            |                        |                 |                                                                 |
| …              | Knife4jConfig             |                        | API文档配置类        | 配置Knife4j api文档                                                 |
| …              | MybatisPlusConfig         |                        | Mybatis分页拦截器配置类 | 配置Mybatis分页查询功能                                                 |
| …              | RestTemplateConfig        |                        | RestTemplate类   | 负责Http请求                                                        |
| …              | WebMvcConfig              |                        | MVC配置类          | 配置静态资源映射 跨域映射 拦截器配置 序列化处理                                       |
|                |                           |                        |                 |                                                                 |
|                |                           |                        |                 |                                                                 |
| \controller    | 控制器层                      |                        | Controller      | 资源映射                                                            |
|                |                           |                        |                 |                                                                 |
| \service       | 业务层                       |                        | Model           | 逻辑处理 调用mapper 异常处理 安全保证 连中间件                                    |
|                |                           |                        |                 |                                                                 |
| \mapper        | 数据层                       |                        | Model           | 存放SQL                                                           |
|                |                           |                        |                 |                                                                 |
| \entity        | 实体类层                      |                        | Model           | 与单表对应的Bean                                                      |
|                |                           |                        |                 |                                                                 |
| \dto           | 实体对象转换层                   |                        | Model           | 在entity基础上封装或合并的Bean                                            |
|                |                           |                        |                 |                                                                 |
|                |                           |                        |                 |                                                                 |
| 非\java目录下的文件夹: |                           |                        |                 |                                                                 |
| \resources     | 配置文件                      |                        |                 |                                                                 |
|                |                           |                        |                 |                                                                 |
| \test          | SpringBoot 测试类            |                        |                 |                                                                 |
