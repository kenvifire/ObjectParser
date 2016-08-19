package com.itluobo.objectparser.test;


import com.itluobo.objectparser.ClassFieldPath;
import com.itluobo.objectparser.FieldPath;
import lombok.Data;

@Data
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
