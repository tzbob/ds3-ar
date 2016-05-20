package ds3ar.ir

trait Error

object Error {
  case class NotFound(target: String, key: Int) extends Error {
    override def toString(): String =
      s"Error: did not find key $key in {{$target}}"
  }
}
