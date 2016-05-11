package Consts;


import Services.PersistenceService;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class AgentConfig {

    static final String CONFIG_FILE_PATH= ".\\src\\main\\resources\\config.xml";

    private AgentConfig() {
        Init();
    }

    public static AgentConfig newInstance(){
        if(_instance == null){
            _instance =new AgentConfig();
        }
        return _instance;
    }

    static AgentConfig _instance;

    static String _connectionString;
    static String _kafkaIp;
    static String _kafkaPort;

    public String GetKafkaIp(){
        return _kafkaIp;
    }
    public String GetKafkaPort(){
        return _kafkaPort;
    }

    public String GetConnectionString(){
        return _connectionString;
    }

    private void Init() {

        try {

            Path path = FileSystems.getDefault().getPath(PersistenceService.GetCurrentDirPath() , CONFIG_FILE_PATH);
            InputStream targetStream = Files.newInputStream(path);
            XPath xpath = XPathFactory.newInstance().newXPath();
            Document document;
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(targetStream);
            _connectionString= (String) xpath.compile("//config//azure//connectionString").evaluate(document, XPathConstants.STRING);

            _kafkaIp = (String) xpath.compile("//config//kafka//ip").evaluate(document, XPathConstants.STRING);
            _kafkaPort = (String) xpath.compile("//config//kafka//port").evaluate(document, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
