package com.msb.bigdata.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDAPISortTest {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local").setAppName("sort test")
        val sc: SparkContext = new SparkContext(conf)
    
        // 统计各网站的PV、UV，只显示top5
        // pv
        val fileRDD: RDD[String] = sc.textFile("spark\\src\\main\\resources\\data\\pvuv.data", 5)
        val pairRDD: RDD[(String, Int)] = fileRDD.map(line => (line.split("\t")(5), 1))
        val reduceRDD: RDD[(String, Int)] = pairRDD.reduceByKey(_+_)
        val swapRDD: RDD[(Int, String)] = reduceRDD.map(_.swap)
        val sortedRDD: RDD[(Int, String)] = swapRDD.sortByKey(false)
        val pvRes: RDD[(String, Int)] = sortedRDD.map(_.swap)
        val pv: Array[(String, Int)] = pvRes.take(5)
        pv.foreach(println)
    
        // uv
        val keysRDD: RDD[(String, String)] = fileRDD.map(
            line => {
                val strs: Array[String] = line.split("\t")
                (strs(5), strs(0)) // 网址，ip
            }
        )
        val key: RDD[(String, String)] = keysRDD.distinct()
        val uvPairRDD: RDD[(String, Int)] = key.map(k => (k._1, 1))
        val uvReduceRDD: RDD[(String, Int)] = uvPairRDD.reduceByKey(_+_)
        val uvSortedRDD: RDD[(String, Int)] = uvReduceRDD.sortBy(_._2, false)
        val uv: Array[(String, Int)] = uvSortedRDD.take(5)
        uv.foreach(println)
        
        // 为什么是6个job
        
        
        
//        while(true){
//
//        }
    }
}
