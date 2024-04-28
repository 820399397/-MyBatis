package cn.xuguowen.mybatis.executor.resultset;

import cn.xuguowen.mybatis.executor.Executor;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: DefaultResultSetHandler
 * Package: cn.xuguowen.mybatis.executor.resultset
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/29 13:02
 * @Version 1.0
 */
public class DefaultResultSetHandler implements ResultSetHandler{
    private final BoundSql boundSql;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        this.boundSql = boundSql;
    }

    @Override
    public <E> List<E> handleResultSets(Statement stmt) throws SQLException {
        ResultSet resultSet = stmt.getResultSet();
        try {
            return resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
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
