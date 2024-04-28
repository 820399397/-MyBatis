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
 * Description:执行器
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:32
 * @Version 1.0
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}
