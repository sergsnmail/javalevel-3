package com.sergsnmail.lesson4;

/**
 * 1. Создать три потока, каждый из которых выводит определенную букву (A, B и C) 5 раз (порядок – ABСABСABС).
 * Используйте wait/notify/notifyAll.
 */
public class ThreadLesson4 {

    private static volatile char curr = 'A';
    private static Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    synchronized (lock) {
                        while (curr != 'A') {
                            lock.wait();
                        }
                        System.out.print('A');
                        curr = 'B';
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    synchronized (lock) {
                        while (curr != 'B') {
                            lock.wait();
                        }
                        System.out.print('B');
                        curr = 'C';
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    synchronized (lock) {
                        while (curr != 'C') {
                            lock.wait();
                        }
                        System.out.print('C');
                        curr = 'A';
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println("main done");
    }
}
