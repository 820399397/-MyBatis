package cn.xuguowen.mybatis.executor;

import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.ResultHandler;
import cn.xuguowen.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * ClassName: Executor
 * Package: cn.xuguowen.mybatis.executor
 * Description:执行器：提供了一个统一的接口来执行查询、提交和回滚事务，并关闭执行器。
 * 具体的实现类会根据不同的需求来实现这些方法，例如简单的执行器、批量执行器等。
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:32
 * @Version 1.0
 */
public interface Executor {
    /**
     * 执行查询操作时，如果不需要处理结果集，就可以使用这个常量。
     */
    ResultHandler NO_RESULT_HANDLER = null;

    /**
     * 执行 SQL 查询的入口方法
     * @param ms                表示一个映射的 SQL 语句，包含了 SQL 的配置信息。
     * @param parameter         查询参数，用于 SQL 的参数化查询。
     * @param resultHandler     结果处理器，用于处理查询结果。如果不需要特别处理结果集，可以传入 NO_RESULT_HANDLER。
     * @param boundSql          表示绑定的 SQL，包含了生成的 SQL 字符串以及绑定的参数。
     * @return                  返回一个包含查询结果的列表
     * @param <E>
     */
    <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    /**
     * 获取当前的事务对象，用于管理事务的提交和回滚。
     * @return
     */
    Transaction getTransaction();

    /**
     * 提交事务，如果提交失败，会抛出 SQLException 异常。
     * @param required      是否需要提交事务。true 表示提交事务，false 表示可能根据某些条件决定不提交。
     * @throws SQLException
     */
    void commit(boolean required) throws SQLException;

    /**
     * 回滚事务，如果回滚失败，会抛出 SQLException 异常。
     * @param required      是否需要回滚事务。true 表示回滚事务，false 表示可能根据某些条件决定不回滚。
     * @throws SQLException
     */
    void rollback(boolean required) throws SQLException;

    /**
     * 关闭执行器，释放资源。如果有未提交的事务，根据参数决定是否回滚。
     * @param forceRollback 是否强制回滚未提交的事务。true 表示强制回滚，false 表示根据事务状态决定是否回滚。
     */
    void close(boolean forceRollback);

}
