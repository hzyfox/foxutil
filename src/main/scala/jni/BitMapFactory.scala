package jni

/**
  * create with jni
  * USER: guyue
  */

class BitMapFactory {
  type BitMap = Array[Long]
  @native def axv2BitMaps(jsonChars: String, bitMap: BitMap)
}

object BitMapFactory{
  def main(args: Array[String]): Unit = {
      println("hello, scala jni")
  }
}
