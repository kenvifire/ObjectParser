package com.itluobo.objectparser.test;

import com.itluobo.objectparser.FieldPath;
import lombok.Data;

@Data
public class TestObjComplex_dst {
    @FieldPath("o_a")
    private int a;
    @FieldPath("o_b")
    private String b;
}
