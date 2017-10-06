package com;

/**
 * Created by Administrator on 2017/9/2/002.
 */
public class TestExtends {

    public static void main(String[] args) {
        //Father f = new Son();
        Son s = new Son();
        //f.FMethod();  // 断点所在位置
        s.FMethod();
    }
}

class Father{
    protected int k = 0;
    private int p = 1;
    private final int q = 100;
    public void FMethod(){}
}

class Son extends Father {
}
