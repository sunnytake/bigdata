package com.msb.bigdata.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/*
分组取topN：
tq.data记录了每天的气温采集数据（每天可能有多条数据)，
请取出每月气温最高的两天
 */
object GroupTopN {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local").setAppName("group topN")
        val sc: SparkContext = new SparkContext(conf)
        sc.setLogLevel("ERROR")
    
        val fileRDD: RDD[String] = sc.textFile("spark\\src\\main\\resources\\data\\tq.data")
        val dataRDD: RDD[(Int, Int, Int, Int)] = fileRDD.map(line => line.split("\t")).map(arr => {
            val arrs: Array[String] = arr(0).split("-")
            // 年，月，日，温度
            (arrs(0).toInt, arrs(1).toInt, arrs(2).toInt, arr(1).toInt)
        })
        
        // 方法1：
        // groupByKey可能出问题，如果数据量很大，会导致内存溢出（除非key、value很少）
        // 且基于算子实现了函数：去重，排序
        val grouped: RDD[((Int, Int), Iterable[(Int, Int)])] = dataRDD.map(t4 => ((t4._1, t4._2), (t4._3, t4._4))).groupByKey()
        val res: RDD[((Int, Int), List[(Int, Int)])] = grouped.mapValues(arr => {
            val map: mutable.HashMap[Int, Int] = new mutable.HashMap()
            arr.foreach(x => {
                if (map.getOrElse(x._1, 0) < x._2)
                    map.put(x._1, x._2)
            })
            map.toList.sorted(new Ordering[(Int, Int)] {
                override def compare(x: (Int, Int), y: (Int, Int)): Int = y._2.compareTo(x._2)
            })
        })
        res.foreach(println)
        
        // 隐式转换 =》 排序
        implicit val suibian = new Ordering[(Int, Int)]{
            override def compare(x: (Int, Int), y: (Int, Int)): Int = y._2.compareTo(x._2)
        }
        
        // 方法2：
        // 取巧，spark rdd的reduceByKey取Max间接达到按日去重，让自己的算子更简单
        val reduced2: RDD[((Int,Int,Int), Int)] = dataRDD.map(t4 => ((t4._1, t4._2, t4._3), t4._4)).reduceByKey((x:Int, y:Int) => if(y > x) y else x)
        val maped2: RDD[((Int, Int), (Int, Int))] = reduced2.map(t2 => ((t2._1._1, t2._1._2), (t2._1._3, t2._2)))
        val grouped2: RDD[((Int, Int), Iterable[(Int, Int)])] = maped2.groupByKey()
        grouped2.mapValues(arr => arr.toList.sorted.take(2)).foreach(println)
    
        // 方法3：结果错误
        // 按照年-月-温度倒序排序，排序过程不会内存溢出
        val sorted3: RDD[(Int, Int, Int, Int)] = dataRDD.sortBy(t4 => (t4._1, t4._2, t4._4), false)
        // 按照年-月-日，去重
        val reduced3: RDD[((Int, Int, Int), Int)] = sorted3.map(t4 => ((t4._1, t4._2, t4._3), t4._4)).reduceByKey((x:Int, y:Int) => if(y>x) y else x)
        val maped3: RDD[((Int, Int), (Int, Int))] = reduced3.map(t2 => ((t2._1._1, t2._1._2), (t2._1._3, t2._2)))
        // 按照年-月，分组
        val grouped3: RDD[((Int, Int), Iterable[(Int, Int)])] = maped3.groupByKey()
        grouped3.mapValues(arr => arr.toList.sorted.take(2)).foreach(println)
        // 上面三个shuffle，年-月-温度、年-月-日、年-月，多个shuffle会打乱之前的顺序，这就是导致错误的原因
        
        // 方法4：如何让方法3的结果正确
        val sorted4: RDD[(Int, Int, Int, Int)] = dataRDD.sortBy(t4 => (t4._1, t4._2, t4._4), false)
        // 这样分组不会乱序，原因是：后续shuffle使用的前面shuffle的顺序子集，则可以保持顺序 => 相当于二次排序
        val grouped4: RDD[((Int, Int), Iterable[(Int, Int)])] = sorted4.map(t4 => ((t4._1, t4._2), (t4._3, t4._4))).groupByKey()
    
        // 最终版本：使用combiner，对数据进行压缩
        val kv: RDD[((Int, Int), (Int, Int))] = dataRDD.map(t4 => ((t4._1, t4._2), (t4._3, t4._4)))
        val res5 = kv.combineByKey(
            // 第一条记录怎么放：
            (v1: (Int, Int)) => {
                Array(v1, (0, 0), (0, 0))
            },
            // 第二条及之后记录如何放：
            (oldv: Array[(Int, Int)], newv: (Int, Int)) => {
                // 去重，排序
                var flag = 0
                for (i <- 0 until oldv.length) {
                    if (oldv(i)._1 == newv._1) {
                        if (oldv(i)._2 < newv._2) {
                            flag = 1
                            oldv(i) = newv
                        } else {
                            flag = 2
                        }
                    }
                }
                if (flag == 0) {
                    oldv(oldv.length - 1) = newv
                }
                // 排序后返回一个新的对象，会不断gc，这是一个性能不高的地方
                // oldv.sorted
                // 在oldv上进行原地排序，性能较高
                util.Sorting.quickSort(oldv)
                oldv
            },
            (v1: Array[(Int, Int)], v2: Array[(Int, Int)]) => {
                val unioned: Array[(Int, Int)] = v1.union(v2)
                unioned.sorted
            }
        )
        res5.map(x => (x._1, x._2.toList)).foreach(println)
        
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    }
}
