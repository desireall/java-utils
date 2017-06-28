/**
 * 本类库是解决EXCEL与XML之间互相转换的问题
 * 开发此类库是为了解决EXCEL在subversion等版本管理软件中合并版本时的问题，因EXCEL是二进制文件，
 * 因此在合并项目时无法自动合并，因此建立中间件将excel转换成明文的文本模式，从而利于管理和维护。
 */
package pers.yuhuo.utils.excel.excel2xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Excel读取器<br/>
 * 用于读取Excel文件中的数据,并转换成xml格式的数据
 */
public class ExcelReader
{
    /**
     * 需要处理的excel文件名
     */
    private String               _fileName   = null;

    private static DecimalFormat format      = null;
    /**
     * 转换成xml的Element
     */
    private Element              _xmlElement = null;

    /**
     * 默认构造方法
     */
    public ExcelReader()
    {
    }

    /**
     * 构造方法
     * 
     * @param fileName
     *            需要处理的Excel路径(相对路径或绝对路径)
     */
    public ExcelReader(String fileName)
    {
        setFileName(fileName);
    }

    /**
     * 设置文件名
     * 
     * @param filename
     *            需要处理的Excel路径(相对路径或绝对路径)
     */
    private void setFileName(String filename)
    {
        _fileName = filename;
    }

    /**
     * 获取需要处理的Excel路径(相对路径或绝对路径)
     * 
     * @return 需要处理的Excel路径
     */
    public String getFileName()
    {
        return _fileName;
    }

    /**
     * 获取将excel文件转换好之后的xml数据
     * 
     * @return 转换好只好的xml数据
     */
    public Element getXml()
    {
        return _xmlElement;
    }

    /**
     * 将整个Excel文件转换的实现方法
     * 
     * @param wb
     *            excel文件操作对象
     * @return 转换之后的xml数据
     */
    protected Element parseWorkBook2Xml(HSSFWorkbook wb)
    {
        Element result = new Element(XmlType.WorkBook);

        if (wb instanceof HSSFWorkbook)
        {
            int as_num = wb.getNumberOfSheets();
            for (int i = 0; i < as_num; i++)
            {
                Element sheet = parseSheet2Xml(wb.getSheetAt(i));
                if (null != sheet)
                {
                    result.addContent(sheet);
                }
            }
        }

        return result;
    }

    /**
     * 将Excel文件的工作表(sheet)转换的实现方法
     * 
     * @param sheet
     *            Excel文件的工作表(sheet)
     * @return 转换之后的xml数据
     */
    public Element parseSheet2Xml(HSSFSheet sheet)
    {
        Element result = null;

        if (sheet instanceof HSSFSheet)
        {
            result = new Element(XmlType.WorkSheet);
            result.setAttribute("name", sheet.getSheetName());
            int rowCount = sheet.getLastRowNum();
            for (int i = 0; i < rowCount; i++)
            {
                Element row = parseRow2Xml(sheet.getRow(i));
                if (null != row)
                {
                    result.addContent(row);
                }
            }
        }

        return result;
    }

    /**
     * 将Excel文件的工作表(sheet)中的某行(row)转换的实现方法
     * 
     * @param row
     *            Excel文件的工作表(sheet)中的某行(row)
     * @return 转换之后的xml数据
     */
    public Element parseRow2Xml(HSSFRow row)
    {
        Element result = null;
        if (row instanceof HSSFRow)
        {
            result = new Element(XmlType.Row);
            result.setAttribute("Height", Short.toString(row.getHeight()));

            int cells = row.getLastCellNum();
            for (int i = 0; i < cells; i++)
            {
                Element cell = parseCell2Xml(row.getCell(i));
                if (null != cell)
                {
                    result.addContent(cell);
                }
            }
        }

        return result;
    }

    /**
     * 将Excel文件的工作表(sheet)中的某行(row)中的某个单元格(cell)转换的实现方法
     * 
     * @param cell
     *            Excel文件的工作表(sheet)中的某行(row)中的某个单元格(cell)
     * @return 转换之后的xml数据
     */
    public Element parseCell2Xml(HSSFCell cell)
    {
        Element result = null;
        if (cell instanceof HSSFCell)
        {
            result = new Element(XmlType.Cell);
            Element value = getCellValue(cell);
            if (null != value)
            {
                result.addContent(value);
            }
        }

        return result;
    }

    /**
     * 设置转换之后的xml数据
     * 
     * @param xml
     *            xml数据
     */
    private void setXmlElement(Element xml)
    {
        _xmlElement = xml;
    }

    /**
     * 解析Excel文件，并转换成xml数据 <br/>
     * 如果文件不存在或无法打开Excel文件则抛出异常
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void load() throws FileNotFoundException, IOException
    {
        setXmlElement(null);
        FileInputStream fis = new FileInputStream(getFileName());

        try
        {
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            setXmlElement(parseWorkBook2Xml(new HSSFWorkbook(fs)));
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
        
        if (null != fis)
        {
            fis.close();
            fis = null;
        }
    }
    
    /**
     * 解析Excel文件，并转换成xml数据 <br/>
     * 如果文件不存在或无法打开Excel文件则抛出异常
     * 
     * @param fileName
     *            需要解析的excel文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void load(String fileName) throws FileNotFoundException, IOException
    {
        setFileName(fileName);
        load();
    }

    /**
     * 数值格式化器
     * 
     * @return
     */
    private static DecimalFormat getFormater()
    {
        if (null == format)
        {
            format = new DecimalFormat();
            format.setDecimalSeparatorAlwaysShown(false);
            format.setGroupingUsed(false);
        }

        return format;
    }

    /**
     * 获取Excel中单元格(cell)数据
     * 
     * @param cell
     *            Excel中的单元格
     * @return Excel中单元格(cell)数据
     */
    public Element getCellValue(HSSFCell cell)
    {
        Element result = null;
        if (cell instanceof HSSFCell)
        {
            result = new Element(XmlType.Data);

            switch (cell.getCellType())
            {
                case HSSFCell.CELL_TYPE_NUMERIC:
                    result.setAttribute("type", "Number");
                    result.setText(getFormater().format(cell.getNumericCellValue()));
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    result.setAttribute("type", "Boolean");
                    result.setText(cell.toString());
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                default:
                    result.setAttribute("type", "String");
                    result.setText(cell.toString());
            }
        }

        return result;
    }

    public boolean output(String fileName) throws FileNotFoundException, IOException
    {
        boolean result = false;

        if (getXml() instanceof Element)
        {
            Document document = new Document(getXml());
            XMLOutputter xop = new XMLOutputter(getFormat());
            xop.output(document, new FileOutputStream(fileName));
            result = true;
        }

        return result;
    }

    private Format getFormat()
    {
        Format result = Format.getCompactFormat();
        result.setEncoding("utf-8");
        result.setIndent("    ");

        return result;
    }
}
