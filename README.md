# Vue Naive Admin Server

Vue Naive Admin 的后端服务，基于 Spring Boot 3.x 和 MyBatis-Plus 实现。

## 技术栈

- Spring Boot 3.x
- Spring Security
- MyBatis-Plus
- MySQL
- Druid
- Caffeine Cache
- JWT
- Knife4j (OpenAPI 3)
- Hutool
- Lombok

## 功能特性

- [x] 用户认证与授权
- [x] 基于 RBAC 的权限管理
- [x] 部门管理
- [x] 角色管理
- [x] 菜单管理
- [x] 操作日志
- [x] 数据权限
- [x] 接口文档

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE vue_naive_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 修改 `application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vue_naive_admin
    username: your_username
    password: your_password
```

### 运行项目

```bash
mvn spring-boot:run
```

访问接口文档：http://localhost:8080/api/doc.html

## 许可证

[MIT](LICENSE) 