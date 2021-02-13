package com.sergsnmail.lesson1.task2;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class Task2Test {

    @Test
    public void testToArrayList_isArrayList() {
        Assert.assertEquals(ArrayList.class, Task2.toArrayList(new Integer[]{1, 2, 3, 4}).getClass());
    }

    @Test
    public void testToArrayList_ArrayListHasGotSameSourceArrayValues() {
        Integer[] actual = new Integer[]{1, 2, 3, 4};
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(actual));
        Assert.assertEquals(expected, Task2.toArrayList(actual));
    }
}