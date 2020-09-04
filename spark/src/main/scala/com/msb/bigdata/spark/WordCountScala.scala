package com.msb.bigdata.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCountScala {
    def main(args: Array[String]): Unit = {
        
        val conf = new SparkConf().setMaster("local").setAppName("wordcount")
        val sc = new SparkContext(conf)
        
        val fileRDD: RDD[String] = sc.textFile("spark/src/main/resources/data/wordcount")
        val words: RDD[String] = fileRDD.flatMap((x:String) => {x.split(" ")})
        val pairWord: RDD[(String, Int)] = words.map((x:String) => {new Tuple2(x,1)})
        val res: RDD[(String, Int)] = pairWord.reduceByKey((x: Int, y: Int) => {x+y})
        res.foreach(println)
        
        // 精简
        fileRDD.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).foreach(println)
    }
}
