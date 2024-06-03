package cn.xuguowen.mybatis.session.defaults;

import cn.xuguowen.mybatis.executor.Executor;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.SqlSession;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * ClassName: DeafultSqlSession
 * Package: cn.xuguowen.mybatis.session
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:34
 * @Version 1.0
 */
public class DeafultSqlSession implements SqlSession {

    private Logger logger = LoggerFactory.getLogger(DefaultSqlSession.class);

    private Configuration configuration;

    private Executor executor;


    public DeafultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    public DeafultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        logger.info("执行查询 statement：{} parameter：{}", statementId, JSON.toJSONString(parameter));
        MappedStatement ms = configuration.getMappedStatement(statementId);
        List<T> list = executor.query(ms, parameter, Executor.NO_RESULT_HANDLER, ms.getSqlSource().getBoundSql(parameter));
        return list.get(0);
    }

    @Override
    public <T> T selectOne(String statementId) {
        return this.selectOne(statementId, null);
    }

    @Override
    public <T> List<T> selectList(String statementId) {
        return this.selectList(statementId,null);
    }

    @Override
    public <T> List<T> selectList(String statementId, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        return Collections.singletonList((T) ("你的操作被代理了！ " + "方法：" + statementId + "，参数：" + parameter + "\n待执行SQL：" + mappedStatement.getSqlSource().getBoundSql(parameter)));
    }

    /**
     * 从映射器注册机中的缓存中获取
     * @param type
     * @return
     * @param <T>
     */
    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type,this);
    }

    @Override
    public void close() {

    }
}
