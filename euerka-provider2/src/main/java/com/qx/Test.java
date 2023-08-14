package com.qx;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author qiux
 * @Date 2023/7/20
 * @since
 */
public class Test {

    public static void main(String[] args) {

        try {
            test3();
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
//            e.getCause()
//            System.out.println(e.getCause());
//            System.out.println(Arrays.stream(e.getCause()).collect(Collectors.toList()));
        }
    }

    private static void test3() {
        test2();
    }

    private static void test2() {
        try {
            test1();
        } catch (Exception e) {
            throw new BussinessException("异常2", e);
        }

    }


    private static void test1() {
        try {
            System.out.println("1");
            int i = 1 / 0;
        } catch (Exception e) {
            throw new BussinessException("异常1", e);
        }


    }

}
