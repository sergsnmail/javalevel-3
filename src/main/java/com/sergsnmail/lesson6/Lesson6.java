package com.sergsnmail.lesson6;

import java.util.Arrays;

public class Lesson6 {

    /**
     * 2. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
     * Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов,
     * идущих после последней четверки. Входной массив должен содержать хотя бы одну четверку, иначе в методе
     * необходимо выбросить RuntimeException. Написать набор тестов для этого метода (по 3-4 варианта входных данных).
     * Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
     *
     * @return
     */
    public int[] getPartArray(int[] arr) {
        if (arr == null) {
            throw new RuntimeException();
        }
        if (Arrays.stream(arr).noneMatch(value -> value == 4)) {
            throw new RuntimeException();
        }
        int idx;
        for (idx = arr.length - 1; arr[idx] != 4; idx--) {}
        return Arrays.copyOfRange(arr, idx + 1, arr.length);
    }

    /**
     * 3. Написать метод, который проверяет состав массива из чисел 1 и 4. Если в нем нет хоть одной четверки
     * или единицы, то метод вернет false; Написать набор тестов для этого метода (по 3-4 варианта входных данных).
     */
    public boolean checkArray(int[] arr) {
        if (Arrays.stream(arr).noneMatch(value -> value == 4)) {
            return false;
        }
        if (Arrays.stream(arr).noneMatch(value -> value == 1)) {
            return false;
        }
        return true;
    }

}
