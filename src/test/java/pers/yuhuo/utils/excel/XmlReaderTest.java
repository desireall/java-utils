package pers.yuhuo.utils.excel;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pers.yuhuo.utils.Constant;
import pers.yuhuo.utils.excel.excel2xml.XmlReader;

public class XmlReaderTest
{
    private final String fileName = Constant.XML_RESOURCES_PATH_PRE + "test.xml";
    private final String xlsFileName = Constant.EXCEL_RESOURCES_PATH_PRE + "test.xls";
    private XmlReader classRelection;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    @Before
    public void setUp() throws Exception
    {
        classRelection = new XmlReader();
    }

    @After
    public void tearDown() throws Exception
    {
        classRelection = null;
    }

    @Test
    public void testGetXmlFileReturnString()
    {
        // it will be null,when not filename be specified.
        Assert.assertNull(classRelection.getXmlFile());

        classRelection = new XmlReader(fileName);
        Assert.assertSame(fileName, classRelection.getXmlFile());
    }

    @Test
    public void testLoad() throws FileNotFoundException, JDOMException, IOException
    {
        Assert.assertNull(classRelection.getWorkBook());
        classRelection.load(fileName);
        Assert.assertTrue(classRelection.getWorkBook() instanceof HSSFWorkbook);
    }

    @Test
    public void testOutputReturnBoolean() throws FileNotFoundException, JDOMException, IOException
    {
        Assert.assertFalse(classRelection.output(xlsFileName));

        //xml <--> excel 转换
        classRelection.load(fileName);
        Assert.assertTrue(classRelection.output( Constant.OUT_RESOURCES_PATH_PRE+"test.xls"));
    }
}
