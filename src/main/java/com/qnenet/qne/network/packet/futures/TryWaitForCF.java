package com.qnenet.qne.network.packet.futures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TryWaitForCF {

    private void run() throws Exception {

        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            workers.add(new Worker("Worker=" + i));
        }

        List<CompletableFuture<Worker>> waitList = new ArrayList<>();
        workers.forEach(worker -> waitList.add(worker.workerDoStuff()));

        long endTime = System.currentTimeMillis() + 2000;
        for (CompletableFuture<Worker> workerCompleatableFuture : waitList) {
            long timeout = endTime - System.currentTimeMillis();
            if (timeout < 0) {
                timeout = 0;
            }
            System.out.println("Get result waiting at most: " + timeout + " ms");
            try {
                Worker workerDoStuffResult = workerCompleatableFuture.get(timeout, TimeUnit.MILLISECONDS);
                System.out.println(workerDoStuffResult.name + ": finished");
            } catch (Exception e) {
                System.out.println("Failed to get result: " + e.getMessage());
            }
        }

    }


    static class Worker {
        private String name;
        private long timeToComplete;

        public Worker(String name) {
            this.name = name;
            this.timeToComplete = (long) (Math.random() * (3000 - 1000 + 1) + 1000);
            System.out.println(name + ": timeToComplete=" + timeToComplete);
        }

        public CompletableFuture<Worker> workerDoStuff() {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(timeToComplete);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return this;
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new TryWaitForCF().run();
    }

}