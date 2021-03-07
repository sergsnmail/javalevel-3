package com.sergsnmail.lesson7;

import com.sergsnmail.lesson7.testengine.TestEngine;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("By class");
        TestEngine.start(TestLesson7.class);

        System.out.println("\nBy class name");
        TestEngine.start("com.sergsnmail.lesson7.TestLesson7");
    }
}
