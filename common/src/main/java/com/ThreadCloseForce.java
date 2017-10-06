package com;

/***************************************
 * @author:Alex Wang
 * @Date:2017/2/19 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class ThreadCloseForce {

    public static void main(String[] args) {

        ThreadService service = new ThreadService();
        long start = System.currentTimeMillis();

        StringBuffer context = new StringBuffer("222");

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(context);
            }
        }).start();

        service.execute(() -> {
            //load a very heavy resource.
            while (true) {
                System.out.println("run...." + context);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        service.shutdown(10000);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}