package com.itluobo.objectparser.test;


import com.itluobo.objectparser.FieldPath;
import lombok.Data;

@Data
public class JsonDetail {
    @FieldPath("a")
    private int dA;
    @FieldPath("b")
    private String dB;
}
