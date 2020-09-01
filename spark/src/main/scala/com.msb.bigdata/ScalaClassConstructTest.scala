package com.msb.bigdata

//class ScalaClassConstructTest {
//
//    println("initialize start")
//
//    def this(name: String){
//        // 必须调用默认构造
//        this()
//    }
//
//    println("initialize end")
//}
//
//
//class ScalaClassConstructTest(name: String) {
//
//    println("initialize start")
//
//    println("initialize end")
//}

class ScalaClassConstructTest(name: String) {
    
    println("initialize start")
    
    def this(name: String, sex: String){
        // 必须调用默认构造
        this(name="zhangsan")
        println("hello")
    }
    
    println("initialize end")
}