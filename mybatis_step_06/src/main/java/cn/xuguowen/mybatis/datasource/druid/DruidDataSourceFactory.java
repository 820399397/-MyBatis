package cn.xuguowen.mybatis.datasource.druid;

import cn.xuguowen.mybatis.datasource.DataSourceFactory;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * ClassName: DruidDataSourceFactory
 * Package: cn.xuguowen.mybatis.datasource.druid
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/15 22:06
 * @Version 1.0
 */
public class DruidDataSourceFactory implements DataSourceFactory {
    //相关配置
    private Properties properties;

    /**
     * 设置属性
     * @param props
     */
    @Override
    public void setProperties(Properties props) {
        this.properties = props;
    }

    /**
     * 获取数据源,这里用的是druid
     * @return
     */
    @Override
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(properties.getProperty("driver"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        return dataSource;
    }
}
