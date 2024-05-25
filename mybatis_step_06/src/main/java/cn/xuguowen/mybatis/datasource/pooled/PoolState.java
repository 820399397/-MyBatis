package cn.xuguowen.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: PoolState
 * Package: cn.xuguowen.mybatis.datasource.pooled
 * Description:是一个管理数据库连接池状态的类。它通常用于管理和监控连接池的各种状态和统计信息。
 *
 * @Author 徐国文
 * @Create 2024/2/27 9:35
 * @Version 1.0
 */
public class PoolState {
    /**
     * 连接池的数据源对象
     */
    protected PooledDataSource dataSource;

    // 空闲链接:存储当前空闲的数据库连接
    protected final List<PooledConnection> idleConnections = new ArrayList<>();
    // 活跃链接:存储当前活跃的（正在使用的）数据库连接
    protected final List<PooledConnection> activeConnections = new ArrayList<>();

    // 记录请求数据库连接的总次数
    protected long requestCount = 0;
    // 累积的请求时间，用于统计请求数据库连接的总耗时
    protected long accumulatedRequestTime = 0;
    // 累积的检查时间，记录从连接池获取连接的总时间
    protected long accumulatedCheckoutTime = 0;
    // 记录被认定为超时的连接数量
    protected long claimedOverdueConnectionCount = 0;
    // 累积的超时连接的检查时间
    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;

    // 记录连接池中等待连接的总时间
    protected long accumulatedWaitTime = 0;
    // 记录必须等待才能获得连接的次数
    protected long hadToWaitCount = 0;
    // 记录获取到无效连接的次数
    protected long badConnectionCount = 0;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 返回总请求次数
     *
     * @return
     */
    public synchronized long getRequestCount() {
        return requestCount;
    }

    /**
     * 返回平均请求时间
     *
     * @return
     */
    public synchronized long getAverageRequestTime() {
        return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
    }

    /**
     * 返回平均等待时间
     *
     * @return
     */
    public synchronized long getAverageWaitTime() {
        return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;
    }

    /**
     * 返回等待次数
     *
     * @return
     */
    public synchronized long getHadToWaitCount() {
        return hadToWaitCount;
    }

    /**
     * 返回无效连接的次数
     *
     * @return
     */
    public synchronized long getBadConnectionCount() {
        return badConnectionCount;
    }

    /**
     * 返回超时连接的数量
     *
     * @return
     */
    public synchronized long getClaimedOverdueConnectionCount() {
        return claimedOverdueConnectionCount;
    }

    /**
     * 返回超时连接的平均检查时间
     *
     * @return
     */
    public synchronized long getAverageOverdueCheckoutTime() {
        return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
    }

    /**
     * 返回平均检查时间
     *
     * @return
     */
    public synchronized long getAverageCheckoutTime() {
        return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
    }

    /**
     * 返回空闲连接的数量
     *
     * @return
     */
    public synchronized int getIdleConnectionCount() {
        return idleConnections.size();
    }

    /**
     * 返回活跃连接的数量
     *
     * @return
     */
    public synchronized int getActiveConnectionCount() {
        return activeConnections.size();
    }
}
