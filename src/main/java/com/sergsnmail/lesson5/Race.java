package com.sergsnmail.lesson5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Race {

    private final CountDownLatch cdlStart = new CountDownLatch(1);
    private final CountDownLatch cdlPrepare;
    private final CountDownLatch cdlFinish;

    private ArrayList<Stage> stages;

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public Race(int carsCount, Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
        this.cdlPrepare = new CountDownLatch(carsCount);
        this.cdlFinish = new CountDownLatch(carsCount);
    }

    public void ready() {
        cdlPrepare.countDown();
    }

    public void start() {
        cdlStart.countDown();
    }

    public void finish() {
        cdlFinish.countDown();
    }

    public void waitParticipants() throws InterruptedException {
        cdlPrepare.await();
    }

    public void waitStart() throws InterruptedException {
        cdlStart.await();
    }

    public void waitFinish() throws InterruptedException {
        cdlFinish.await();
    }
}
