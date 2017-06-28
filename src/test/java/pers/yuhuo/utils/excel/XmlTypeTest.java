package pers.yuhuo.utils.excel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pers.yuhuo.utils.excel.excel2xml.XmlType;

public class XmlTypeTest
{
    private XmlType classRelection = null;
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
        classRelection = new XmlType();
    }

    @After
    public void tearDown() throws Exception
    {
        classRelection = null;
    }

    @Test
    public void testConst()
    {
        Assert.assertTrue(classRelection instanceof XmlType);
    }

}
