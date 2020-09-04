package com.msb.bigdata.scala

import java.util

object ScalaImplicitTest3 {
    def main(args: Array[String]): Unit = {
        
        implicit val nameTest = "zhangsan"
        implicit val ageTest = 88
        def ooxx(implicit name:String, age:Int) = {
            println(name + " " + age)
        }
        ooxx
        
    }
}

