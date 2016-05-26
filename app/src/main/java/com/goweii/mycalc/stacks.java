package com.goweii.mycalc;

import java.util.LinkedList;

public class Stacks {

    private LinkedList list = new LinkedList();
    int top = -1;

    public void push(Object value) {
        top++;
        list.addFirst(value);
    }

    public Object pop() {
        Object temp = list.getFirst();
        top--;
        list.removeFirst();
        return temp;
    }

    public Object top() {
        return list.getFirst();
    }

}
