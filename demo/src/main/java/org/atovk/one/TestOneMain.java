package org.atovk.one;

public class TestOneMain {

    public static void one() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hello One World");
    }

}
