package cn.xuguowen.mybatis.session;


import cn.xuguowen.mybatis.binding.MapperRegistry;
import cn.xuguowen.mybatis.datasource.druid.DruidDataSourceFactory;
import cn.xuguowen.mybatis.datasource.pooled.PooledDataSourceFactory;
import cn.xuguowen.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import cn.xuguowen.mybatis.executor.Executor;
import cn.xuguowen.mybatis.executor.SimpleExecutor;
import cn.xuguowen.mybatis.executor.resultset.DefaultResultSetHandler;
import cn.xuguowen.mybatis.executor.resultset.ResultSetHandler;
import cn.xuguowen.mybatis.executor.statement.PreparedStatementHandler;
import cn.xuguowen.mybatis.executor.statement.StatementHandler;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.Environment;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.reflection.MetaObject;
import cn.xuguowen.mybatis.reflection.factory.DefaultObjectFactory;
import cn.xuguowen.mybatis.reflection.factory.ObjectFactory;
import cn.xuguowen.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import cn.xuguowen.mybatis.reflection.wrapper.ObjectWrapperFactory;
import cn.xuguowen.mybatis.scripting.LanguageDriver;
import cn.xuguowen.mybatis.scripting.LanguageDriverRegistry;
import cn.xuguowen.mybatis.scripting.xmltags.XMLLanguageDriver;
import cn.xuguowen.mybatis.transaction.Transaction;
import cn.xuguowen.mybatis.transaction.jdbc.JdbcTransactionFactory;
import cn.xuguowen.mybatis.type.TypeAliasRegistry;
import cn.xuguowen.mybatis.type.TypeHandlerRegistry;
import cn.xuguowen.mybatis.executor.parameter.ParameterHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    protected final Set<String> loadedResources = new HashSet<>();

    // 类型处理器注册机
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    protected final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();

    // 对象工厂和对象包装器工厂
    protected ObjectFactory objectFactory = new DefaultObjectFactory();
    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();

    protected String databaseId;


    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

        languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
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

    /**
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
    }


    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, resultHandler, rowBounds, boundSql);
    }



    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction tx) {
        return new SimpleExecutor(this, tx);
    }

    // 类型处理器注册机
    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }


    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }


    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public LanguageDriverRegistry getLanguageRegistry() {
        return languageRegistry;
    }

    public LanguageDriver getDefaultScriptingLanguageInstance() {
        return languageRegistry.getDefaultDriver();
    }


    // 创建元对象
    public MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory);
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        // 创建参数处理器
        ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
        // 插件的一些参数，也是在这里处理，暂时不添加这部分内容 interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }


}
