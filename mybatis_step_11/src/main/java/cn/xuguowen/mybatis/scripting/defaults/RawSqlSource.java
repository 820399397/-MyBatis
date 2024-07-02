package cn.xuguowen.mybatis.scripting.defaults;

import cn.xuguowen.mybatis.builder.SqlSourceBuilder;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.SqlSource;
import cn.xuguowen.mybatis.scripting.xmltags.DynamicContext;
import cn.xuguowen.mybatis.scripting.xmltags.SqlNode;
import cn.xuguowen.mybatis.session.Configuration;

import java.util.HashMap;

/**
 * ClassName: RawSqlSource
 * Package: cn.xuguowen.mybatis.scripting.defaults
 * Description:直接使用原始 SQL 语句的 SqlSource 实现类。
 *
 * @Author 徐国文
 * @Create 2024/5/30 10:00
 * @Version 1.0
 */
public class RawSqlSource implements SqlSource {

    // 内部使用的 SqlSource
    private final SqlSource sqlSource;

    /**
     * 构造方法，通过 SqlNode 构建 RawSqlSource。
     *
     * @param configuration MyBatis 配置对象
     * @param rootSqlNode   根 SQL 节点
     * @param parameterType 参数类型
     */
    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType);
    }

    /**
     * 构造方法，通过 SQL 字符串构建 RawSqlSource。
     *
     * @param configuration MyBatis 配置对象
     * @param sql           SQL 字符串
     * @param parameterType 参数类型
     */
    public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<>());
    }

    /**
     * 获取绑定的 SQL。
     *
     * @param parameterObject 参数对象
     * @return 绑定的 SQL 对象
     */
    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    /**
     * 从 SqlNode 获取 SQL 字符串。
     *
     * @param configuration MyBatis 配置对象
     * @param rootSqlNode   根 SQL 节点
     * @return SQL 字符串
     */
    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(configuration, null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

}
