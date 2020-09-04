package com.msb.bigdata.scala

class Dog(name: String, age:Int){}
case class Pet(name: String, age:Int)

object ScalaCaseClassTest {
    def main(args: Array[String]): Unit = {
        val dog1 = new Dog("dog1", 18)
        val dog2 = new Dog("dog1", 18)
        // false
        println(dog1.equals(dog2))
        println(dog1 == dog2)
    
        val pet1 = new Pet("pet1", 18)
        val pet2 = new Pet("pet1", 18)
        // true
        println(pet1.equals(pet2))
        println(pet1 == pet2)
        
    }
}
