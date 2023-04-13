# music-box

## 运行前须知
1. 需要在/src/main/resources下追新增一个文件: application-dev.yaml
2. 填写数据库信息

```yaml
server:
  port: 8080

spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource  # DRUID
    url: jdbc:mysql://ip:port/database_name  # MYSQL
    username: your_username
    password: yout_password
```
