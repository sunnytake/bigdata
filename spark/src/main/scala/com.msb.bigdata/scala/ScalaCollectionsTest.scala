package com.msb.bigdata.scala

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
        arr1.foreach(println)
        
        // 2.链表
        // scala中数据及collections中有两个包：immutable和mutable。默认为不可变的immutable
        val list1 = List(1, 2, 3, 4, 5, 4, 3, 2, 1)
        for (elem <- list1) { println(elem) }
        list1.foreach(println)
        
        // 可变链表
        val list2 = new ListBuffer[Int]()
        list2.+=(33)
        list2.foreach(println)
        
        // 3.集合
        val set1: Set[Int] = Set(1, 2, 3, 4, 2, 1)
        for (elem <- set1) { println(elem) }
        set1.foreach(println)
        
        // 可变集合
        val set2: mutable.Set[Int] = mutable.Set(11, 22, 33, 44, 11)
        set2.add(88)
    
        // 4.元组
        val t1: (Int, String) = new Tuple2(11, "abc")
        val t2: (Int, String, String) = Tuple3(22, "abc", "s")
        val t3: (Int, Int, Int, Int) = (1, 2, 3, 4)
        println(t3._2)
    
        // tuple没有foreach方法，必须先拿到迭代器
        val tIter: Iterator[Any] = t3.productIterator
        while(tIter.hasNext){ println(tIter.next) }
        
        // 5.Map
        val map1: Map[String, Int] = Map(("a", 33), "b" -> 22)
        val keys: Iterable[String] = map1.keys
        println(map1.get("a").get)
        println(map1.get("w").getOrElse("空值"))
    
        val map2: mutable.Map[String, Int] = mutable.Map(("a", 11), ("b", 22))
        map2.put("c", 22)
    
        // 迭代器引入
        val listStr: Set[String] = Set("Hello World", "Hello msb", "Good idea")
        val flatMap = listStr.flatMap((x: String) => x.split(" "))
        flatMap.foreach(println)
        val mapList = flatMap.map((_, 1))
        mapList.foreach(println)
    
        // 以上代码的问题：内存扩大了N倍，每一步计算内存都保留有对象数据。是否可以解决数据计算中间状态占用内存的问题？
        // 迭代器：什么是迭代器？为什么会有迭代器模式？
        // 迭代器不存数据,存的是指针
        val iter: Iterator[String] = listStr.iterator
        val iterFlatMap = iter.flatMap((x: String) => x.split(" "))
        // 此处打印过了之后，后面就不会打印
        // iterFlatMap.foreach(println)
        val iterMapList = iterFlatMap.map((_, 1))
        iterMapList.foreach(println)
        
    }
}
