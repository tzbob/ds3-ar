package ds3ar.ir

import kantan.csv.{CsvReader, ReadResult, RowDecoder}
import kantan.csv.ops._ // kantan.csv syntax
import kantan.csv.generic._ // case class decoder derivation

import scala.io.Source
import scala.util.Try

trait ParamReader[T] {
  val fileName0: String
}

object ParamReader {
  private def readFile(fileName: String): String = {
    Source.fromURL(getClass.getResource(fileName)).mkString
  }

  def read[T: RowDecoder](fileName: String): List[T] = {
    readFile(fileName).asCsvReader[T](',', false).toList.map(_.toOption).flatten
  }
}
