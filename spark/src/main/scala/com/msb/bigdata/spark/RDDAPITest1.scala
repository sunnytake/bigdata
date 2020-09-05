package com.msb.bigdata.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDAPITest1 {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local").setAppName("test1")
        val sc: SparkContext = new SparkContext(conf)
        
        val dataRDD: RDD[Int] = sc.parallelize(List(1,2,3,4,5,4,3,2,1))
        
        // 数值大于3的
        val filterRDD: RDD[Int] = dataRDD.filter(_ > 3)
        val res1: Array[Int] = filterRDD.collect()
        res1.foreach(println)
    
        // 去重 => 即distinct的内部实现过程
        val res2: RDD[Int] = dataRDD.map((_, 1)).reduceByKey(_+_).map(_._1)
        res2.foreach(println)
    
        // union后分区数为两个rdd分区数之和
        val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5))
        val rdd2: RDD[Int] = sc.parallelize(List(3, 4, 5, 6, 7))
        println(rdd1.partitions.size)
        println(rdd2.partitions.size)
        val unionRDD: RDD[Int] = rdd1.union(rdd2)
        println(unionRDD.partitions.size)
        
        // 笛卡尔积：窄依赖，数据集传输是纯IO，不涉及shuffle读写（shuffle需要分区，进而实现数据集分发）
        // 把rdd1全量拷贝到rdd2所在机器，把rdd2全量拷贝到rdd1所在机器
        // 不需要区分记录，直接本地IO拉取数据，一定比先partition计算分区号，然后shuffle落文件，最后IO拉取速度快
        val cartesianRDD: RDD[(Int, Int)] = rdd1.cartesian(rdd2)
        cartesianRDD.foreach(println)
    
        // 交集：宽依赖，会触发shuffle
        val intersectionRDD: RDD[Int] = rdd1.intersection(rdd2)
        intersectionRDD.foreach(println)
    
        // 差集：有方向的，宽依赖，会触发shuffle
        val subtractRDD: RDD[Int] = rdd1.subtract(rdd2)
    
        val kv1: RDD[(String, Int)] = sc.parallelize(List(
            ("zhangsan", 11),
            ("zhangsan", 12),
            ("lisi", 13),
            ("wangwu", 14)
        ))
        val kv2: RDD[(String, Int)] = sc.parallelize(List(
            ("zhangsan", 21),
            ("zhangsan", 22),
            ("lisi", 23),
            ("wangwu", 28)
        ))
        val cogroupRDD: RDD[(String, (Iterable[Int], Iterable[Int]))] = kv1.cogroup(kv2)
        cogroupRDD.foreach(println)
    
        // join
        val joinRDD: RDD[(String, (Int, Int))] = kv1.join(kv2)
        joinRDD.foreach(println)
    
        // left out join
        val leftOuterJoinRDD: RDD[(String, (Int, Option[Int]))] = kv1.leftOuterJoin(kv2)
        leftOuterJoinRDD.foreach(println)
        
    }
}
