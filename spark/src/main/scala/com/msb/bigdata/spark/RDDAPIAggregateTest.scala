package com.msb.bigdata.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDAPIAggregateTest {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local").setAppName("test")
        val sc: SparkContext = new SparkContext(conf)
        sc.setLogLevel("ERROR")
    
        val data: RDD[(String, Int)] = sc.parallelize(List(
            ("zhangsan", 234),
            ("zhangsan", 5667),
            ("zhangsan", 343),
            ("lisi", 212),
            ("lisi", 44),
            ("lisi", 33),
            ("wangwu", 535),
            ("wangwu", 22)
        ))
        // 分组
        val groupRDD: RDD[(String, Iterable[Int])] = data.groupByKey()
        groupRDD.foreach(println)
    
        // 转换回去
        val dataCopy: RDD[(String, Int)] = groupRDD.flatMap(e => e._2.map(x => (e._1, x)))
        dataCopy.foreach(println)
        val dataCopy2: RDD[(String, Int)] = groupRDD.flatMapValues(e => e.iterator)
        dataCopy2.foreach(println)
    
        val left2RDD: RDD[(String, List[Int])] = groupRDD.mapValues(e => e.toList.sorted.take(2))
        left2RDD.foreach(println)
    
        // sum、count、min、max、avg
        val sumRDD: RDD[(String, Int)] = data.reduceByKey(_+_)
        val maxRDD: RDD[(String, Int)] = data.reduceByKey((ov, nv) => if(ov > nv) ov else nv)
        val minRDD: RDD[(String, Int)] = data.reduceByKey((ov, nv) => if(ov < nv) ov else nv)
        val countRDD: RDD[(String, Int)] = data.mapValues(e => 1).reduceByKey(_+_)
        val avgRDD: RDD[(String, Int)] = sumRDD.join(countRDD).mapValues(e => e._1 / e._2)
        // 算平均数的优化，只用拉起一次初始数据集
        val sumAndCountRDD: RDD[(String, (Int, Int))] = data.combineByKey(
            // createCombiner：V => C，第一条记录的value怎么放入hashmap
            (value: Int) => (value, 1),
            // mergeValue: (C, V) => C，如果有第二条记录，第二条及以后的value如何放入hashmap
            (oldValue: (Int, Int), newValue: Int) => (oldValue._1 + newValue, oldValue._2 + 1),
            // mergeCombiners: (C, C) => C，
            (v1: (Int, Int), v2: (Int, Int)) => (v1._1 + v2._1, v1._2 + v2._2))
        sumAndCountRDD.mapValues(e => e._1/e._2).foreach(println)
    }
}
