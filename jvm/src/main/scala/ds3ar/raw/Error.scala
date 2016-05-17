package ds3ar.raw

trait Error

object Error {
  case class NotFound(id: Int, target: String, key: Int) extends Error {
    override def toString(): String =
      s"Error: did not find $id in {{$target}} with key $key"
  }
}
