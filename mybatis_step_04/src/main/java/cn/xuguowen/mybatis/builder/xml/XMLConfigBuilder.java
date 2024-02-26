package cn.xuguowen.mybatis.builder.xml;

import cn.xuguowen.mybatis.builder.BaseBuilder;
import cn.xuguowen.mybatis.io.Resources;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.mapping.SqlCommandType;
import cn.xuguowen.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: XMLConfigBuilder
 * Package: cn.xuguowen.mybatis.builder.xml
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/14 20:55
 * @Version 1.0
 */
public class XMLConfigBuilder extends BaseBuilder {

    private Element root;

    public XMLConfigBuilder(Reader reader) {
        // 1. 调用父类初始化Configuration
        super(new Configuration());
        // 2. dom4j 处理 xml
        SAXReader saxReader = new SAXReader();
        try {
            Document doc = saxReader.read(new InputSource(reader));
            root = doc.getRootElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Configuration parse() {
        try {
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return super.configuration;
    }

    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element element : mapperList) {
            String resource = element.attributeValue("resource");
            Reader resourceAsReader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputSource(resourceAsReader));
            Element rootElement = document.getRootElement();

            String namespace = rootElement.attributeValue("namespace");

            // 解析<select>、<insert>、<update>、<delete>节点
            List<Element> select = rootElement.elements("select");
            for (Element selectElement : select) {
                String id = selectElement.attributeValue("id");
                String resultType = selectElement.attributeValue("resultType");
                String parameterType = selectElement.attributeValue("parameterType");
                String sql = selectElement.getTextTrim();

                // 这段代码是 XML 映射文件解析器中的一部分，主要用于解析 <select> 节点中的 SQL 语句，并将其中的参数占位符 #{} 替换为 ?
                // 首先，创建一个空的 HashMap 对象 parameterMap，用于存储参数的索引和名称
                HashMap<Integer, String> parameterMap = new HashMap<>();
                // 然后，使用正则表达式 (#{\\{(.*?)}) 匹配 SQL 语句中的参数占位符。这个正则表达式可以匹配形如 #{parameterName} 的字符串
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                // 使用 Matcher 对象对 SQL 语句进行匹配操作
                Matcher matcher = pattern.matcher(sql);
                // 在循环中，使用 matcher.find() 方法查找下一个匹配的字符串。如果找到了匹配项，则执行以下操作
                for (int i = 1; matcher.find(); i++) {
                    // 获取第一个匹配组的内容，即 #{parameterName}
                    String g1 = matcher.group(1);
                    // 获取第二个匹配组的内容，即 parameterName
                    String g2 = matcher.group(2);
                    // 将参数索引和参数名称存储到 parameterMap 中
                    parameterMap.put(i, g2);
                    // 使用 sql.replace(g1, "?") 将 SQL 语句中的 #{parameterName} 替换为 ?
                    sql = sql.replace(g1, "?");
                }
                String msId = namespace + "." + id;
                String nodeName = selectElement.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
                MappedStatement mappedStatement = new MappedStatement.Builder(configuration,msId,sqlCommandType,parameterType,resultType,sql,parameterMap).build();
                super.configuration.addMappedStatement(mappedStatement);
            }

            super.configuration.addMapper(Resources.classForName(namespace));

        }

    }
}
