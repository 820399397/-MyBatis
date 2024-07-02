package cn.xuguowen.mybatis.builder.xml;

import cn.xuguowen.mybatis.builder.BaseBuilder;
import cn.xuguowen.mybatis.datasource.DataSourceFactory;
import cn.xuguowen.mybatis.io.Resources;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.Environment;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.mapping.SqlCommandType;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
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
        super(new Configuration());
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
            // 解析环境信息
            environmentsElement(root.element("environments"));
            // 解析映射器
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return super.configuration;
    }

    private void environmentsElement(Element context) throws Exception {
        String defaultValue = context.attributeValue("default");
        List<Element> environmentList = context.elements("environment");
        for (Element environment : environmentList) {
            String id = environment.attributeValue("id");
            if (id.equals(defaultValue)) {
                // 事务管理器
                Element transactionManager = environment.element("transactionManager");
                String transactionManagerType = transactionManager.attributeValue("type");
                TransactionFactory transactionFactory = (TransactionFactory) super.configuration.getTypeAliasRegistry().resolveAlias(transactionManagerType).newInstance();
                // 数据源
                Element datasource = environment.element("dataSource");
                String dataSourceType = datasource.attributeValue("type");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) super.configuration.getTypeAliasRegistry().resolveAlias(dataSourceType).newInstance();
                List<Element> properties = datasource.elements("property");
                Properties props = new Properties();
                for (Element property : properties) {
                    props.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }
                // 反射的方式给数据源设置属性
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();
                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id).dataSource(dataSource).transactionFactory(transactionFactory);
                configuration.setEnvironment(environmentBuilder.build());
            }
        }
    }

    /*
     * <mappers>
     *	 <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/PostMapper.xml"/>
     * </mappers>
     */
    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            InputStream inputStream = Resources.getResourceAsStream(resource);

            // 在for循环里每个mapper都重新new一个XMLMapperBuilder，来解析
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource);
            mapperParser.parse();
        }
    }

}
