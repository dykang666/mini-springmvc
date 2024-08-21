package com.example.minispringmvc.framework.servlet;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * @author kangdongyang
 * @version 1.0
 * @description:
 * @date 2024/8/13 11:47
 */
public class Main {
    public static void main(String[] args) {
        List<GarbageCollectorMXBean> list = ManagementFactory.getGarbageCollectorMXBeans();
        list.stream().forEach(s-> System.out.println(s.getName()));
    }
}
