package com.tsmask.graphqlserver.provider;

import com.tsmask.graphqlserver.datafetchers.GraphQLDataFetchers;
import com.tsmask.graphqlserver.datafetchers.UserDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GraphQLProvider {

    private final GraphQLDataFetchers graphQLDataFetchers;
    private final UserDataFetcher userDataFetcher;

    @Autowired
    public GraphQLProvider(GraphQLDataFetchers graphQLDataFetchers, UserDataFetcher userDataFetcher) {
        this.graphQLDataFetchers = graphQLDataFetchers;
        this.userDataFetcher = userDataFetcher;
    }

    /**
     * 数据类型方法对应编写
     *
     * @return RuntimeWiring对应执行方法
     */
    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                // 查询方法R/get
                .type("Query", builderFunction -> builderFunction
                        .dataFetcher("hello", graphQLDataFetchers.getHelloWorldDataFetcher())
                        .dataFetcher("echo", graphQLDataFetchers.getEchoDataFetcher())
                        .dataFetcher("users", userDataFetcher.getUsersDataFetcher())
                        .dataFetcher("user", userDataFetcher.getUserByIdDataFetcher())
                )
                // 级联字段关联查询
                .type("User", builderFunction -> builderFunction.dataFetcher("info", userDataFetcher.getInfoDataFetcher()))
                // 增改删方法CUD/post/put/del
                .type("Mutation", builderFunction -> builderFunction
                        .dataFetcher("createUser", userDataFetcher.createUserDataFetcher())
                        .dataFetcher("updateUser", userDataFetcher.updateUserDataFetcher())
                        .dataFetcher("deleteUser", userDataFetcher.deleteUserDataFetcher())
                )
                .build();
    }

    @Bean
    public GraphQL graphQL() throws IOException {
        // SDL读取查询类型文件，new SchemaParser().parse(?)解析File、InputStream、String
        // ClassPathResource classPathResource = new ClassPathResource("schema.graphql");
        // TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(classPathResource.getInputStream());
        // 多SDL文件注册
        // ClassPathResource UserSchema = new ClassPathResource("schema/UserSchema.graphql");
        // ClassPathResource schema = new ClassPathResource("schema/QuerySchema.graphql");
        // TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        // SchemaParser schemaParser = new SchemaParser();
        // typeRegistry.merge(schemaParser.parse(UserSchema.getInputStream()));
        // typeRegistry.merge(schemaParser.parse(schema.getInputStream()));
        // 遍历解析目录下的schema，没找到直接获取文件列表的方法
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        SchemaParser schemaParser = new SchemaParser();
        String[] schemaArr = {"UserSchema", "QuerySchema", "MutationSchema"};
        for (String str : schemaArr) {
            typeRegistry.merge(schemaParser.parse(new ClassPathResource("schema/" + str + ".graphql").getInputStream()));
        }

        RuntimeWiring runtimeWiring = buildWiring();  // 数据方法对应编写
        SchemaGenerator schemaGenerator = new SchemaGenerator();  // 查询器构建
        // 查询生成
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

}