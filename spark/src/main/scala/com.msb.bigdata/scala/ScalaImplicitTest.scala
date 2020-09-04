package com.msb.bigdata.scala

import java.util

object ScalaImplicitTest {
    def main(args: Array[String]): Unit = {
        val list = new util.LinkedList[Int]()
        list.add(1)
        list.add(2)
        list.add(3)
        
        // 想让list也能有foreach方法
        // 方法1：封装一个foreach方法
        def foreach[T](list: util.LinkedList[T], f: (T) => Unit) = {
            val iter: util.Iterator[T] = list.iterator()
            while(iter.hasNext) f(iter.next)
        }
        foreach(list, println)
        
        // 方法2：封装一个类
        val xx = new XXX(list)
        xx.foreach(println)
        
        // 方法3：隐式转换方法，也可以用隐式转换类。增强了scala代码的可扩展性
        implicit def suibian[T](list: util.LinkedList[T]) = {
            new XXX(list)
        }
        // 效果同上
//        implicit class KKK[T](list: util.LinkedList[T]){
//            def foreach[T](f:(T) => Unit) = {
//                val iter: util.Iterator[T] = list.iterator()
//                while(iter.hasNext) f(iter.next)
//            }
//        }
        // 放生了什么事？
        // 1.scala编译器发现list.foreach(println)有bug
        // 2.去寻找有没有implicit定义的方法，且方法的参数正好是list的类型
        // 3.编译期：完成曾经手动val xx = new XXX(list); xx.foreach(println)，相当于编译器自动改写了代码
        list.foreach(println)
    }
}

class XXX[T](list: util.LinkedList[T]){
    def foreach(f:(T) => Unit) = {
        val iter: util.Iterator[T] = list.iterator()
        while(iter.hasNext) f(iter.next)
    }
}

// 1.12