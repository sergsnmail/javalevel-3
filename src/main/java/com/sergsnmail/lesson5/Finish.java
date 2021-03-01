package com.sergsnmail.lesson5;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Finish extends Stage{
    private int position = 0;
    private Lock lock = new ReentrantLock(true);
    @Override
    public void go(Car c) {
        try {
            lock.lock();
            if (position == 0) {
                System.out.println(c.getName() + " - WIN");
            }
            this.position++;
        } finally {
            lock.unlock();
        }
    }
}
