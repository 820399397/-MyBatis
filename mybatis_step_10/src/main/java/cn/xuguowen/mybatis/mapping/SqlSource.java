package cn.xuguowen.mybatis.mapping;

/**
 * ClassName: SqlSource
 * Package: cn.xuguowen.mybatis.mapping
 * Description:SqlSource接口，定义了获取SQL语句及其绑定参数的方法。
 *
 * @Author 徐国文
 * @Create 2024/5/30 9:37
 * @Version 1.0
 */
public interface SqlSource {
    /**
     * 根据参数对象获取绑定的SQL。
     *
     * @param parameterObject 参数对象
     * @return 绑定的SQL
     */
    BoundSql getBoundSql(Object parameterObject);
}
