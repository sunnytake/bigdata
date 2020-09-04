package com.msb.bigdata.scala

object ScalaMatchTest {
    def main(args: Array[String]): Unit = {
        val tup: (Double, Int, String, Boolean, Char) = (1.0, 88, "abc", false, 'a')
        val iter: Iterator[Any] = tup.productIterator
    
        val res: Iterator[Unit] = iter.map(
            (x) => {
                x match {
                    case o: Int => println(s"$o is Int")
                    case 88 => println(s"$x is 88")
                    case w: Int if w > 50 => println(s"$w is > 50")
                    case _ => println("do not know type")
                }
            })
        while(res.hasNext) res.next
    }
}
