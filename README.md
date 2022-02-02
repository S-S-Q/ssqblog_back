# SSQBlog_Background

#### 介绍
个人博客搭建，后端部分。

后端部分功能主要有：

+ 接收md文件，提取md文件中的front-matter信息，然后存储到数据库中。
+ 通过Jwt进行权限认证（认证是否有权限更改博客）
+ 进行统计，统计博客标签。
+ 进行搜索，预计采用ElesticSearch实现（现在还未实现）

#### 软件架构
+ 主框架采用SpringBoot
+ 使用Mybatis-plus框架进行数据库增删查改
+ 使用Elesticsearch框架进行搜索
+ 使用Redis框架进行Jwt令牌存储，并且判断令牌是否过期
+ 使用SpringSecurity框架进行授权以及认证
+ 采用Swagger框架进行后端接口文档构建

#### 安装教程

+ 首先导入项目

+ 确保电脑（或者服务器）上安装有redis ,mysql ,以及ElesticSearch
+ 修改application.yaml中数据库，redis相关的配置内容
+ 通过ssqblog.sql文件导入相关表及数据到数据库中
+ 然后启动程序，访问localhost:8081接口

#### 前端仓库

[SSQBlog_Front: 个人博客前端 (gitee.com)](https://gitee.com/ssq_SSQ/SSQBlog_Front)

