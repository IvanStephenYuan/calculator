package com.stephen.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @author IvanStephen
 * @project calculator
 * @date 2019/10/29 15:47
 **/
public class CalculatorUtil {
    public static void main(String[] args) throws Exception {
        //BlockingQueue<Integer> queue = new SynchronousQueue<>();
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        System.out.print(queue.offer(1) + " ");
        System.out.print(queue.offer(2) + " ");
        System.out.print(queue.offer(3) + " ");

        System.out.print(queue.take() + " ");
        System.out.println(queue.size());

        String str1 = "通话";
        String str2 = "重地";
        System. out. println(String. format("str1：%d | str2：%d",  str1. hashCode(),str2. hashCode()));
        System. out. println(str1. equals(str2));
    }
}
