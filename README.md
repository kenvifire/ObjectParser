[![Build Status](https://travis-ci.org/kenvifire/ObjectParser.svg?branch=master)](https://travis-ci.org/kenvifire/ObjectParser)

## Java/Json Object Parser

Java/Json object parser is a tiny tool for parsing elements from complex java/json object.

## Usage

### Annotation

By annotations, you can build a java object from another java/json object.

For example, if you have an object origin, which is a combination of multiple complex objects, and you want to
retrieve some fields from this object and construct another object, you can use annotation to accomplish this.

<pre><code>
public class TestObj_1 {
    private int a;
    private Long b;
    private TestObject_In in;
    private TestObjComplex_origin data;

}

public class TestObject_In {
   private int c;
}

public class TestObjComplex_origin {
    private int o_a;
    private String o_b;
}

//dest class
public class TestObject_dst {
    @FieldPath("a")
    private int dA;
    @FieldPath("b")
    private long dB;
    @FieldPath("in.c")
    private int dC;
    @ClassFieldPath("data")
    private TestObjComplex_dst data;
}

public class TestObjComplex_dst {
    @FieldPath("o_a")
    private int a;
    @FieldPath("o_b")
    private String b;
}
</code></pre>

From above code, you can see that `TestObj_1` is a combination of `TestObject_In` and `TestObjComplex_origin` and 
two primitive fields.

And you want to do the following mappings:

- TestObject_dst.dA <--> TestObj_1.a
- TestObject_dst.dB <--> TestObj_1.b
- TestObject_dst.dC <--> TestObj_1.in.c

and then map `TestObject_dst.data` and `TestObj_1.data` with following mappings:

- TestObjComplex_dst.a <-->  TestObjComplex_origin.o_a
- TestObjComplex_dst.b <-->  TestObjComplex_origin.o_b

For primitive fields with same type, just use the `FieldPath` annotation, and set it's value to the field path.

For compound fields you need to use `ClassFieldPath` annotation, and set it's value to the filed path.

For list fields you should use `ListFieldPath`, and you should add element type as another param.

If you want to retrieve an element in list by it's index, just add`[index]` to the field path.

A field path is the path for looking for the filed you want in the dest class.


### Java API

Java API is useful when you want to get a few fields by field paths.

For example, you want to get the value of the second element of the first element in data's objList field, 
just use `JsonObjectParser parse = new JsonObjectParser(json)`
to construct a parser, then get the value by `parser.getInt("data.objList[0].c[1]")`. Very easy!


<pre><code>
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
</code></pre>




### Parsers

- JsonObjectParser: used to parse json object
- ObjectParser: used to parse java object



Have fun!



