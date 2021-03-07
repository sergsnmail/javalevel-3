package com.sergsnmail.lesson7.testengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TestEngine {

    private Class<?> testClass;
    private Method beforeSuite;
    private Method afterSuite;
    private Map<Integer, Set<Method>> tests = new TreeMap();

    private TestEngine() {
    }

    public static void start(Class<?> testClass) {
        if (testClass == null) {
            throw new IllegalArgumentException("Argument \'testClass\' can not be null");
        }
        (new TestEngine()).prepareTesting(testClass).beginTest();
    }

    public static void start(String className) throws ClassNotFoundException {
        TestEngine.start(Class.forName(className));
    }

    private TestEngine prepareTesting(Class<?> testClass) {
        this.testClass = testClass;
        Method[] methods = testClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                addTest(method.getAnnotation(Test.class).priority(), method);
            }
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuite != null) {
                    throw new RuntimeException();
                }
                beforeSuite = method;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuite != null) {
                    throw new RuntimeException();
                }
                afterSuite = method;
            }
        }
        return this;
    }

    private void beginTest() {
        try {
            Object instance = testClass.newInstance();
            if (beforeSuite != null) {
                invokeMethod(instance, beforeSuite);
            }
            if (tests.size() > 0) {
                tests.forEach((key, val) -> val.forEach((method) -> invokeMethod(instance, method)));
            }
            if (afterSuite != null) {
                invokeMethod(instance, afterSuite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTest(int priority, Method test) {
        if (test == null) {
            throw new IllegalArgumentException("Argument \'test\' can not be null");
        }
        Set<Method> samePriorityTests = tests.getOrDefault(priority, new HashSet<>());
        samePriorityTests.add(test);
        tests.put(priority, samePriorityTests);
    }

    private void invokeMethod(Object instance, Method method) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}