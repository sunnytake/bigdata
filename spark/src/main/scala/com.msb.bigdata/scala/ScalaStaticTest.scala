package com.msb.bigdata.scala

/*
scala中：可以在object/class体中写代码，优先于main方法中代码执行。
* 假设认为scala中的object为静态，类被加载时这些裸露的代码（相当于静态代码块）优先执行
* 如果懂单例，这些代码相当于默认构造方法中的过程。scala的编译器很人性化，让人少写了很多代码。
 */
object ScalaStaticTest {
    
    private val test: ScalaStaticTest = new ScalaStaticTest()
    
    println("ooxx ... up")
    
    def main(args: Array[String]): Unit = {
        println("Hello World!")
        test.printMsg()
    }
    
    println("ooxx ... down")
}

class ScalaStaticTest{
    var a = 3
    
    println(s"ooxx ... up$a")
    
    def printMsg() = {
        println("hello from msg")
    }
    
    println(s"ooxx ... down${a+4}")
}
