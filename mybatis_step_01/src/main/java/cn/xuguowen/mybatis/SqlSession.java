package cn.xuguowen.mybatis;

import java.util.List;

/**
 * ClassName: SqlSession
 * Package: cn.xuguowen.mybatis
 * Description:定义了对数据库操作的查询接口，分为查询一个结果和查询多个结果，同时包括有参数和无参数的方法。
 * 在MyBatis中，所有SQL语句的执行是从开启SqlSession会话开始的，之后创建执行器。
 * 为了简化，这里直接在SqlSession会话实现类DefaultSqlSession中操作数据库并执行SQL语句。
 *
 * @Author 徐国文
 * @Create 2024/2/8 19:30
 * @Version 1.0
 */
public interface SqlSession {

    <T> T selectOne(String statementId, Object parameter);

    <T> T selectOne(String statementId);

    <T> List<T> selectList(String statementId);

    <T> List<T> selectList(String statementId,Object parameter);

    void close();
}
