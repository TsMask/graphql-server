package com.tsmask.graphqlserver.datafetchers;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

@Component
public class GraphQLDataFetchers {

    /**
     * 输出world
     *
     * @return 返回字符串
     */
    public DataFetcher getHelloWorldDataFetcher() {
        return environment -> "world";
    }

    /**
     * 参数直接输出
     *
     * @return 返回字符串
     */
    public DataFetcher getEchoDataFetcher() {
        return environment -> environment.getArgument("toEcho");
    }

}