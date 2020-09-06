package com.msb.bigdata.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RddMapAndMapValuesTest {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local").setAppName("group topN")
        val sc: SparkContext = new SparkContext(conf)
        sc.setLogLevel("ERROR")
        
        val data: RDD[String] = sc.parallelize(List(
            "hello world",
            "hello spark",
            "hello world",
            "hello hadoop",
            "hello world",
            "hello msb",
            "hello world",
        ))
        val words: RDD[String] = data.flatMap(_.split(" "))
        val kv: RDD[(String, Int)] = words.map((_, 1))
        val res: RDD[(String, Int)] = kv.reduceByKey(_+_)
        val res1: RDD[(String, Int)] = res.map(x => (x._1, x._2*10))
        val res2: RDD[(String, Int)] = res.mapValues(x => x*10)
        // 如果res1和res2后都接groupByKey，res1会产生2次shuffle,res2会产生1次。原因如下：
        // map操作中：key可以发生变化，并且丢弃了前面所用的分区器（preservesPartitioning=false） => 后续shuffle要重新分区
        // mapValues操作中：key一定不会发生变化，保留了前面所用的分区器（preservesPartitioning=true） => 后续shuffle不用重新分区
        // 结论：建议使用flatmapValues和mapValues
    }
}
