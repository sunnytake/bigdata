package com.msb.bigdata

import java.util

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object ScalaCollectionsTest {
    def main(args: Array[String]): Unit = {
    
        // 可以用java的collections
        val listJava  = new util.LinkedList[String]()
        listJava.add("hello")
    
        // scala也有自己的collections
        // 1.数组：Java中泛型是<>，scala中是[]，所以数组下标用(i)
        val arr1 = Array(1, 2, 3, 4)
        arr1(1) = 99
        println(arr1(1))
    
        for (elem <- arr1) { println(elem) }
        // 遍历元素，需要函数接收元素
        arr1.foreach(_)
        
        // 2.链表
        // scala中数据及collections中有两个包：immutable和mutable。默认为不可变的immutable
        val list1 = List(1, 2, 3, 4, 5, 4, 3, 2, 1)
        for (elem <- list1) { println(elem) }
        list1.foreach(_)
        
        // 可变链表
        val list2 = new ListBuffer[Int]()
        list2.+=(33)
        list2.foreach(_)
        
        // 3.集合
        val set1: Set[Int] = Set(1, 2, 3, 4, 2, 1)
        for (elem <- set1) { println(elem) }
        set1.foreach(_)
        
        // 可变集合
        val set2: mutable.Set[Int] = mutable.Set(11, 22, 33, 44, 11)
        set2.add(88)
    
        // 4.元祖
        val t1: (Int, String) = new Tuple2(11, "abc")
        val t2: (Int, String, String) = Tuple3(22, "abc", "s")
        val t3: (Int, Int, Int, Int) = (1, 2, 3, 4)
        println(t3._2)
        
    }
}
