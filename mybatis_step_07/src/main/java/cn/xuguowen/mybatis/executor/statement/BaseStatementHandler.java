package cn.xuguowen.mybatis.executor.statement;

import cn.xuguowen.mybatis.executor.Executor;
import cn.xuguowen.mybatis.executor.resultset.ResultSetHandler;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * ClassName: BaseStatementHandler
 * Package: cn.xuguowen.mybatis.executor.statement
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:46
 * @Version 1.0
 */
public abstract class BaseStatementHandler implements StatementHandler{

    // 配置对象，包含了 MyBatis 的配置和相关信息
    protected final Configuration configuration;

    // 执行器，用于执行 SQL 语句
    protected final Executor executor;

    // 映射的 SQL 语句对象，包含了 SQL 语句和相关的配置信息。
    protected final MappedStatement mappedStatement;

    // 参数对象，包含了执行 SQL 语句时所需的参数。
    protected final Object parameterObject;

    // 结果集处理器，用于处理 SQL 查询的结果集。
    protected final ResultSetHandler resultSetHandler;

    // 绑定的 SQL 对象，包含了 SQL 语句和相关的参数信息。
    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject,BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;

        this.parameterObject = parameterObject;
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 准备 SQL 语句
     * @param connection        数据库连接对象，用于执行 SQL 语句。
     * @return                  准备好的 SQL 语句对象，可能是 Statement、PreparedStatement 或 CallableStatement 的实例。
     * @throws SQLException
     */
    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            // 实例化 Statement
            statement = instantiateStatement(connection);
            // 参数设置，可以被抽取，提供配置
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        } catch (Exception e) {
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}
