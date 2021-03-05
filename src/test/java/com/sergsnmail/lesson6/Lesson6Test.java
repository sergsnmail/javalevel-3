package com.sergsnmail.lesson6;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class Lesson6Test {

    private Lesson6 lesson6;

    @Before
    public void init() {
        lesson6 = new Lesson6();
    }

    @Test
    public void testReformatArray_FourInTheInside() {
        Assert.assertArrayEquals(new int[]{1, 7}, lesson6.getPartArray(new int[] {1, 2, 4, 4, 2, 3, 4, 1, 7}));
    }

    @Test
    public void testReformatArray_FourAtTheEnd() {
        Assert.assertArrayEquals(new int[0], lesson6.getPartArray(new int[] {1, 2, 4, 4, 2, 3, 4, 1, 4}));
    }

    @Test
    public void testReformatArray_FourAtTheBeginning() {
        Assert.assertArrayEquals(new int[] {2, 2, 3, 2, 3, 5, 1, 7}, lesson6.getPartArray(new int[] {4, 2, 2, 3, 2, 3, 5, 1, 7}));
    }

    @Test (expected = RuntimeException.class)
    public void testReformatArray_Exception() {
        lesson6.getPartArray(new int[] {1, 2, 2, 3, 1, 7});
    }

    @Test
    public void testCheckArray_TrueIfOnlyOneAndFour(){
        Assert.assertTrue(lesson6.checkArray(new int[]{1,4,1,4}));
    }

    @Test
    public void testCheckArray_FalseIfOnlyOne(){
        Assert.assertFalse(lesson6.checkArray(new int[]{1,1,1,1}));
    }

    @Test
    public void testCheckArray_FalseIfOnlyFour(){
        Assert.assertFalse(lesson6.checkArray(new int[]{4,4,4,4}));
    }

    @Test
    public void testCheckArray_FalseIfAbsentOneAndFour(){
        Assert.assertFalse(lesson6.checkArray(new int[]{2,3,5,6}));
    }
}