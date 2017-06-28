package pers.yuhuo.utils.excel;

import java.io.FileOutputStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import pers.yuhuo.utils.Constant;

public class JdomTest
{
	
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        parserXml(Constant.XML_RESOURCES_PATH_PRE+"test.xml");
        createXml();
    }
    
    public static void parserXml(String fileName)
    {
        SAXBuilder builder = new SAXBuilder();
        try {  
            Document document = builder.build(fileName);
            XMLOutputter xop = new XMLOutputter();
            FileOutputStream fos = new FileOutputStream(Constant.OUT_RESOURCES_PATH_PRE+"test2.xml");
            xop.output(document, fos);   // 输出
            
            
            Element employees = document.getRootElement();
            List<Element> ems = employees.getChildren();
            int size = ems.size();
            for (int i = 0; i < size; i++)
            {
                List<Element> rows = ems.get(i).getChildren();

                int rowCount = rows.size();
                for (int j = 0; j < rowCount; j++)
                {
                    System.out.println("value==" + rows.get(j).getChild("cell").getChild("data").getText());
                }
            }
            System.out.println(ems.size());
        }
        catch (Exception ex)
        {
        	System.err.println(ex);
        }
    }

    public static void createXml()
    {
        try 
        {  
            Element root = new Element("iamroot");
            root.setAttribute("aaaa", "value11111");
            root.setAttribute("cccccc", "value2222");
            root.setText("bbbbb");
            Document document = new Document(root);
            XMLOutputter xop = new XMLOutputter();
            xop.output(document, new FileOutputStream(Constant.OUT_RESOURCES_PATH_PRE+"test3.xml"));
        }
        catch (Exception ex)
        {
        }
    }
        
}
