package cn.xuguowen.mybatis.session;


import cn.xuguowen.mybatis.binding.MapperRegistry;
import cn.xuguowen.mybatis.datasource.druid.DruidDataSourceFactory;
import cn.xuguowen.mybatis.datasource.pooled.PooledDataSourceFactory;
import cn.xuguowen.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import cn.xuguowen.mybatis.mapping.Environment;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.transaction.jdbc.JdbcTransactionFactory;
import cn.xuguowen.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

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

    // 环境
    protected Environment environment;

    // 映射器注册机
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    // 映射语句 key为SQL语句的完整ID（namespace.id）
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    // 类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    }

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

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }

    public void setMapperRegistry(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    public Map<String, MappedStatement> getMappedStatements() {
        return mappedStatements;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }
}
