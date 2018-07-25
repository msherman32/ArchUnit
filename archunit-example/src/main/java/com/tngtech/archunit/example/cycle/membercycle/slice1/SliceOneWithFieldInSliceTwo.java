package com.tngtech.archunit.example.cycle.membercycle.slice1;

import com.tngtech.archunit.example.cycle.membercycle.slice2.SliceTwoWithMethodParameterInSliceThree;

public class SliceOneWithFieldInSliceTwo {
    public SliceTwoWithMethodParameterInSliceThree classInSliceTwo;
}
