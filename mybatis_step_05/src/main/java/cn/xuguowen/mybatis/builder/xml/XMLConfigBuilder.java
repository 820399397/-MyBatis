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
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();
                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id).dataSource(dataSource).transactionFactory(transactionFactory);
                configuration.setEnvironment(environmentBuilder.build());
            }
        }
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

                // 使用“？”匹配
                HashMap<Integer, String> parameterMap = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameterMap.put(i, g2);
                    sql = sql.replace(g1, "?");
                }
                String msId = namespace + "." + id;
                String nodeName = selectElement.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

                BoundSql boundSql = new BoundSql(sql, parameterMap, parameterType, resultType);

                MappedStatement mappedStatement = new MappedStatement.Builder(configuration,msId,sqlCommandType,boundSql).build();

                super.configuration.addMappedStatement(mappedStatement);
            }

            super.configuration.addMapper(Resources.classForName(namespace));

        }

    }
}
