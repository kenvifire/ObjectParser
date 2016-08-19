package com.itluobo.objectparser.test;

import com.itluobo.objectparser.FieldPath;
import lombok.Data;

@Data
public class TestListObj_dest {
    @FieldPath("a")
    private int a_t;
    @FieldPath("b")
    private String b_t;
}
