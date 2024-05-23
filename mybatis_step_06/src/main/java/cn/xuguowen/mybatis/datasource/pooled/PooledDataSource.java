package cn.xuguowen.mybatis.datasource.pooled;

import cn.xuguowen.mybatis.datasource.unpooled.UnpooledDataSource;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.logging.Logger;

/**
 * ClassName: PooledDataSource
 * Package: cn.xuguowen.mybatis.datasource.pooled
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/27 9:26
 * @Version 1.0
 */
public class PooledDataSource implements DataSource {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(PooledDataSource.class);

    // 池状态
    private final PoolState state = new PoolState(this);

    // 活跃链接：正在被应用程序使用的连接。
    // 活跃连接数:指的是连接池中允许的最大并发数据库连接数。这个参数设置决定了应用程序在同一时间内可以同时使用的数据库连接的最大数量.这意味着最多可以同时有 10 个活跃连接
    protected int poolMaximumActiveConnections = 10;

    // 空闲链接：已经建立但未被当前使用，可以立即分配给新请求的连接。
    // 空闲连接数:指数据库连接池中当前未被使用但已经建立的数据库连接数量。这意味着连接池中始终至少保持 5 个空闲连接。
    protected int poolMaximumIdleConnections = 5;

    // 在被强制返回之前,池中连接被检查的时间
    protected int poolMaximumCheckoutTime = 20000;
    // 这是给连接池一个打印日志状态机会的低层次设置,还有重新尝试获得连接, 这些情况下往往需要很长时间 为了避免连接池没有配置时静默失败)。
    protected int poolTimeToWait = 20000;
    // 发送到数据的侦测查询,用来验证连接是否正常工作,并且准备 接受请求。默认是“NO PING QUERY SET” ,这会引起许多数据库驱动连接由一 个错误信息而导致失败
    protected String poolPingQuery = "NO PING QUERY SET";
    // 开启或禁用侦测查询
    protected boolean poolPingEnabled = false;
    // 用来配置 poolPingQuery 多次时间被用一次
    protected int poolPingConnectionsNotUsedFor = 0;

    private int expectedConnectionTypeCode;

    private final UnpooledDataSource dataSource;

    /**
     * 对无池化的UnpooledDataSource对象进行扩展处理
     */
    public PooledDataSource() {
        this.dataSource = new UnpooledDataSource();
    }

    /**
     * 这个方法实现了一个连接池中连接的返还逻辑，根据连接池当前的空闲连接数量和连接的状态，决定是将连接重新加入空闲连接池还是关闭连接。通过同步和条件判断，确保连接池在多线程环境下的正确性和高效性。
     * @param connection        需要被放回连接池的连接对象 PooledConnectio
     * @throws SQLException
     */
    protected void pushConnection(PooledConnection connection) throws SQLException {
        // 方法是同步的，使用了 synchronized 关键字锁定了 state 对象，确保连接池的状态在多线程环境下是一致的。
        synchronized (state) {
            // 从活跃连接列表中移除正在返回的连接。
            state.activeConnections.remove(connection);
            // 判断链接是否有效
            if (connection.isValid()) {
                // 检查空闲连接数是否少于最大空闲连接数，并且连接类型是否匹配
                if (state.idleConnections.size() < poolMaximumIdleConnections && connection.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    // 如果是，将连接的使用时间累加到 accumulatedCheckoutTime 中
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    // 如果连接不自动提交，回滚事务。
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    // 实例化一个新的DB连接，加入到idle列表
                    PooledConnection newConnection = new PooledConnection(connection.getRealConnection(), this);
                    state.idleConnections.add(newConnection);
                    // 设置新连接的创建和最后使用时间戳
                    newConnection.setCreatedTimestamp(connection.getCreatedTimestamp());
                    newConnection.setLastUsedTimestamp(connection.getLastUsedTimestamp());
                    // 将当前连接标记为无效。
                    connection.invalidate();
                    // 记录日志，通知所有等待连接的线程。
                    logger.info("Returned connection " + newConnection.getRealHashCode() + " to pool.");
                    // 通知其他线程可以来抢DB连接了
                    state.notifyAll();
                }
                // 否则，空闲链接还比较充足
                else {
                    // 将连接的使用时间累加到 accumulatedCheckoutTime 中。
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    // 如果连接不自动提交，回滚事务。
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    // 关闭真实的数据库连接。
                    connection.getRealConnection().close();
                    // 记录日志，将连接标记为无效。
                    logger.info("Closed connection " + connection.getRealHashCode() + ".");
                    connection.invalidate();
                }
            } else {
                // 如果连接无效，记录日志并增加无效连接计数 badConnectionCount。
                logger.info("A bad connection (" + connection.getRealHashCode() + ") attempted to return to the pool, discarding connection.");
                state.badConnectionCount++;
            }
        }
    }

    /**
     * 这个方法尝试从连接池中获取一个可用连接。
     * 如果有空闲连接，则直接返回。
     * 如果没有空闲连接但活跃连接数未达到最大值，则创建新连接。
     * 如果活跃连接数已满，则检查最老的活跃连接是否超时，如果超时则重新实例化该连接。
     * 如果所有方法都失败，则等待其他线程释放连接。该方法确保了连接池在多线程环境下的高效性和可靠性。
     * @param username      用户名
     * @param password      密码
     * @return  返回一个有效的 PooledConnection 对象。
     * @throws SQLException
     */
    private PooledConnection popConnection(String username, String password) throws SQLException {
        boolean countedWait = false;    // 是否已经记录等待次数
        PooledConnection conn = null;   // 要返回的连接对象
        long t = System.currentTimeMillis();    // 当前时间戳，用于统计请求时间
        int localBadConnectionCount = 0;        // 本地坏连接计数

        // 循环尝试获取连接，直到成功获得一个有效连接或发生异常。
        while (conn == null) {
            // 使用 synchronized 锁定 state 对象，确保多线程环境下的连接池状态一致
            synchronized (state) {
                // 如果空闲连接池中有连接，移除并返回第一个空闲连接。
                if (!state.idleConnections.isEmpty()) {
                    conn = state.idleConnections.remove(0);
                    logger.info("Checked out connection " + conn.getRealHashCode() + " from pool.");
                }
                // 如果无空闲链接：创建新的链接
                else {
                    // 活跃连接数不足：如果没有空闲连接且活跃连接数未达到最大值，创建一个新的连接并返回。
                    if (state.activeConnections.size() < poolMaximumActiveConnections) {
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        logger.info("Created connection " + conn.getRealHashCode() + ".");
                    }
                    // 活跃连接数已满
                    else {
                        // 取得活跃链接列表的第一个，也就是最老的一个连接、检查最老的活跃连接是否超时。如果超时，移除并重新实例化一个新的连接。
                        PooledConnection oldestActiveConnection = state.activeConnections.get(0);
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        // 如果checkout时间过长，则这个链接标记为过期
                        if (longestCheckoutTime > poolMaximumCheckoutTime) {
                            state.claimedOverdueConnectionCount++;
                            state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            state.accumulatedCheckoutTime += longestCheckoutTime;
                            state.activeConnections.remove(oldestActiveConnection);
                            if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
                                oldestActiveConnection.getRealConnection().rollback();
                            }
                            // 删掉最老的链接，然后重新实例化一个新的链接
                            conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
                            oldestActiveConnection.invalidate();
                            logger.info("Claimed overdue connection " + conn.getRealHashCode() + ".");
                        }
                        // 如果没有超时连接，则等待其他线程释放连接，等待时间为 poolTimeToWait 毫秒。
                        else {
                            try {
                                if (!countedWait) {
                                    state.hadToWaitCount++;
                                    countedWait = true;
                                }
                                logger.info("Waiting as long as " + poolTimeToWait + " milliseconds for connection.");
                                long wt = System.currentTimeMillis();
                                state.wait(poolTimeToWait);
                                state.accumulatedWaitTime += System.currentTimeMillis() - wt;
                            } catch (InterruptedException e) {
                                break;
                            }
                        }

                    }
                }
                // 如果获得了连接，检查连接的有效性。如果连接有效，重置连接状态并将其添加到活跃连接池。否则，记录坏连接，重试获取连接。
                if (conn != null) {
                    if (conn.isValid()) {
                        if (!conn.getRealConnection().getAutoCommit()) {
                            conn.getRealConnection().rollback();
                        }
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));
                        // 记录checkout时间
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        state.activeConnections.add(conn);
                        state.requestCount++;
                        state.accumulatedRequestTime += System.currentTimeMillis() - t;
                    } else {
                        logger.info("A bad connection (" + conn.getRealHashCode() + ") was returned from the pool, getting another connection.");
                        // 如果没拿到，统计信息：失败链接 +1
                        state.badConnectionCount++;
                        localBadConnectionCount++;
                        conn = null;
                        // 失败次数较多，抛异常
                        if (localBadConnectionCount > (poolMaximumIdleConnections + 3)) {
                            logger.debug("PooledDataSource: Could not get a good connection to the database.");
                            throw new SQLException("PooledDataSource: Could not get a good connection to the database.");
                        }
                    }
                }
            }
        }

        // 如果最终没有获得连接，抛出异常。
        if (conn == null) {
            logger.debug("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
            throw new SQLException("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
        }

        return conn;
    }

    /**
     * 用于强制关闭连接池中的所有连接，无论是活跃连接还是空闲连接。
     */
    public void forceCloseAll() {
        // 使用 synchronized 锁定 state 对象，确保多线程环境下的连接池状态一致。
        synchronized (state) {
            // 调用 assembleConnectionTypeCode 方法，根据数据源的 URL、用户名和密码来生成连接类型码 expectedConnectionTypeCode。这个类型码用于识别当前连接池所管理的连接。
            expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            // 逆序遍历 state.activeConnections 列表，逐个移除每个活跃连接。
            for (int i = state.activeConnections.size(); i > 0; i--) {
                try {
                    PooledConnection conn = state.activeConnections.remove(i - 1);
                    // 对每个连接，调用 conn.invalidate() 将其标记为无效。
                    conn.invalidate();

                    // 获取真实的数据库连接 realConn。
                    Connection realConn = conn.getRealConnection();
                    // 如果连接没有自动提交事务，则回滚未提交的事务。
                    if (!realConn.getAutoCommit()) {
                        realConn.rollback();
                    }
                    // 关闭真实的数据库连接 realConn。
                    realConn.close();
                } catch (Exception ignore) {
                    // 捕获和忽略可能的异常，确保即使某个连接关闭失败，其他连接也能继续被关闭。
                }
            }
            // 逆序遍历 state.idleConnections 列表，逐个移除每个空闲连接。
            for (int i = state.idleConnections.size(); i > 0; i--) {
                try {
                    PooledConnection conn = state.idleConnections.remove(i - 1);
                    // 对每个连接，调用 conn.invalidate() 将其标记为无效
                    conn.invalidate();

                    // 获取真实的数据库连接 realConn。
                    Connection realConn = conn.getRealConnection();
                    // 如果连接没有自动提交事务，则回滚未提交的事务。
                    if (!realConn.getAutoCommit()) {
                        realConn.rollback();
                    }
                    // 关闭真实的数据库连接 realConn。
                    realConn.close();
                } catch (Exception ignore) {
                    // 捕获和忽略可能的异常，确保即使某个连接关闭失败，其他连接也能继续被关闭。
                }
            }
            // 记录一条信息日志，表示连接池已强制关闭和移除所有连接。
            logger.info("PooledDataSource forcefully closed/removed all connections.");
        }
    }

    /**
     * 测试一个 PooledConnection 是否仍然有效。在数据库连接池中，保持连接的健康状态是非常重要的，这个方法帮助确保从连接池中获取的连接是有效的。
     * @param conn
     * @return
     */
    protected boolean pingConnection(PooledConnection conn) {
        boolean result = true;

        try {
            // 尝试检查 conn（即 PooledConnection）的实际连接是否已关闭
            result = !conn.getRealConnection().isClosed();
        } catch (SQLException e) {
            // 如果捕获到 SQLException，说明连接可能有问题，记录日志并将 result 设为 false
            logger.info("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
            result = false;
        }

        // 如果初始检查通过，继续进行详细的 Ping 测试。
        if (result) {
            // 检查 poolPingEnabled 是否启用。如果启用，继续检查
            if (poolPingEnabled) {
                // 检查连接的空闲时间是否超过设定的 poolPingConnectionsNotUsedFor，即只有在连接空闲时间超过设定值时才进行 Ping 测试。
                if (poolPingConnectionsNotUsedFor >= 0 && conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor) {
                    try {
                        logger.info("Testing connection " + conn.getRealHashCode() + " ...");
                        // 尝试执行 Ping 查询（通常是一个简单的 SQL 查询，如 SELECT 1）
                        Connection realConn = conn.getRealConnection();
                        Statement statement = realConn.createStatement();
                        ResultSet resultSet = statement.executeQuery(poolPingQuery);
                        resultSet.close();
                        if (!realConn.getAutoCommit()) {
                            realConn.rollback();
                        }
                        result = true;
                        logger.info("Connection " + conn.getRealHashCode() + " is GOOD!");
                    } catch (Exception e) {
                        logger.info("Execution of ping query '" + poolPingQuery + "' failed: " + e.getMessage());
                        try {
                            conn.getRealConnection().close();
                        } catch (SQLException ignore) {
                        }
                        result = false;
                        logger.info("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
                    }
                }
            }
        }

        return result;
    }

    /**
     * 从一个可能被代理的 Connection 对象中获取原始的数据库连接对象。如果传入的 Connection 是一个代理对象，它会返回该代理对象所封装的实际数据库连接；否则，它直接返回传入的连接对象。
     * @param conn
     * @return
     */
    public static Connection unwrapConnection(Connection conn) {
        // 使用 Proxy.isProxyClass 方法检查传入的 Connection 对象的类是否是一个代理类。如果是代理类，继续处理；否则，直接返回该连接。
        if (Proxy.isProxyClass(conn.getClass())) {
            // 使用 Proxy.getInvocationHandler 方法获取与代理类关联的 InvocationHandler 对象。
            InvocationHandler handler = Proxy.getInvocationHandler(conn);
            if (handler instanceof PooledConnection) {
                // 检查 InvocationHandler 是否为 PooledConnection 类型。如果是，调用 getRealConnection 方法获取并返回原始的数据库连接对象。
                return ((PooledConnection) handler).getRealConnection();
            }
        }
        return conn;
    }

    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

    @Override
    public Connection getConnection() throws SQLException {
        PooledConnection pooledConnection = popConnection(dataSource.getUsername(), dataSource.getPassword());
        return pooledConnection.getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }

    protected void finalize() throws Throwable {
        forceCloseAll();
        super.finalize();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        DriverManager.setLoginTimeout(loginTimeout);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public void setDriver(String driver) {
        dataSource.setDriver(driver);
        forceCloseAll();
    }

    public void setUrl(String url) {
        dataSource.setUrl(url);
        forceCloseAll();
    }

    public void setUsername(String username) {
        dataSource.setUsername(username);
        forceCloseAll();
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
        forceCloseAll();
    }


    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        dataSource.setAutoCommit(defaultAutoCommit);
        forceCloseAll();
    }

    public int getPoolMaximumActiveConnections() {
        return poolMaximumActiveConnections;
    }

    public void setPoolMaximumActiveConnections(int poolMaximumActiveConnections) {
        this.poolMaximumActiveConnections = poolMaximumActiveConnections;
    }

    public int getPoolMaximumIdleConnections() {
        return poolMaximumIdleConnections;
    }

    public void setPoolMaximumIdleConnections(int poolMaximumIdleConnections) {
        this.poolMaximumIdleConnections = poolMaximumIdleConnections;
    }

    public int getPoolMaximumCheckoutTime() {
        return poolMaximumCheckoutTime;
    }

    public void setPoolMaximumCheckoutTime(int poolMaximumCheckoutTime) {
        this.poolMaximumCheckoutTime = poolMaximumCheckoutTime;
    }

    public int getPoolTimeToWait() {
        return poolTimeToWait;
    }

    public void setPoolTimeToWait(int poolTimeToWait) {
        this.poolTimeToWait = poolTimeToWait;
    }

    public String getPoolPingQuery() {
        return poolPingQuery;
    }

    public void setPoolPingQuery(String poolPingQuery) {
        this.poolPingQuery = poolPingQuery;
    }

    public boolean isPoolPingEnabled() {
        return poolPingEnabled;
    }

    public void setPoolPingEnabled(boolean poolPingEnabled) {
        this.poolPingEnabled = poolPingEnabled;
    }

    public int getPoolPingConnectionsNotUsedFor() {
        return poolPingConnectionsNotUsedFor;
    }

    public void setPoolPingConnectionsNotUsedFor(int poolPingConnectionsNotUsedFor) {
        this.poolPingConnectionsNotUsedFor = poolPingConnectionsNotUsedFor;
    }

    public int getExpectedConnectionTypeCode() {
        return expectedConnectionTypeCode;
    }

    public void setExpectedConnectionTypeCode(int expectedConnectionTypeCode) {
        this.expectedConnectionTypeCode = expectedConnectionTypeCode;
    }
}
