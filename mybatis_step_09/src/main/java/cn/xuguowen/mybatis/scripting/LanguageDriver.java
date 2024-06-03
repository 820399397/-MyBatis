package cn.xuguowen.mybatis.scripting;

import cn.xuguowen.mybatis.executor.parameter.ParameterHandler;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.mapping.SqlSource;
import cn.xuguowen.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * ClassName: LanguageDriver
 * Package: cn.xuguowen.mybatis.scripting
 * Description:定义了用于处理SQL脚本语言的接口，包含创建SQL源码和参数处理器的方法。
 *
 * @Author 徐国文
 * @Create 2024/5/30 9:38
 * @Version 1.0
 */
public interface LanguageDriver {

    /**
     * 创建SQL源码(mapper xml方式)
     *
     * @param configuration MyBatis的配置对象
     * @param script        代表SQL脚本的XML元素
     * @param parameterType 参数的类型
     * @return 解析后的SQL源码对象
     */
    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

    /**
     * 创建参数处理器
     *
     * @param mappedStatement MyBatis的映射语句对象
     * @param parameterObject 参数对象
     * @param boundSql        绑定的SQL对象
     * @return 参数处理器对象
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);

}
