package com.msb.bigdata.scala

object ScalaLesson02 {
    // 方法与函数
    def main(args: Array[String]): Unit = {
        // 方法：返回值，参数，函数体
        def func1(name: String): String ={
            println(s"Hello World, $name")
            s"Hello World, $name"
        }
        println(func1("zhangsan"))
        
        // 签名：(Int, Int) => Int，(参数类型列表) => 返回值类型
        // 匿名函数(a: Int, b: Int) => { a + b }, (参数列表) => 函数体
        val funcRef: (Int, Int) => Int = (a: Int, b: Int) => { a + b }
        println(funcRef(3, 4))
        
        // 偏应用函数
        def baseFunc(date: String, tp:String, msg:String)={
            println(s"$date\t$tp\t$msg")
        }
        var info = baseFunc(_: String, "info", _:String)
        var error = baseFunc(_:String, "error", _:String)
        info("2019-07-13", "ok...")
        error("2019-07-14", "error...")
        
        // 可变参数（类型一致）
        def func2(a: Int*) = {
            for (elem <- a) {
                println(elem)
            }
        }
        func2(2)
        func2(2, 3, 4, 5, 6)
        
        // 高阶函数（函数作为参数，或函数作为返回值）
        def computer(a: Int, b: Int, op: (Int, Int) => Int): Unit ={
            val res: Int = op(a, b)
            println(res)
        }
        computer(3, 8, (x: Int, y: Int) => { x+y })
        // 语法糖：参数按顺序调用
        computer(3, 8, _ + _)
        
        def factory(op: String):(Int, Int) => Int = {
            def plus(x: Int, y: Int): Int = { x+y }
            if(op.equals("+")){
                plus
            }else{
                (x: Int, y: Int) => { x*y }
            }
        }
        computer(3, 8, factory("+"))
        
        // 柯里化
        def func3(a: Int)(b: Int)(c: String) = {
            println(s"$a\t$b\t$c")
        }
        func3(3)(8)("test")
        def func4(a: Int*)(b: String*): Unit = {
            a.foreach(println)
            b.foreach(println)
        }
        func4(1, 2, 3)("a", "b", "c")
        
        // 为方法创建变量引用
        def test() = { println("test") }
        val funcref = test _
        
    }
}
