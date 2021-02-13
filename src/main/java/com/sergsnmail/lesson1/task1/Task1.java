package com.sergsnmail.lesson1.task1;

import java.util.Arrays;

public class Task1 {
    public static void main(String[] args) {
        Integer[] intArray = new Integer[]{1, 2, 3, 4};
        String[] stringArray = new String[]{"1st", "2nd", "3rd", "4th"};

        System.out.printf("Original: %s\n", Arrays.toString(intArray));
        changePos(intArray, 0,3);
        System.out.printf("Changed: %s\n", Arrays.toString(intArray));

        System.out.printf("Original: %s\n", Arrays.toString(stringArray));
        changePos(stringArray, 0,3);
        System.out.printf("Changed: %s\n", Arrays.toString(stringArray));
    }

    /**
     * 1. Написать метод, который меняет два элемента массива местами.(массив может быть любого ссылочного типа);
     *
     * @param source  Массив элементов
     * @param posFrom Позиция первого элемента
     * @param posTo   Позиция второго элемента
     * @param <T>     Ссылочный тип
     */
    public static <T> void changePos(T[] source, int posFrom, int posTo) {
        if (source == null || !(posFrom >= 0 && posFrom < source.length) || !(posTo >= 0 && posTo < source.length)) {
            throw new IllegalArgumentException();
        }
        T temp = source[posTo];
        source[posTo] = source[posFrom];
        source[posFrom] = temp;
    }
}
