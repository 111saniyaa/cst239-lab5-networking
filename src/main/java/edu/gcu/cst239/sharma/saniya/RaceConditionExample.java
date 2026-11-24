package edu.gcu.cst239.sharma.saniya;

import org.w3c.dom.css.Counter;

public class RaceConditionExample {
    
    static class Counter {
        int value;
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Runnable increment = () -> {
            for (int i = 0; i < 100_000; i++) counter.value++;
        };
        Runnable decrement = () -> {
            for (int i = 0; i < 100_000; i++) counter.value--;
        };

        Thread first = new Thread(increment);
        Thread second = new Thread(decrement);
        first.start();
        second.start();
        first.join();
        second.join();

        System.out.println("Final value = " + counter.value);
    }
}


