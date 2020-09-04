package com.msb.bigdata.scala

/*
java中：有一个知识点：静态。实现了实例化类对象时，类中非静态的部分可以个性化，而静态的部分可以归一化。
scala中：class定义的是必须被定义为对象才能使用的； object约等于静态或单例的，可以直接使用的。同名的class与object为伴生关系，可以在类中使用object中的属性。
 */
object ScalaHelloWorld {
    def main(args: Array[String]): Unit = {
        println("Hello World!")
    }
}
