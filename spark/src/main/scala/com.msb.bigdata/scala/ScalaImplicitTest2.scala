package com.msb.bigdata.scala

import java.util

object ScalaImplicitTest2 {
    def main(args: Array[String]): Unit = {
        val list = new util.LinkedList[Int]()
        list.add(1)
        list.add(2)
        list.add(3)
        val list2 = new util.ArrayList[Int]()
        list2.add(1)
        list2.add(2)
        list2.add(3)
        
       
        implicit def suibian[T](list: util.LinkedList[T]) = {
            val iter = list.iterator()
            new XXX2(iter)
        }
        list.foreach(println)
        implicit def suibian2[T](list: util.ArrayList[T]) = {
            val iter = list.iterator()
            new XXX2(iter)
        }
        list2.foreach(println)
    }
}

class XXX2[T](iter: util.Iterator[T]){
    def foreach(f:(T) => Unit) = {
        while(iter.hasNext) f(iter.next)
    }
}
