package com.sergsnmail.lesson7;

import com.sergsnmail.lesson7.testengine.AfterSuite;
import com.sergsnmail.lesson7.testengine.BeforeSuite;
import com.sergsnmail.lesson7.testengine.Test;

public class TestLesson7 {

    private Echo echo;

    @BeforeSuite
    public void before(){
        echo = new Echo();
        echo.echo("before");
    }

    @AfterSuite
    public void after(){
        echo.echo("after");
    }

    @Test(priority = 3)
    public void test1(){
        echo.echo("test1");
    }

    @Test(priority = 2)
    public void test2(){
        echo.echo("test2");
    }

    @Test(priority = 1)
    public void test3(){
        echo.echo("test3");
    }

    @Test(priority = 1)
    public void test4(){
        echo.echo("test4");
    }
}
