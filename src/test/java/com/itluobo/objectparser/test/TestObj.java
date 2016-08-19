package com.itluobo.objectparser.test;

import com.itluobo.objectparser.ClassFieldPath;
import com.itluobo.objectparser.FieldPath;
import com.itluobo.objectparser.ListFieldPath;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TestObj{
    @FieldPath("data.int")
    private int i;

    @FieldPath("data.true")
    private boolean b_true;

    @FieldPath("data.false")
    private boolean b_false;

    @FieldPath("data.long")
    private long l;

    @FieldPath("data.double")
    private double d;

    @FieldPath("data.string")
    private String str;

    @FieldPath("data.date")
    private Date date;

    @ClassFieldPath("data.detail")
    private JsonDetail data;

    @ListFieldPath(value = "data.strList", clazz = String.class)
    private List<String> strList;

    @ListFieldPath(value = "data.intList", clazz = int.class)
    private List<Integer> intList;

    @ListFieldPath(value = "data.longList", clazz = long.class)
    private List<Long> longList;

    @ListFieldPath(value = "data.doubleList", clazz = double.class)
    private List<Double> doubleList;

    @ListFieldPath(value = "data.objList", clazz = TestListObj_dest.class)
    private List<TestListObj_dest>  objList;
}
