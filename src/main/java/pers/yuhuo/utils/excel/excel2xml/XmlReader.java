/**
 * 本类库是解决EXCEL与XML之间互相转换的问题
 */
package pers.yuhuo.utils.excel.excel2xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

/**
 * Xml读取器<br/>
 * 用于由Excel文件转换过来的数据,并转换成Excel格式的数据
 */
public class XmlReader
{
    /**
     * 需要转换的xml文件地址
     */
    private String _fileName = null;

    /**
     * work book
     */
    private HSSFWorkbook _wb       = null;

    /**
     * cell类型字典
     */
    private static HashMap<String, Integer> _typeMap  = null;

    /**
     * 默认构造函数
     */
    public XmlReader()
    {
    }

    /**
     * 构造函数
     * 
     * @param fileName
     *            要解析(转换为Excel)的xml文件
     */
    public XmlReader(String fileName)
    {
        setXmlFile(fileName);
    }

    /**
     * 设置转换成功之后的workbook对象
     * 
     * @param wb
     *            转换成功之后的workbook对象
     */
    private void setWorkBook(HSSFWorkbook wb)
    {
        _wb = wb;
    }

    /**
     * 获取转换成功之后的workbook对象
     * 
     * @return 转换成功之后的workbook对象
     */
    public HSSFWorkbook getWorkBook()
    {
        return _wb;
    }

    /**
     * 设置要转换的xml文件
     * 
     * @param fileName
     *            要转换的xml文件名
     */
    private void setXmlFile(String fileName)
    {
        _fileName = fileName;
    }

    /**
     * 获取要转换的xml文件
     * 
     * @return 要转换的xml文件
     */
    public String getXmlFile()
    {
        return _fileName;
    }

    /**
     * 转换字典
     * 
     * @return
     */
    protected static HashMap<String, Integer> getTypeMap()
    {
        if (null == _typeMap)
        {
            _typeMap = new HashMap<String, Integer>();
            // _typeMap.put("String",
            // Integer.valueOf(HSSFCell.CELL_TYPE_BLANK));
            _typeMap.put("Boolean", Integer.valueOf(HSSFCell.CELL_TYPE_BOOLEAN));
            // _typeMap.put("String",
            // Integer.valueOf(HSSFCell.CELL_TYPE_ERROR));
            // _typeMap.put("String",
            // Integer.valueOf(HSSFCell.CELL_TYPE_FORMULA));
            _typeMap.put("Number", Integer.valueOf(HSSFCell.CELL_TYPE_NUMERIC));
            _typeMap.put("String", Integer.valueOf(HSSFCell.CELL_TYPE_STRING));
        }

        return _typeMap;
    }

    /**
     * 根据xml中data数据中的type转换成int
     * 
     * @param type
     *            获取
     * @return
     */
    protected static int getType(String type)
    {
        int result = HSSFCell.CELL_TYPE_STRING;

        if ((null != type) && (getTypeMap().containsKey(type)))
        {
            result = getTypeMap().get(type).intValue();
        }

        return result;
    }

    /**
     * 获取单元格数据类型
     * 
     * @param cell
     *            单元格数据
     * @return 单元格数据类型
     */
    protected int getCellType(Element cell)
    {
        int result = HSSFCell.CELL_TYPE_STRING;

        if (null != cell)
        {
            Element data = cell.getChild("Data");
            if (null != data)
            {
                result = getType(data.getAttributeValue("type"));
            }
        }

        return result;
    }

    /**
     * 获取单元格的具体数据
     * 
     * @param cell
     *            单元格数据
     * @return
     */
    protected String getCellValue(Element cell)
    {
        String result = null;
        if (null != cell)
        {
            Element data = cell.getChild("Data");
            if (null != data)
            {
                result = data.getText();
            }
        }

        return result;
    }

    /**
     * 解析xml数据单元格
     * 
     * @param column
     *            单元格列号
     * @param cell
     *            单元格数据
     * @param row
     *            Excel的Row对象
     */
    protected void parseXml2Cell(int column,Element cell, HSSFRow row)
    {
        if ((null != cell) && (null != row))
        {
            HSSFCell wbCell = row.createCell(column, getCellType(cell));
            String value = getCellValue(cell);
            wbCell.setCellValue(value);
        }
    }

    /**
     * 解析xml到Excel的row对象
     * 
     * @param rownum
     *            行号
     * @param row
     *            行数据
     * @param sheet
     *            Excel的sheet对象
     */
    protected void parseXml2Row(int rownum, Element row, HSSFSheet sheet)
    {
        if ((null != row) && (null != sheet))
        {
            HSSFRow wbRow = sheet.createRow(rownum);
            wbRow.setHeight(Short.parseShort(row.getAttributeValue("Height")));
            List<Element> cells = row.getChildren();
            int len = cells.size();
            for (int i = 0; i < len; i++)
            {
                parseXml2Cell(i, cells.get(i), wbRow);
            }
        }
    }

    /**
     * 将xml的sheet数据转换成sheet对象
     * 
     * @param sheet
     *            sheet数据
     * @param wb
     *            WorkBook对象
     * @return HSSFSheet
     */
    protected void parseXml2WorkSheet(Element sheet, HSSFWorkbook wb)
    {
        if ((null != sheet) && (null != wb))
        {
            HSSFSheet result = wb.createSheet(sheet.getAttributeValue("name"));
            List<Element> rows = sheet.getChildren();
            int len = rows.size();
            for (int i = 0; i < len; i++)
            {
                parseXml2Row(i, rows.get(i), result);
            }
        }
    }

    /**
     * 解析实现方法
     * 
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void load() throws FileNotFoundException, JDOMException, IOException, IllegalStateException
    {
        setWorkBook(null);
        SAXBuilder sb = new SAXBuilder();
        FileInputStream fis = new FileInputStream(getXmlFile());
        try
        {
            Document doc = sb.build(fis);
            Element root = doc.getRootElement();
            HSSFWorkbook wb = new HSSFWorkbook();
            List<Element> sheets = root.getChildren();
            for (Element sheet :sheets)
            {
                parseXml2WorkSheet(sheet, wb);
            }

            setWorkBook(wb);
        }
        catch (JDOMException ex)
        {
            if (null != fis)
            {
                fis.close();
                fis = null;
            }
            throw ex;
        }
        catch (IOException ex)
        {
            if (null != fis)
            {
                fis.close();
                fis = null;
            }
            throw ex;
        }
        catch (IllegalStateException ex)
        {
            if (null != fis)
            {
                fis.close();
                fis = null;
            }
            throw ex;
        }

        if (null != fis)
        {
            fis.close();
            fis = null;
        }
    }

    /**
     * 解析实现方法
     * 
     * @param fileName
     *            要解析的xml文件名
     */
    public void load(String fileName) throws FileNotFoundException, JDOMException, IOException
    {
        setXmlFile(fileName);
        load();
    }

    /**
     * 要保存的Excel文件名
     * 
     * @param fileName
     *            Excel文件名
     * @return 成功返回true,失败返回false
     * @throws IOException
     */
    public boolean output(String fileName) throws IOException, FileNotFoundException
    {
        boolean result = false;

        if (getWorkBook() instanceof HSSFWorkbook)
        {
            FileOutputStream fos = new FileOutputStream(fileName);
            try
            {
                getWorkBook().write(fos);
            }
            catch (IOException ex)
            {
                if (null != fos)
                {
                    fos.close();
                    fos = null;
                }

                throw ex;
            }

            if (null != fos)
            {
                fos.close();
                fos = null;
            }

            result = true;
        }

        return result;
    }
}
