package cn.xuguowen.mybatis.session;


import cn.xuguowen.mybatis.binding.MapperProxyFactory;
import cn.xuguowen.mybatis.binding.MapperRegistry;
import cn.xuguowen.mybatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName: Configuration
 * Package: cn.xuguowen.mybatis.session
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/14 20:56
 * @Version 1.0
 */
public class Configuration {
    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);
    /**
     * 映射的语句，存在Map里 key是sql语句的id
     */
    private final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public void addMappedStatement(MappedStatement mappedStatement) {
        mappedStatements.put(mappedStatement.getId(), mappedStatement);
    }

    public void addMapper(Class<?> type) {
        mapperRegistry.addMapper(type);
    }

    public MappedStatement getMappedStatement(String statementId) {
        return mappedStatements.get(statementId);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }
}
