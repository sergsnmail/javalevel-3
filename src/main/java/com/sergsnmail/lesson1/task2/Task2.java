package com.sergsnmail.lesson1.task2;

import java.util.ArrayList;
import java.util.Arrays;

public class Task2 {

    public static void main(String[] args) {
        ArrayList<String> stringArrayList = toArrayList(new String[]{"1st", "2nd", "3rd", "4th"});
        System.out.println(stringArrayList.toString());

        ArrayList<Integer> integerArrayList = toArrayList(new Integer[]{1, 2, 3, 4});
        System.out.println(integerArrayList.toString());
    }

    /**
     * 2. Написать метод, который преобразует массив в ArrayList;
     * @param arr Массив-источник элементов
     * @param <T> Ссылочный тип
     * @return ArrayList полученный из массива-источника
     */
    public static <T> ArrayList<T> toArrayList(T[] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }
}
