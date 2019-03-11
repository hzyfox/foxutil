package data

import java.io.File

import jxl._
import jxl.write.{Label, WritableCell, WritableCellFeatures, WritableCellFormat, WritableSheet, WritableWorkbook}

/**
  * create with data
  * USER: husterfox
  */
object DataManipulate {
  var workBook: Workbook = _
  var writableWorkbook: WritableWorkbook = _
  var outputPath: String = _
  var inputPath: String = _

  def getWritableWorkBook(filePath: String): Unit = {
    val file = getFile(filePath)
    //    if (checkFileExist(file)) {
    //      workBook = Workbook.getWorkbook(file)
    //      writableWorkbook = Workbook.createWorkbook(file, workBook)
    //    } else {
    writableWorkbook = Workbook.createWorkbook(file)
    //    }
  }

  def getFile(filePath: String): File = {
    new File(filePath)
  }

  def checkFileExist(file: File): Boolean = {
    file.exists()
  }

  def writeRawData(data: (String, List[String]), sheetIndex: Int): Unit = {
    val writableSheet = writableWorkbook.createSheet("rawData", sheetIndex)
    var row = 0
    var column = 0
    val head = data._1
    head.split("""\s+""").foreach((cell: String) => {
      writableSheet.addCell(new Label(column, row, cell))
      column += 1
    })
    column = 0
    row += 1
    val entity = data._2
    entity.foreach((str: String) => {
      val str0 = str.replace("±", "")
      str0.split("""\s+""").foreach((cell: String) => {
        writableSheet.addCell(new Label(column, row, cell))
        column += 1
      })
      column = 0
      row += 1
    })
    //    writableWorkbook.write()
    //    writableWorkbook.close()
    //    if (workBook != null) {
    //      workBook.close()
    //      workBook = null
    //    }
  }

  def aggregateData(sheetIndex: Int): Unit = {
    var row = 0
    var column = 0
    val writableSheet = writableWorkbook.createSheet("aggregateData", sheetIndex)
    val rawSheet = writableWorkbook.getSheet(0)
    val column1 = rawSheet.getColumn(1).toList.drop(1).map(_.getContents)
    val distinctKindNumber = column1.distinct.length
    val numberPerKind = column1.length / distinctKindNumber
    val columnSum = rawSheet.getColumns
    val rowSum = rawSheet.getRows

    for (i <- 0 until 1 + numberPerKind) {
      setRow(writableSheet, i, 0, rawSheet.getRow(i))
    }
    column += columnSum
    row += 1 //表头占一行
    for (i <- 0 until distinctKindNumber) {
      for (j <- 0 until numberPerKind) {
        setRow(writableSheet, row + j, column, rawSheet.getRow(1 + numberPerKind + numberPerKind * i + j))
      }
      column += columnSum
    }
    //handleData(writableSheet)
    //    writableWorkbook.write()
    //    writableWorkbook.close()
    //    if (workBook != null) {
    //      workBook.close()
    //      workBook = null
    //    }
  }

  //we hardcode 8 columns here [info]	Benchmark	(entry)	Mode	Cnt	Score	Error	Units
  //maybe we will add column, the this code will be invalid
  def handleData(sheetIndex: Int): Unit = {
    var row = 0
    var column = 0
    val columnCellKindNumber = 8

    val readableSheet = writableWorkbook.getSheet(1)
    writableWorkbook.copySheet(1, "handleData", sheetIndex)
    val writableSheet = writableWorkbook.getSheet(sheetIndex)
    val columnSum = writableSheet.getColumns
    val columnKindNumber = columnSum / columnCellKindNumber
    // the zero row is table head
    val rowSum = writableSheet.getRows
    val dataRowSum = rowSum - 1
    //删除了列之后，列会向左偏移，因此要修正误差
    var deleteNumber = 0
    for (i <- 1 until columnKindNumber) {
      //println(s"delete ${i * columnCellKindNumber} ${i * columnCellKindNumber + 2} ${i * columnCellKindNumber + 3} ${i * columnCellKindNumber + 4} ${i * columnCellKindNumber + 7} ")
      writableSheet.removeColumn(i * columnCellKindNumber - deleteNumber)
      deleteNumber += 1
      writableSheet.removeColumn(i * columnCellKindNumber + 2 - deleteNumber)
      deleteNumber += 1
      writableSheet.removeColumn(i * columnCellKindNumber + 3 - deleteNumber)
      deleteNumber += 1
      writableSheet.removeColumn(i * columnCellKindNumber + 4 - deleteNumber)
      deleteNumber += 1
      writableSheet.removeColumn(i * columnCellKindNumber + 7 - deleteNumber)
      deleteNumber += 1
    }
    copyColumn(columnCellKindNumber - 1, writableSheet.getColumns, writableSheet)
    writableSheet.removeColumn(columnCellKindNumber - 1)
    writableSheet.removeColumn(0)
    for (i <- 0 until columnKindNumber - 1) {
      copyCell(1, 6 + i * 3, 0, 6 + i * 3 + 1, writableSheet, ".Score")
      copyCell(1, 6 + i * 3, 0, 6 + i * 3 + 2, writableSheet, ".Error")
    }
    copyCell(1, 0, 0, 4, writableSheet, ".Score")
    copyCell(1, 0, 0, 5, writableSheet, ".Error")
    writableSheet.removeColumn(0)
    deleteNumber = 0
    for (i <- 0 until columnKindNumber - 1) {
      writableSheet.removeColumn(5 + i * 3 - deleteNumber)
      deleteNumber += 1
    }
    for (i <- 0 until columnKindNumber) {
      groupColumn(3 + i * 2, 3 + i * 2 + 1, writableSheet)
    }
    deleteNumber = 0
    for (i <- 0 until columnKindNumber) {
      writableSheet.removeColumn(3 + i * 2 - deleteNumber)
      deleteNumber += 1
    }
    for (i <- 0 until columnKindNumber) {
      setCellContent(3 + i, 0, writableSheet.getCell(3 + i, 0)
        .getContents.split("±")(0)
        .split("""\.""")(1), writableSheet)
    }

  }

  def setCellContent(col: Int, row: Int, content: String, writableSheet: WritableSheet): Unit = {
    val readCell = writableSheet.getWritableCell(col, row)
    val cellFormat = readCell.getCellFormat
    val cellFeature = readCell.getCellFeatures
    val newCell = new Label(col, row, content)
    if (cellFormat != null) {
      val writableCellFormat = new WritableCellFormat(cellFormat)
      newCell.setCellFormat(writableCellFormat)
    }
    if (cellFeature != null) {
      val writableFeature = new WritableCellFeatures(cellFeature)
      newCell.setCellFeatures(writableFeature)
    }
    writableSheet.addCell(newCell)
  }

  //将fromCol 复制到toCol，并把toCol的内容添加到fromCol背后
  def groupColumn(fromCol: Int, toCol: Int, writableSheet: WritableSheet, contentReverse: Boolean = false): Unit = {
    for (row <- 0 until writableSheet.getRows) {
      copyCell(row, fromCol, row, toCol, writableSheet, "±" + writableSheet.getWritableCell(toCol, row).getContents, contentReverse)
    }
  }

  //仅支持Label类型的cell。
  def copyCell(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int, writableSheet: WritableSheet, content: String = null, contentReverse: Boolean = false): Unit = {
    val readCell = writableSheet.getWritableCell(fromCol, fromRow)
    val newCell = readCell.copyTo(toCol, toRow)
    val cellFormat = readCell.getCellFormat
    val cellFeature = readCell.getCellFeatures
    if (cellFormat != null) {
      val writableCellFormat = new WritableCellFormat(cellFormat)
      newCell.setCellFormat(writableCellFormat)
    }
    if (cellFeature != null) {
      val writableFeature = new WritableCellFeatures(cellFeature)
      newCell.setCellFeatures(writableFeature)
    }
    if (content != null) {
      if (contentReverse)
        writableSheet.addCell(new Label(newCell.getColumn, newCell.getRow, content + newCell.getContents))
      else
        writableSheet.addCell(new Label(newCell.getColumn, newCell.getRow, newCell.getContents + content))
    } else {
      writableSheet.addCell(newCell)
    }
  }


  def copyColumn(fromIndex: Int, toIndex: Int, writableSheet: WritableSheet): Unit = {
    for (i <- 0 until writableSheet.getRows) {
      copyCell(i, fromIndex, i, toIndex, writableSheet)

    }
  }

  def copyRow(fromIndex: Int, toIndex: Int, writableSheet: WritableSheet): Unit = {
    for (i <- 0 until writableSheet.getColumns) {
      copyCell(fromIndex, i, toIndex, i, writableSheet)
    }
  }

  def copySheet(fromSheet: WritableSheet, toSheet: WritableSheet): Unit = {
    writableWorkbook.copySheet(1, "test", 2)
  }

  def setRow(writableSheet: WritableSheet, rowIndex: Int, startColumn: Int, array: Array[Cell]): Unit = {
    var col = startColumn
    for (i <- array.indices) {
      array(i).getType match {
        case CellType.LABEL =>
          writableSheet.addCell(new Label(col, rowIndex, array(i).getContents))
        case CellType.NUMBER =>
          writableSheet.addCell(new write.Number(col, rowIndex, array(i).getContents.toDouble))
        case _ => throw new RuntimeException("unkonwn type: " + array(i).getType.toString)
      }
      col += 1
    }
  }

  def setColumn(writableSheet: WritableSheet, columnIndex: Int, startRow: Int, array: Array[Cell]): Unit = {
    var row = startRow
    for (i <- array.indices) {
      array(i).getType match {
        case CellType.LABEL =>
          writableSheet.addCell(new Label(columnIndex, row, array(i).getContents))
        case CellType.NUMBER =>
          writableSheet.addCell(new write.Number(columnIndex, row, array(i).getContents.toDouble))
        case _ => throw new RuntimeException("unkonwn type: " + array(i).getType.toString)
      }
      row += 1
    }
  }


  def main(args: Array[String]): Unit = {
    outputPath = "/Users/guyue/Desktop/select-jnicost-6.xls"
    inputPath = "/Users/guyue/workspace/mison/result/select-jnicost-6"
    getWritableWorkBook(outputPath)
    writeRawData(ReadFile.readFile(inputPath), 0)

    //getWritableWorkBook(outputPath)
    aggregateData(1)

    //getWritableWorkBook(outputPath)
    handleData(2)
    val sheet2 = writableWorkbook.getSheet(2)
    val cellView = new CellView()
    cellView.setAutosize(true)
    for (i <- 0 until sheet2.getColumns) {
      sheet2.setColumnView(i, cellView)
      //sheet2.setRowView(i, sheet2.getRowView(i).getSize * 2)
    }


    writableWorkbook.write()
    writableWorkbook.close()
    if (workBook != null) {
      workBook.close()
    }
  }

}
