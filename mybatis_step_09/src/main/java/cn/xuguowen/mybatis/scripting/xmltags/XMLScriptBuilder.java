package cn.xuguowen.mybatis.scripting.xmltags;

import cn.xuguowen.mybatis.builder.BaseBuilder;
import cn.xuguowen.mybatis.mapping.SqlSource;
import cn.xuguowen.mybatis.scripting.defaults.RawSqlSource;
import cn.xuguowen.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: XMLScriptBuilder
 * Package: cn.xuguowen.mybatis.scripting.xmltags
 * Description:XML 脚本构建器，用于解析 XML 形式的 SQL 脚本。
 *
 * @Author 徐国文
 * @Create 2024/5/30 9:59
 * @Version 1.0
 */
public class XMLScriptBuilder extends BaseBuilder {
    // XML 元素节点
    private Element element;

    // 是否包含动态 SQL
    private boolean isDynamic;

    // 参数类型
    private Class<?> parameterType;

    /**
     * 构造一个 XML 脚本构建器。
     *
     * @param configuration MyBatis 配置对象
     * @param element       XML 元素节点
     * @param parameterType 参数类型
     */
    public XMLScriptBuilder(Configuration configuration, Element element, Class<?> parameterType) {
        super(configuration);
        this.element = element;
        this.parameterType = parameterType;
    }


    /**
     * 解析 XML 脚本节点，构建 SQL 源对象。
     *
     * @return 解析得到的 SQL 源对象
     */
    public SqlSource parseScriptNode() {
        List<SqlNode> contents = parseDynamicTags(element);
        MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
        return new RawSqlSource(configuration, rootSqlNode, parameterType);
    }

    /**
     * 解析动态标签，获取 SQL 内容。
     *
     * @param element XML 元素节点
     * @return 解析得到的 SQL 节点列表
     */
    List<SqlNode> parseDynamicTags(Element element) {
        List<SqlNode> contents = new ArrayList<>();
        // element.getText 拿到 SQL
        String data = element.getText();
        contents.add(new StaticTextSqlNode(data));
        return contents;
    }

}
