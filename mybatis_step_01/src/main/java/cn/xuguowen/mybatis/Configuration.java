package cn.xuguowen.mybatis;

import java.sql.Connection;
import java.util.Map;

/**
 * ClassName: Configuration
 * Package: cn.xuguowen.mybatis
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/8 20:13
 * @Version 1.0
 */
public class Configuration {

    private Connection connection;

    private Map<String, XNode> mapperElement;

    private Map<String,String> dataSource;

    public Map<String, String> getDataSource() {
        return dataSource;
    }

    public void setDataSource(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Map<String, XNode> getMapperElement() {
        return mapperElement;
    }

    public void setMapperElement(Map<String, XNode> mapperElement) {
        this.mapperElement = mapperElement;
    }
}
