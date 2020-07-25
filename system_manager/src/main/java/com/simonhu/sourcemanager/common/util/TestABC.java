package com.simonhu.sourcemanager.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjun
 * @Description: TODO
 * @date 2018/1/25
 */
public class TestABC {
    public static void main(String[] args) {
        Integer a = Integer.valueOf("111");
        Integer b = Integer.valueOf("123");
        Integer c = Integer.valueOf("133");
        Integer d = Integer.valueOf("143");
        List list = new ArrayList();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);

        Integer aaa = Integer.valueOf("133");

        System.out.println(list.contains(aaa));
    }
}
