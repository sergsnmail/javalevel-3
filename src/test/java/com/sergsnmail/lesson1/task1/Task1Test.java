package com.sergsnmail.lesson1.task1;

import org.junit.Assert;
import org.junit.Test;

public class Task1Test {

    @Test
    public void testChangePos_Integer() {
        Integer[] intArray = new Integer[]{1, 2, 3, 4};
        Task1.changePos(intArray, 0, 3);
        Assert.assertArrayEquals(new Integer[]{4,2,3,1}, intArray);
    }

    @Test
    public void testChangePos_String() {
        String[] stringArray = new String[]{"1st", "2nd", "3d", "4th"};
        Task1.changePos(stringArray, 0, 3);
        Assert.assertArrayEquals(new String[]{"4th", "2nd", "3rd", "1st"}, stringArray);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testChangePos_IllegalArgumentException_SourceIsNull(){
        Task1.changePos(null, 0, 3 );
    }

    @Test (expected = IllegalArgumentException.class)
    public void testChangePos_IllegalArgumentException_IndexOutOfBounds_posFrom(){
        Task1.changePos(new Integer[]{1, 2, 3, 4}, 5, 0 );
    }

    @Test (expected = IllegalArgumentException.class)
    public void testChangePos_IllegalArgumentException_IndexOutOfBounds_posTo(){
        Task1.changePos(new Integer[]{1, 2, 3, 4}, 0, 5 );
    }
}