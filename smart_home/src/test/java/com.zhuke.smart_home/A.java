package com.zhuke.smart_home;

/**
 * Created by ZHUKE on 2016/3/16.
 */
public class A {
    int a;

    public A() {
        this.a = 10;
    }
}

class B {
    public static void main(String[] args) {
        A a = new A();
        System.out.println("A.a = " + a.a);
        new B().modifyA(a);
        System.out.println("A.a = " + a.a);
    }

    public void modifyA(A a) {
        a.a = 20;
    }
}
