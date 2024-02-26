package cn.xuguowen.mybatis;

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.io.Resources;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: SqlSessionFactoryBuilder
 * Package: cn.xuguowen.mybatis
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/8 20:16
 * @Version 1.0
 */
public class SqlSessionFactoryBuilder {

    /**
     * 构建SqlSessionFactory:主要用于创建解析XML文件的类，以及初始化SqlSession工厂类DefaultSqlSessionFactory。
     * 另外,saxReader.setEntityResolver(new XMLMapperEntityResolver());这句代码是为了保证在不连接网络时也可以解析XML文件，否则需要从互联网上获取DTD文件。
     * @param reader
     * @return
     */
    public DefaultSqlSessionFactory build(Reader reader) {
        SAXReader saxReader = new SAXReader();
        try {
            saxReader.setEntityResolver(new XMLMapperEntityResolver());
            Document documet = saxReader.read(new InputSource(reader));
            Configuration configuration = parseConfiguration(documet.getRootElement());
            return new DefaultSqlSessionFactory(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 封装Configuration对象：获取XML文件中的元素（一个是数据库的连接信息，另一个是对数据库操作语句的解析）
     * @param root
     * @return
     */
    private Configuration parseConfiguration(Element root) {
        Configuration configuration = new Configuration();
        configuration.setDataSource(dataSource(root.selectNodes("//property")));
        configuration.setMapperElement(mapperElement(root.selectNodes("//mapper")));
        configuration.setConnection(connection(configuration.getDataSource()));
        return configuration;
    }

    /**
     * 获取数据库连接信息
     * @param dataSource
     * @return
     */
    private Connection connection(Map<String, String> dataSource) {
        try {
            Class.forName(dataSource.get("driver"));
            return DriverManager.getConnection(dataSource.get("url"), dataSource.get("username"), dataSource.get("password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取SQL语句信息：虽然这块代码相对较长，但其核心是解析XML文件中的SQL语句。
     * 解析完成的SQL语句就有了名称和语句的映射关系，当操作数据库时，就可以通过映射关系获取到对应的SQL语句并执行操作。
     * @param list
     * @return
     */
    private Map<String, XNode> mapperElement(List<Element> list) {
        Map<String, XNode> map = new HashMap<>();
        for (Element e : list) {
            String resource = e.attributeValue("resource");
            try {
                Reader reader = Resources.getResourceAsReader(resource);
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new InputSource(reader));
                Element root = document.getRootElement();
                // 命名空间
                String namespace = root.attributeValue("namespace");

                // SELECT
                List<Element> selectNodes = root.selectNodes("select");
                for (Element selectNode : selectNodes) {
                    String id = selectNode.attributeValue("id");
                    String parameterType = selectNode.attributeValue("parameterType");
                    String resultType = selectNode.attributeValue("resultType");
                    String sqlText = selectNode.getText();

                    // "?" 匹配
                    Map<Integer,String> parameter = new HashMap<>();
                    Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                    Matcher matcher = pattern.matcher(sqlText);
                    for (int i = 1; matcher.find(); i++) {
                        String g1 = matcher.group(1);
                        String g2 = matcher.group(2);
                        parameter.put(i, g2);
                        sqlText = sqlText.replace(g1, "?");
                    }

                    XNode xNode = new XNode();
                    xNode.setSql(sqlText);
                    xNode.setNameSpace(namespace);
                    xNode.setParameter(parameter);
                    xNode.setParameterType(parameterType);
                    xNode.setResultType(resultType);
                    xNode.setId(id);
                    map.put(namespace + "." + id, xNode);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 获取数据源配置信息
     * @param list
     * @return
     */
    private Map<String, String> dataSource(List<Element> list) {
        Map<String, String> dataSource = new HashMap<>();
        for (Element e : list) {
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            dataSource.put(name, value);
        }
        return dataSource;
    }
}
