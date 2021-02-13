package com.sergsnmail.lesson1.task3;

import org.junit.Assert;
import org.junit.Test;

public class BoxTest {

    @Test
    public void testCompareTo_False() {
        Box<Apple> appleBox = new Box<>();
        appleBox.add(new Apple(1.0f));
        appleBox.add(new Apple(1.5f));
        appleBox.add(new Apple(2.0f));
        appleBox.add(new Apple(2.5f));

        Box<Apple> appleBox2 = new Box<>();
        appleBox2.add(new Apple(0.5f));
        appleBox2.add(new Apple(1.5f));
        appleBox2.add(new Apple(2.0f));
        appleBox2.add(new Apple(2.5f));

        Assert.assertFalse(appleBox.compareTo(appleBox2));
    }

    @Test
    public void testCompareTo_True() {
        Box<Apple> appleBox = new Box<>();
        appleBox.add(new Apple(1.0f));
        appleBox.add(new Apple(1.5f));
        appleBox.add(new Apple(2.0f));
        appleBox.add(new Apple(2.5f));

        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange(1.0f));
        orangeBox.add(new Orange(1.5f));
        orangeBox.add(new Orange(2.0f));
        orangeBox.add(new Orange(2.5f));

        Assert.assertTrue(appleBox.compareTo(orangeBox));
    }

    @Test
    public void testMoveTo_ContentCopyToDest() {
        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange(1.0f));
        orangeBox.add(new Orange(1.5f));
        orangeBox.add(new Orange(2.0f));
        orangeBox.add(new Orange(2.5f));

        Object[] expected = orangeBox.getContent().toArray();

        Box<Orange> orangeBox2 = new Box<>();
        orangeBox.moveTo(orangeBox2);
        Object[] actual = orangeBox2.getContent().toArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testMoveTo_SourceContentClear() {
        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange(1.0f));
        orangeBox.add(new Orange(1.5f));
        orangeBox.add(new Orange(2.0f));
        orangeBox.add(new Orange(2.5f));

        Object[] expected = new Object[0];

        Box<Orange> orangeBox2 = new Box<>();
        orangeBox.moveTo(orangeBox2);
        Object[] actual = orangeBox.getContent().toArray();
        Assert.assertArrayEquals(expected, actual);
    }
}