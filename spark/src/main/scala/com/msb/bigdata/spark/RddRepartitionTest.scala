package com.msb.bigdata.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RddRepartitionTest {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local").setAppName("partition test")
        val sc: SparkContext = new SparkContext(conf)
        sc.setLogLevel("ERROR")
    
        val data: RDD[Int] = sc.parallelize(1 to 10, 5)
        val dataWithIndex: RDD[(Int, Int)] = data.mapPartitionsWithIndex(
            (pindex, piter) => {
                piter.map(value => (pindex, value))
            }
        )
        dataWithIndex.foreach(println)
        
        // 抽样 => 分区数同data
        val sampleData: RDD[Int] = data.sample(false, 0.1, 111)
        println(s"data: ${data.getNumPartitions}, sampleData: ${sampleData.getNumPartitions}")
    
        // 调整分区的数量：repartition
        val repartitioned: RDD[(Int, Int)] = dataWithIndex.repartition(4)
        println(s"repartitioned: ${repartitioned.getNumPartitions}")
        val repartitionedWithIndex = repartitioned.mapPartitionsWithIndex(
            (pindex, piter) => {
                piter.map(value => (pindex, value))
            }
        )
        repartitionedWithIndex.foreach(println)
    
        // repatition底层调用的coalesce
        // 增大分区，不开启shuffle => 怎么知道后续进入哪个分区，所以还是4个分区
        val repartitioned2: RDD[(Int, Int)] = dataWithIndex.coalesce(8, false)
        println(s"repartitioned2: ${repartitioned2.getNumPartitions}")
        val repartitionedWithIndex2 = repartitioned2.mapPartitionsWithIndex(
            (pindex, piter) => {
                piter.map(value => (pindex, value))
            }
        )
        repartitionedWithIndex2.foreach(println)
    
        // 增大分区，开启shuffle => 总分区数是8，但是允许有的分区没有数据
        val repartitioned3: RDD[(Int, Int)] = dataWithIndex.coalesce(8, true)
        println(s"repartitioned3: ${repartitioned3.getNumPartitions}")
        val repartitionedWithIndex3 = repartitioned3.mapPartitionsWithIndex(
            (pindex, piter) => {
                piter.map(value => (pindex, value))
            }
        )
        repartitionedWithIndex3.foreach(println)
    
        // 减少分区，不开启shuffle => 可以减少，把某几个分区拉到一个分区即可。不用分区器，只用加减运算即可，IO移动，区分于shuffle移动
        val repartitioned4: RDD[(Int, Int)] = dataWithIndex.coalesce(3, false)
        println(s"repartitioned4: ${repartitioned4.getNumPartitions}")
        val repartitionedWithIndex4 = repartitioned4.mapPartitionsWithIndex(
            (pindex, piter) => {
                piter.map(value => (pindex, value))
            }
        )
        repartitionedWithIndex4.foreach(println)
    }
}
