package com.thread;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/8/27/027.
 */
public class ThreadService extends Thread{

    private String threadName;

    private Runnable task;

    private Thread target;

    public ThreadService(String threadName, Runnable task) {
        this.threadName = threadName;
        this.task = task;
    }

    @Override
    public void run() {
        target = new Thread(task, threadName);
        target.setDaemon(true);
        target.start();
        try {
            target.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutDown(){
        this.interrupt();
    }

    public static void main(String[] args) {
        Integer[] arr  = new Integer[2];
        List list = new ArrayList<>();
        list.add("11");
        Object o = null;
        Arrays.asList(arr).forEach(i-> System.out.println(i));
        Integer x=new Integer(666);
        System.out.println(x +"~~~~"+(o == null)+","+list.size());
        StringBuffer s = new StringBuffer("hhhh");
        add(arr, o, x, list, s);
        System.out.println(x +"~~~~"+(o == null)+","+list.size()+"---"+s);
        Optional.of("=====================").ifPresent(System.out::println);
        Arrays.asList(arr).forEach(i-> System.out.println(i));
    }

    public static void add(Integer[] iii, Object o, Integer x, List list, StringBuffer s){
        iii[0] = 1;
        o = new Object();
        x = 77777;
        list.add("555");
        s = s.append("nnn");
    }
}
