package cn.xuguowen.mybatis.session.defaults;

import cn.xuguowen.mybatis.binding.MapperRegistry;
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
    /**
     * 映射器注册机
     */
    private MapperRegistry mapperRegistry;

    public DeafultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        return (T) ("你的操作被代理了！ " + "方法：" + statementId + "，参数：" + parameter);
    }

    @Override
    public <T> T selectOne(String statementId) {
        return (T) ("你的操作被代理了！ " + "方法：" + statementId);
    }

    @Override
    public <T> List<T> selectList(String statementId) {
        return Collections.singletonList((T) ("你的操作被代理了！ " + "方法：" + statementId));
    }

    @Override
    public <T> List<T> selectList(String statementId, Object parameter) {
        return Collections.singletonList((T) ("你的操作被代理了！ " + "方法：" + statementId + "，参数：" + parameter));
    }

    /**
     * 从映射器注册机中的缓存中获取
     * @param type
     * @return
     * @param <T>
     */
    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperRegistry.getMapper(type,this);
    }

    @Override
    public void close() {

    }
}
