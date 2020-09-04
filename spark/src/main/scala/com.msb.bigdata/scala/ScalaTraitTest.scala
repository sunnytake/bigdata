package com.msb.bigdata.scala

class Person(name:String){
    def hello() = {
        println(s"$name say hello")
    }
}

trait Good{
    def goodsay() = {
        println("I am good!")
    }
    def smell(): Unit
}

trait Toy{
    def toysay() = {
        println("I am toy!")
    }
}

class SuperMan(name: String) extends Good with Toy{
    override def smell(): Unit = {
        println("I am smell")
    }
    
    override def goodsay(): Unit = {
        println("I am new good say")
    }
}

object ScalaTraitTest {
    def main(args: Array[String]): Unit = {
    
    }
}


