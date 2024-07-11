package cn.xuguowen.mybatis.scripting.defaults;


import cn.xuguowen.mybatis.executor.parameter.ParameterHandler;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.mapping.ParameterMapping;
import cn.xuguowen.mybatis.reflection.MetaObject;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.type.JdbcType;
import cn.xuguowen.mybatis.type.TypeHandler;
import cn.xuguowen.mybatis.type.TypeHandlerRegistry;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * ClassName: DefaultParameterHandler
 * Package: cn.xuguowen.mybatis.scripting.defaults
 * Description:默认的参数处理器，实现了 ParameterHandler 接口，用于处理 SQL 参数的设置。
 *
 * @Author 徐国文
 * @Create 2024/5/30 11:07
 * @Version 1.0
 */
public class DefaultParameterHandler implements ParameterHandler {
    // 日志记录器
    private Logger logger = LoggerFactory.getLogger(DefaultParameterHandler.class);

    // 类型处理器注册器
    private final TypeHandlerRegistry typeHandlerRegistry;

    // 映射语句
    private final MappedStatement mappedStatement;

    // 参数对象
    private final Object parameterObject;

    // 绑定的 SQL
    private BoundSql boundSql;

    // 配置对象
    private Configuration configuration;

    /**
     * 构造一个默认的参数处理器。
     *
     * @param mappedStatement 映射语句
     * @param parameterObject 参数对象
     * @param boundSql        绑定的 SQL
     */
    public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
    }

    /**
     * 获取参数对象。
     *
     * @return 参数对象
     */
    @Override
    public Object getParameterObject() {
        return parameterObject;
    }

    /**
     * 设置 SQL 参数。
     *
     * @param ps 预编译的 SQL 语句
     * @throws SQLException SQL 异常
     */
    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        // logger.info("Parameter object type: {}", parameterObject.getClass());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (null != parameterMappings) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String propertyName = parameterMapping.getProperty();
                Object value;
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    value = parameterObject;
                } else {
                    // 通过 MetaObject.getValue 反射取得值设进去
                    MetaObject metaObject = configuration.newMetaObject(parameterObject);
                    value = metaObject.getValue(propertyName);
                }
                JdbcType jdbcType = parameterMapping.getJdbcType();

                // 设置参数
                logger.info("根据每个ParameterMapping中的TypeHandler设置对应的参数信息 value：{}", JSON.toJSONString(value));
                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                typeHandler.setParameter(ps, i + 1, value, jdbcType);
            }
        }
    }

}
