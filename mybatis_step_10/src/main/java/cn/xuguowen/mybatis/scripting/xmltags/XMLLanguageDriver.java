package cn.xuguowen.mybatis.scripting.xmltags;

import cn.xuguowen.mybatis.executor.parameter.ParameterHandler;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.mapping.SqlSource;
import cn.xuguowen.mybatis.scripting.LanguageDriver;
import cn.xuguowen.mybatis.scripting.defaults.DefaultParameterHandler;
import cn.xuguowen.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * ClassName: XMLLanguageDriver
 * Package: cn.xuguowen.mybatis.scripting.xmltags
 * Description:基于XML配置的语言驱动实现，用于解析XML格式的SQL脚本并创建相应的参数处理器。
 *
 * @Author 徐国文
 * @Create 2024/5/30 9:58
 * @Version 1.0
 */
public class XMLLanguageDriver implements LanguageDriver {

    /**
     * 解析XML脚本并创建SQL源码对象
     *
     * @param configuration MyBatis的配置对象
     * @param script        代表SQL脚本的XML元素
     * @param parameterType 参数的类型
     * @return 解析后的SQL源码对象
     */
    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        // 用XML脚本构建器解析
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }

    /**
     * 创建参数处理器
     *
     * @param mappedStatement MyBatis的映射语句对象
     * @param parameterObject 参数对象
     * @param boundSql        绑定的SQL对象
     * @return 参数处理器对象
     */
    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

}
