package com.sergsnmail.lesson1.task3;

public class Task3 {
    public static void main(String[] args) {

        /**
         * Создаем коробки с фруктами
         */
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

        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange(1.0f));
        orangeBox.add(new Orange(1.5f));
        orangeBox.add(new Orange(2.0f));
        orangeBox.add(new Orange(2.5f));

        /**
         * Сравниваем веса коробок
         */
        System.out.println(appleBox.compareTo(orangeBox));
        System.out.println(appleBox2.compareTo(orangeBox));

        /**
         * Пересыпаем фрукты в новую коробку
         */
        Box<Orange> orangeBox2 = new Box<>();
        System.out.printf("(Before moving) orangeBox quantity: %s\n",orangeBox.getQuantity());
        System.out.printf("(Before moving) orangeBox2 quantity: %s\n",orangeBox2.getQuantity());
        orangeBox.moveTo(orangeBox2);
        System.out.printf("(After moving) orangeBox quantity: %s\n",orangeBox.getQuantity());
        System.out.printf("(After moving) orangeBox quantity: %s\n",orangeBox2.getQuantity());
    }
}
