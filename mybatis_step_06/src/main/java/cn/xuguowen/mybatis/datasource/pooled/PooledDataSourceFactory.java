package cn.xuguowen.mybatis.datasource.pooled;

import cn.xuguowen.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;

/**
 * ClassName: PooledDataSourceFactory
 * Package: cn.xuguowen.mybatis.datasource.pooled
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/28 13:39
 * @Version 1.0
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    @Override
    public DataSource getDataSource() {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver(super.props.getProperty("driver"));
        pooledDataSource.setUrl(super.props.getProperty("url"));
        pooledDataSource.setUsername(super.props.getProperty("username"));
        pooledDataSource.setPassword(super.props.getProperty("password"));
        return pooledDataSource;
    }
}
