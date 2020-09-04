package com.msb.bigdata.scala
import scala.collection.immutable

object ScalaLesson01 {
    // 流程控制
    def main(args: Array[String]): Unit = {
        // if
        var a = 3
        if( a < 0){
            println(s"$a < 0")
        }else{
            print(s"$a >= 0")
        }
        
        //while
        var b = 0
        while(b < 10){
            println(b)
            b += 1
        }
        
        // for
        val seqs: Range.Inclusive = 1 to (10, 2)
        val seqs2: Range = 1 until (10, 2)
        println(seqs)
        for( i <- seqs if(i%2 == 0)){
            println(i)
        }
    
        for(i <- 1 to 9;j <- 1 to 9 if (j<= i)){
            print(s"$i * $j = ${i * j}\t")
            if(j == i) println()
        }
    
        // 生成器
        val seqs3: immutable.IndexedSeq[Int] = for (i <- 1 to 10) yield {
            i + 8
        }
        println(seqs3)
    }
}
