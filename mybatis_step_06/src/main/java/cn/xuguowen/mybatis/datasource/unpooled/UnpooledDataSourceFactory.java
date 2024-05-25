package cn.xuguowen.mybatis.datasource.unpooled;

import cn.xuguowen.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * ClassName: UnpooledDataSourceFactory
 * Package: cn.xuguowen.mybatis.datasource.unpooled
 * Description:无池化工厂
 *
 * @Author 徐国文
 * @Create 2024/2/28 13:38
 * @Version 1.0
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected Properties props;

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }

    @Override
    public DataSource getDataSource() {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(props.getProperty("driver"));
        unpooledDataSource.setUrl(props.getProperty("url"));
        unpooledDataSource.setUsername(props.getProperty("username"));
        unpooledDataSource.setPassword(props.getProperty("password"));
        return unpooledDataSource;
    }
}
