package cn.xuguowen.mybatis;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName: DefaultSqlSession
 * Package: cn.xuguowen.mybatis
 * Description: DefaultSqlSession默认会话实现类简化了ORM框架的处理流程。
 * 该实现类包装了元素的提取、数据库的连接、JDBC的执行，并且完成了SQL语句执行时的入参、出参的处理，最终返回查询结果。
 *
 * @Author 徐国文
 * @Create 2024/2/8 19:36
 * @Version 1.0
 */
public class DefaultSqlSession implements SqlSession {

    // 数据库的连接
    private Connection connection;

    // key为SQL语句的ID，value是XML中的SQL语句标签节点对象
    private Map<String, XNode> mapperElement;

    public DefaultSqlSession(Connection connection, Map<String, XNode> mapperElement) {
        this.connection = connection;
        this.mapperElement = mapperElement;
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        return null;
    }

    @Override
    public <T> T selectOne(String statementId) {
        try {
            // 1. 获取SQL语句的XML配置标签
            XNode xNode = mapperElement.get(statementId);
            // 2.获取预处理SQL语句对象
            PreparedStatement pre = connection.prepareStatement(xNode.getSql());
            // 3.执行SQL语句
            ResultSet resultSet = pre.executeQuery();
            // 4.处理结果集
            List<T> objects = resultSet2Obj(resultSet, Class.forName(xNode.getResultType()));
            return objects.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public <T> List<T> selectList(String statementId) {
        try {
            XNode xNode = mapperElement.get(statementId);
            PreparedStatement pre = connection.prepareStatement(xNode.getSql());
            ResultSet resultSet = pre.executeQuery();
            return resultSet2Obj(resultSet, Class.forName(xNode.getResultType()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> selectList(String statementId, Object parameter) {
        return null;
    }

    @Override
    public void close() {
        if (Objects.isNull(connection)) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用反射将查询结果集转换为对象
     *
     * @param resultSet 结果集
     * @param clazz     对象类型
     * @param <T>
     * @return
     */
    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        List<T> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            // getColumnCount()方法可以获取ResultSet中的列数。例如：id、name、sex
            int columnCount = metaData.getColumnCount();
            // 每次遍历行值（记录）
            while (resultSet.next()) {
                T obj = (T) clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    // 获取列值
                    Object value = resultSet.getObject(i);
                    // 获取列名
                    String columnName = metaData.getColumnName(i);
                    // 利用反射，将列值设置到对象的属性中
                    String setMethodName = "set" + columnName.substring(0,1).toUpperCase() + columnName.substring(1);
                    Method method = null;
                    if (value instanceof Timestamp) {
                        method = clazz.getMethod(setMethodName, Date.class);
                    } else {
                        method = clazz.getMethod(setMethodName,value.getClass());
                    }
                    method.invoke(obj,value);
                }
                list.add(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
