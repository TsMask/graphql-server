# graphql-server

![奚视界 -【红昭愿】](https://pic.hanfugou.com/android/2020/0/18/b520c887a4aa4960a21eaf945aec68da.jpg_700x.jpg)

## [GraphQL 介绍](https://graphql.cn/)
>GraphQL 既是一种用于 API 的查询语言也是一个满足你数据查询的运行时。 GraphQL 对你的 API 中的数据提供了一套易于理解的完整描述，使得客户端能够准确地获得它需要的数据，而且没有任何冗余，也让 API 更容易地随着时间推移而演进，还能用于构建强大的开发者工具。

GraphQL Java和Spring Boot结合实践

## 开始
根据官网的示例：[GraphQL Java和Spring Boot入门](https://www.graphql-java.com/tutorials/getting-started-with-spring-boot/) 

使用依赖发布时间最新版本v14,另外使用`mvc`做`url`地址映射`/graphql`作为请求入口。
```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>com.graphql-java</groupId>
            <artifactId>graphql-java</artifactId>
            <version>14.0</version>
        </dependency>
    </dependencies>
```

>`springboot`构建版本：`2.2.2.RELEASE`，建议改为你目前使用的版本，避免再次下载。

项目使用`springboot`与`maven`方式构建，微服务采用`restful`和`graphql`两种方式进行开发，两者相辅相成，比如：上传、websocket等一些接口混用的模式。