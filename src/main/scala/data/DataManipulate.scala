package data

import java.io.File

import jxl.Workbook
import jxl.write.{Label, WritableWorkbook}

/**
  * create with data
  * USER: husterfox
  */
object DataManipulate {
  def getWritableWorkBook(filePath: String): WritableWorkbook = {
    var writableWorkbook: WritableWorkbook = null
    val file = getFile(filePath)
    if (checkFileExist(file)) {
      val workBook = Workbook.getWorkbook(file)
      writableWorkbook = Workbook.createWorkbook(file, workBook)
    } else {
      writableWorkbook = Workbook.createWorkbook(file)
    }
    writableWorkbook
  }

  def getFile(filePath: String): File = {
    new File(filePath)
  }

  def checkFileExist(file: File): Boolean = {
    file.exists()
  }

  def writeRawData(data: (String, List[String]), writableWorkbook: WritableWorkbook, sheetIndex: Int): Unit = {
    val writableSheet = writableWorkbook.createSheet("rawData", 0)
    var row = 0
    var column = 0
    val head = data._1
    head.split("""\s+""").foreach((cell: String) => {
      writableSheet.addCell(new Label(row, column, cell))
      column += 1
    })
    row += 1
    val entity = data._2
    entity.foreach((str: String) => {
      str.split("""\s\s+""").foreach((cell: String) => {
        writableSheet.addCell(new Label(row, column, cell))
        column += 1
      })
      row += 1
    })
    writableWorkbook.write()
    writableWorkbook.close()
  }

  def main(args: Array[String]): Unit = {
    writeRawData(ReadFile.readFile("/Users/husterfox/workspace/foxutil/result/select-3_3"),
      getWritableWorkBook("/Users/husterfox/Desktop/test.xls"),
      0)
  }

}
