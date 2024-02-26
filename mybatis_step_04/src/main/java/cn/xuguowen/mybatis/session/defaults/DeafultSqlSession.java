package cn.xuguowen.mybatis.session.defaults;

import cn.xuguowen.mybatis.binding.MapperRegistry;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.SqlSession;

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


    private Configuration configuration;

    public DeafultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        return (T) ("你的操作被代理了！ " + "方法：" + statementId + "，参数：" + parameter + "\n待执行SQL：" + mappedStatement.getSql());
    }

    @Override
    public <T> T selectOne(String statementId) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        return (T) ("你的操作被代理了！ " + "方法：" + statementId+ "\n待执行SQL：" + mappedStatement.getSql());
    }

    @Override
    public <T> List<T> selectList(String statementId) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        return Collections.singletonList((T) ("你的操作被代理了！ " + "方法：" + statementId + "\n待执行SQL：" + mappedStatement.getSql()));
    }

    @Override
    public <T> List<T> selectList(String statementId, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        return Collections.singletonList((T) ("你的操作被代理了！ " + "方法：" + statementId + "，参数：" + parameter + "\n待执行SQL：" + mappedStatement.getSql()));
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
