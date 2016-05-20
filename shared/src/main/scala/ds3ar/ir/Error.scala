package ds3ar.ir

trait Error

object Error {
  case class NotFound[Key](target: String, key: Key) extends Error {
    override def toString(): String =
      s"Error: did not find key $key in {{$target}}"
  }
}
