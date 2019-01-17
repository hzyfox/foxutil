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

}
