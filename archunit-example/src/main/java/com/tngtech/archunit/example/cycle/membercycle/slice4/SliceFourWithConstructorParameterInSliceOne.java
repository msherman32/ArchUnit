package com.tngtech.archunit.example.cycle.membercycle.slice4;

import com.tngtech.archunit.example.cycle.membercycle.slice1.SliceOneWithFieldInSliceTwo;

public class SliceFourWithConstructorParameterInSliceOne {

    public SliceFourWithConstructorParameterInSliceOne(SliceOneWithFieldInSliceTwo fieldInSliceTwo) {
    }
}
