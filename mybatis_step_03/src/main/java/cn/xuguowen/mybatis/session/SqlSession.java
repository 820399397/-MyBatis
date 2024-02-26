package cn.xuguowen.mybatis.session;

import cn.xuguowen.mybatis.binding.MapperRegistry;

import java.util.List;

/**
 * ClassName: SqlSession
 * Package: cn.xuguowen.mybatis.session
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:30
 * @Version 1.0
 */
public interface SqlSession {

    /**
     * 根据指定的statementId和参数parameter执行查询，将查询结果封装成一个Java对象并返回。
     *
     * @param statementId
     * @param parameter
     * @param <T>
     * @return
     */
    <T> T selectOne(String statementId, Object parameter);

    /**
     * 根据指定的statementId执行查询，将查询结果封装成一个Java对象并返回。
     *
     * @param statementId
     * @param <T>
     * @return
     */
    <T> T selectOne(String statementId);

    /**
     * 根据指定的statementId执行查询，将查询结果封装成一个Java对象列表并返回。
     *
     * @param statementId
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String statementId);

    /**
     * 根据指定的statementId和参数parameter执行查询，将查询结果封装成一个Java对象列表并返回。
     *
     * @param statementId
     * @param parameter
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String statementId, Object parameter);

    /**
     * 根据类对象获取映射器代理对象
     * @param type
     * @return
     * @param <T>
     */
    <T> T getMapper(Class<T> type);

    /**
     * 关闭会话
     */
    void close();
}
