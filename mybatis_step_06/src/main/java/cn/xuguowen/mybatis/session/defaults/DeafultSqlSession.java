package cn.xuguowen.mybatis.session.defaults;

import cn.xuguowen.mybatis.mapping.Environment;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * ClassName: DeafultSqlSession
 * Package: cn.xuguowen.mybatis.session
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:34
 * @Version 1.0
 */
public class DeafultSqlSession implements SqlSession {


    private Configuration configuration;

    public DeafultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        try {
            MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
            Environment environment = configuration.getEnvironment();

            Connection connection = environment.getDataSource().getConnection();
            String sql = mappedStatement.getBoundSql().getSql();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,Long.parseLong(((Object[]) parameter)[0].toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> objects = this.resultSet2Obj(resultSet, Class.forName(mappedStatement.getBoundSql().getResultType()));
            return objects.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T selectOne(String statementId) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        return (T) ("你的操作被代理了！ " + "方法：" + statementId+ "\n待执行SQL：" + mappedStatement.getBoundSql().getSql());
    }

    @Override
    public <T> List<T> selectList(String statementId) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        return Collections.singletonList((T) ("你的操作被代理了！ " + "方法：" + statementId + "\n待执行SQL：" + mappedStatement.getBoundSql().getSql()));
    }

    @Override
    public <T> List<T> selectList(String statementId, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        return Collections.singletonList((T) ("你的操作被代理了！ " + "方法：" + statementId + "，参数：" + parameter + "\n待执行SQL：" + mappedStatement.getBoundSql().getSql()));
    }

    /**
     * 从映射器注册机中的缓存中获取
     * @param type
     * @return
     * @param <T>
     */
    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type,this);
    }

    @Override
    public void close() {

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
