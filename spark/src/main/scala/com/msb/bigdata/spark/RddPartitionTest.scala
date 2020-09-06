package com.msb.bigdata.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object RddPartitionTest {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local").setAppName("partition test")
        val sc: SparkContext = new SparkContext(conf)
        sc.setLogLevel("ERROR")
    
        val dataRDD: RDD[Int] = sc.parallelize(1 to 10, 2)
        // 外关联，sql查询
        // 每个元素都造成了连接关闭
        val res1: RDD[String] = dataRDD.map(
            (value: Int) => {
                println("-------- connect mysql --------")
                println(s"-------- select $value --------")
                println("-------- close mysql --------")
                value + "selected"
            }
        )
        res1.foreach(println)
        // 可以对分区元素进行操作 => 每个分区的数据处理只需开关一次数据库连接
        val res2: RDD[String] = dataRDD.mapPartitionsWithIndex(
            (pindex, piter) => {
                // spark是一个pipeline，迭代器嵌套的模式，数据不会在内存挤压
                val lb = new ListBuffer[String] // 致命的，可能撑爆内存
                println(s"---$pindex----- connect mysql --------")
                while (piter.hasNext) {
                    val value: Int = piter.next
                    println(s"---$pindex----- select $value --------")
                    lb.+=(value + "selected")
                }
                println(s"---$pindex----- close mysql --------")
                lb.iterator
            }
        )
        res2.foreach(println)
        // 最优方法 => 迭代器的灵魂，不会造成内存积压
        val res3: RDD[String] = dataRDD.mapPartitionsWithIndex(
            (pindex, piter) => {
                new Iterator[String] {
                    println(s"---$pindex----- connect mysql --------")
                
                    override def hasNext: Boolean = {
                        if (piter.hasNext == false) {
                            println(s"---$pindex----- close mysql --------")
                            false
                        } else
                            true
                    }
                
                    override def next(): String = {
                        val value: Int = piter.next
                        println(s"---$pindex----- select $value --------")
                        value + "selected"
                    }
                }
            }
        )
        res3.foreach(println)
        
    }
}
