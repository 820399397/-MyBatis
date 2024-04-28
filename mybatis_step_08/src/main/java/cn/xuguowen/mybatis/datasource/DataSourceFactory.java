package cn.xuguowen.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * ClassName: DataSourceFactory
 * Package: cn.xuguowen.mybatis.datasource
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/15 22:05
 * @Version 1.0
 */
public interface DataSourceFactory {
    /**
     * 设置属性
     * @param props
     */
    void setProperties(Properties props);

    /**
     * 获取数据源
     * @return
     */
    DataSource getDataSource();
}
