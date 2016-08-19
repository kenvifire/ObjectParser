package com.itluobo.objectparser.test;

import com.itluobo.objectparser.JsonObjectParser;
import org.junit.Assert;
import org.junit.Test;


public class JsonObjectParserTest {


    @Test
    public void test() {
        JsonObjectParser parser = new JsonObjectParser("{\n" +
                "\t\"page\":1,\n" +
                "\t\"data\":{\n" +
                "\t\t\"o\":{},\n" +
                "\t\t\"int\":1,\n" +
                "\t\t\"true\":true,\n" +
                "\t\t\"false\":false,\n" +
                "\t\t\"long\":100,\n" +
                "\t\t\"double\":100.2,\n" +
                "\t\t\"string\":\"string\"\n" +
                "\t}\n" +
                "}");
        Assert.assertEquals(Integer.valueOf(1), parser.getInt("data.int"));
        Assert.assertEquals(true,parser.getBoolean("data.true"));
        Assert.assertEquals(false,parser.getBoolean("data.false"));
        Assert.assertEquals(Long.valueOf(100),parser.getLong("data.long"));
        Assert.assertEquals(Double.valueOf(100.2),parser.getDouble("data.double"));
        Assert.assertEquals("string",parser.getString("data.string"));
    }


    @Test
    public void test_parse() {
        /*** test data **/
        /*
       {
            "data" : {
            	"o":{},
            	"int":1,
            	"true":true,
            	"false":false,
            	"long":100,
            	"double":100.2,
            	"string":"string",
            	"date":1463046784000,
            	"detail":{
            		"a": 11,
            		"b": "dB"
            	},
            	"strList" :[
            		"a","b"
            	],
            	"intList" : [
            		1,2,3
            	],
            	"longList" :[
            		2,3,4
            	],
            	"doubleList" :[
            	   1.1,2.2,3.3
            	],
            	"objList" : [
            	  {"a":1,"b":"bb","c":[1,2,3]},
            	  {"a":2,"b":"bb2"}

            	]
            }
        }
        *
        * */
        /***** test data end***/
        JsonObjectParser parser = new JsonObjectParser("{\n" +
                "            \"data\" : {\n" +
                "            \t\"o\":{},\n" +
                "            \t\"int\":1,\n" +
                "            \t\"true\":true,\n" +
                "            \t\"false\":false,\n" +
                "            \t\"long\":100,\n" +
                "            \t\"double\":100.2,\n" +
                "            \t\"string\":\"string\",\n" +
                "            \t\"date\":1463046784000,\n" +
                "            \t\"detail\":{\n" +
                "            \t\t\"a\": 11,\n" +
                "            \t\t\"b\": \"dB\"\n" +
                "            \t},\n" +
                "            \t\"strList\" :[\n" +
                "            \t\t\"a\",\"b\"\n" +
                "            \t],\n" +
                "            \t\"intList\" : [\n" +
                "            \t\t1,2,3\n" +
                "            \t],\n" +
                "            \t\"longList\" :[\n" +
                "            \t\t2,3,4\n" +
                "            \t],\n" +
                "            \t\"doubleList\" :[\n" +
                "            \t   1.1,2.2,3.3\n" +
                "            \t],\n" +
                "            \t\"objList\" : [\n" +
                "            \t  {\"a\":1,\"b\":\"bb\",\"c\":[1,2]},\n" +
                "            \t  {\"a\":2,\"b\":\"bb2\"}\n" +
                "\n" +
                "            \t]\n" +
                "            }\n" +
                "        }");
        TestObj obj = parser.buildFromJson(TestObj.class);
        Assert.assertEquals(Integer.valueOf(1), Integer.valueOf(obj.getI()));
        Assert.assertEquals(true, parser.getBoolean("data.true"));
        Assert.assertEquals(false,parser.getBoolean("data.false"));
        Assert.assertEquals(Long.valueOf(100),parser.getLong("data.long"));
        Assert.assertEquals(Double.valueOf(100.2),parser.getDouble("data.double"));
        Assert.assertEquals("string",parser.getString("data.string"));
        Assert.assertEquals(1463046784000l, parser.getDate("data.date").getTime());
        Assert.assertEquals(11,obj.getData().getDA());
        Assert.assertEquals("dB",obj.getData().getDB());
        Assert.assertEquals(Integer.valueOf(1), parser.getInt("data.intList[0]"));
        Assert.assertEquals(Integer.valueOf(2), parser.getInt("data.intList[1]"));
        Assert.assertEquals(Long.valueOf(3), parser.getLong("data.longList[1]"));
        Assert.assertEquals(Integer.valueOf(2), parser.getInt("data.objList[1].a"));
        Assert.assertEquals(Integer.valueOf(2), parser.getInt("data.objList[0].c[1]"));


    }


}
