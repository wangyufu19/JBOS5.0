package com.application.samples.comparable;

public class Insertion<T extends Comparable<T>> extends Sort<T> {

    @Override
    public void sort(T[] nums) {
        int N = nums.length;
        for (int i = 1; i < N; i++) {
            for (int j = i; j > 0 && less(nums[j], nums[j - 1]); j--) {
                swap(nums, j, j - 1);
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
        Insertion insertion=new Insertion();
        insertion.sort(arr);
        System.out.println("排序后");
        for (int i:arr){
            System.out.print(i);
        }
    }
}