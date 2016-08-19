package com.itluobo.objectparser.test;

import com.itluobo.objectparser.ObjectParser;
import org.junit.Assert;
import org.junit.Test;

public class ObjectParserTest {

    @Test
    public void test_parse() {
        TestObj_1 t1 = new TestObj_1();
        t1.setA(1);
        t1.setB(2l);

        TestObject_In in = new TestObject_In();
        in.setC(3);

        t1.setIn(in);

        TestObjComplex_origin complex = new TestObjComplex_origin();
        complex.setO_a(3);
        complex.setO_b("B");
        t1.setData(complex);



        ObjectParser objectParser = new ObjectParser(t1);

        TestObject_dst dst = objectParser.buildObject(TestObject_dst.class);
        Assert.assertEquals(1, dst.getDA());
        Assert.assertEquals(2l, dst.getDB());
        Assert.assertEquals(3, dst.getDC());
        Assert.assertEquals(3, dst.getData().getA());
        Assert.assertEquals("B", dst.getData().getB());
    }
}
