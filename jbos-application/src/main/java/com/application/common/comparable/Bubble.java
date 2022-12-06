package com.application.common.comparable;

public class Bubble<T extends Comparable<T>> extends Sort<T> {

    @Override
    public void sort(T[] nums) {
        int N = nums.length;
        for (int i = 0; i<N ;i++) {
            for (int j = 0; j < N-1; j++) {
                if (less(nums[j + 1], nums[j])) {
                    swap(nums, j, j + 1);
                }
            }
        }
    }
    public static void main(String[] args){
        Integer[] arr={5,4,3,2,1};
        System.out.println("排序前");
        for (int i:arr){
            System.out.print(i);
        }
        System.out.println("");
        Bubble bubble=new Bubble();
        bubble.sort(arr);
        System.out.println("排序后");
        for (int i:arr){
            System.out.print(i);
        }
    }
}