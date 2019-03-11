package data

import scala.io.Source

/**
  * create with data
  * USER: husterfox
  */
object ReadFile {
  def readFile(filePath: String): (String, List[String]) = {
    val strList = Source.fromFile(filePath).getLines().filter((str: String) => {
      str.contains("avgt") || str.contains("Cnt")
    }).toList
    val head = strList.head
    (head, strList.drop(1))
  }

  def compareFile(filePath1: String, filePath2: String) = {
    val file1List = Source.fromFile(filePath1).getLines()
    val file2List = Source.fromFile(filePath2).getLines()
    var lineNumber = 1
    while (file1List.hasNext && file2List.hasNext) {
      val str1 = file1List.next()
      val str2 = file2List.next()
      if (str1 != str2) {
        println(s"found difference at line $lineNumber")
      }
      lineNumber += 1
    }
    if (file1List.hasNext || file2List.hasNext) {
      println("file length not euqal")
    }
  }

  def main(args: Array[String]): Unit = {
//        for (i<- 0 until 5){
//          compareFile("/Users/guyue/Desktop/rust"+i,"/Users/guyue/Desktop/jni"+i)
//          println(s"$i-------------------------------------------------------------$i")
//        }
//    Source.fromFile("/Users/guyue/workspace/mison/mison/src/main/resources/mison/test.json").getLines()
//      .collect {
//        case line if line.startsWith(s"giant:") => line.drop("giant:".length)
//      }.toList.head.foreach(
//      char => if (char.toBinaryString.length > 8) {
//        println(char)
//      }
//    )
    println(10.toChar.toString.equals("\n"))
  }
}
