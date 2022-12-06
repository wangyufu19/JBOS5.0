package com.application.common.comparable;

public class Selection <T extends Comparable<T>> extends Sort<T> {

    public void sort(T[] nums) {
        int N = nums.length;
        for (int i = 0; i < N - 1; i++) {
            int min = i;
            for (int j = i + 1; j < N; j++) {
                if (less(nums[j], nums[min])) {
                    min = j;
                }
            }
            swap(nums, i, min);
        }
    }

    public static void main(String[] args){
        Integer[] arr={6,2,9};
        System.out.println("排序前");
        for (int i:arr){
            System.out.print(i);
        }
        System.out.println("");
        Selection selection=new Selection();
        selection.sort(arr);
        System.out.println("排序后");
        for (int i:arr){
            System.out.print(i);
        }
    }
}
