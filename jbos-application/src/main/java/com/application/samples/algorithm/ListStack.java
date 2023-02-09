package com.application.samples.algorithm;

import java.util.Iterator;

public class ListStack<Item> implements MyStack<Item> {

    private Node top = null;
    private int N = 0;


    private class Node {
        Item item;
        Node next;
    }


    @Override
    public MyStack<Item> push(Item item) {

        Node newTop = new Node();

        newTop.item = item;
        newTop.next = top;

        top = newTop;

        N++;

        return this;
    }


    @Override
    public Item pop() throws Exception {

        if (isEmpty()) {
            throw new Exception("stack is empty");
        }

        Item item = top.item;

        top = top.next;
        N--;

        return item;
    }


    @Override
    public boolean isEmpty() {
        return N == 0;
    }


    @Override
    public int size() {
        return N;
    }


    @Override
    public Iterator<Item> iterator() {

        return new Iterator<Item>() {

            private Node cur = top;


            @Override
            public boolean hasNext() {
                return cur != null;
            }


            @Override
            public Item next() {
                Item item = cur.item;
                cur = cur.next;
                return item;
            }
        };

    }
    public static void main(String[] args) throws Exception {
        Integer[] arr={5,4,3,2,1,0};


        ListStack listStack=new ListStack();
        for (int i:arr){
            listStack.push(i);
        }

        Iterator iterator=listStack.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("size="+listStack.size());
    }
}