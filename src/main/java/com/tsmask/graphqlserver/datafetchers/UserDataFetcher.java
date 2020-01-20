package com.tsmask.graphqlserver.datafetchers;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDataFetcher {

    /**
     * 固定用户数据
     *
     * @return 列表嵌套
     */
    private List<Map<String, String>> users() {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("id", "user-1");
        map1.put("username", "11Harry Potter and the Philosopher's Stone");
        map1.put("password", "13");
        map1.put("info", "info-1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("id", "user-2");
        map2.put("username", "22Harry Potter and the Philosopher's Stone");
        map2.put("password", "223");
        map2.put("info", "info-2");
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("id", "user-3");
        map3.put("username", "32Harry Potter and the Philosopher's Stone");
        map3.put("password", "33");
        map3.put("info", "info-3");
        return Arrays.asList(map1, map2, map3);
    }

    /**
     * 固定用户信息数据
     *
     * @return 列表嵌套
     */
    private List<Map<String, String>> infos() {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("id", "info-1");
        map1.put("age", "11");
        map1.put("firstName", "11Herman");
        map1.put("lastName", "11Rowling");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("id", "info-2");
        map2.put("age", "22");
        map2.put("firstName", "22Herman");
        map2.put("lastName", "22Rowling");
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("id", "info-3");
        map3.put("age", "33");
        map3.put("firstName", "33Herman");
        map3.put("lastName", "33Rowling");
        return Arrays.asList(map1, map2, map3);
    }

    /**
     * 查询所有
     *
     * @return 执行函数
     */
    public DataFetcher getUsersDataFetcher() {
        return environment -> {
            System.err.println("getUsersDataFetcher查询指定字段：" + environment.getSelectionSet().getArguments().keySet());
            System.err.println("getUsersDataFetcher参数变量：" + environment.getVariables());
            return users();
        };
    }

    /**
     * 根据id查询
     *
     * @return 执行函数
     */
    public DataFetcher getUserByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String userID = dataFetchingEnvironment.getArgument("id");
            return users()
                    .stream()
                    .filter(user -> user.get("id").equals(userID))
                    .findFirst()
                    .orElse(null);
        };
    }

    /**
     * 级联查询
     *
     * @return 执行函数
     */
    public DataFetcher getInfoDataFetcher() {
        return dataFetchingEnvironment -> {
            System.err.println("getInfoDataFetcher查询指定字段：" + dataFetchingEnvironment.getSelectionSet().getArguments().keySet());
            Map<String, String> user = dataFetchingEnvironment.getSource();
            System.out.println("getInfoDataFetcher级联查询info字段：" + user);
            String infoID = user.get("info");
            return infos()
                    .stream()
                    .filter(info -> info.get("id").equals(infoID))
                    .findFirst()
                    .orElse(null);
        };
    }

    /**
     * 创建执行
     *
     * @return 执行函数
     */
    public DataFetcher createUserDataFetcher() {
        return dataFetchingEnvironment -> {
            System.err.println("createUserDataFetcher变量值：" + dataFetchingEnvironment.getVariables());
            // 通过环境参数取值，建议用json工具解 gson等
            Map<String, String> mapArg = dataFetchingEnvironment.getArgument("user");
            // 判断info信息是否存在
            if (mapArg.containsKey("info")) {
                // 信息对象map，建议用json工具解 gson等
                Map<String, Object> infoArg = dataFetchingEnvironment.getArgument("user");
                HashMap mapInfo = (HashMap) infoArg.get("info");
                System.out.println("createUserDataFetcher信息对象：" + mapInfo);
            }
            // 用户id 随机生成
            mapArg.put("id", "user-" + Math.round(Math.random() * 100) + "");
            // 信息id，级联用到，先默认1
            mapArg.put("info", "info-1");
            return mapArg;
        };
    }

    /**
     * 更新执行
     *
     * @return 执行函数
     */
    public DataFetcher updateUserDataFetcher() {
        return dataFetchingEnvironment -> {
            System.err.println("updateUserDataFetcher变量值：" + dataFetchingEnvironment.getVariables());
            String userID = dataFetchingEnvironment.getArgument("id");
            System.out.println("updateUserDataFetcher查询id：" + userID);
            Map<String, String> map = users()
                    .stream()
                    .filter(user -> user.get("id").equals(userID))
                    .findFirst()
                    .orElse(null);
            // 通过参数取值，建议用json工具解 gson等
            Map<String, String> mapArg = dataFetchingEnvironment.getArgument("user");
            // 判断info信息是否存在
            if (mapArg.containsKey("info")) {
                // 信息对象map，建议用json工具解 gson等
                Map<String, Object> infoArg = dataFetchingEnvironment.getArgument("user");
                HashMap mapInfo = (HashMap) infoArg.get("info");
                System.out.println("updateUserDataFetcher信息对象：" + mapInfo);
            }
            // 判断password信息是否存在
            if (mapArg.containsKey("password")) {
                map.put("password", mapArg.get("password"));
            }
            return map;
        };
    }

    /**
     * 删除执行
     *
     * @return 执行函数
     */
    public DataFetcher deleteUserDataFetcher() {
        return dataFetchingEnvironment -> {
            String userID = dataFetchingEnvironment.getArgument("id");
            System.out.println("deleteUserDataFetcher软删除：" + userID);
            return users()
                    .stream()
                    .filter(user -> user.get("id").equals(userID))
                    .findFirst()
                    .orElse(null);
        };
    }

}
