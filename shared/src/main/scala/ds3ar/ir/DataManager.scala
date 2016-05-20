package ds3ar.ir

import cats.data.Xor

import com.trueaccord.scalapb._
import java.io.InputStream

object DataManager {
  def apply[A <: GeneratedMessage with Message[A], T, Key](
    input: InputStream,
    companion: GeneratedMessageCompanion[A],
    errorMessage: String
  )(builder: A => T)(keyMaker: T => Key) = new DataManager[Key, T] {
    val all = companion.streamFromDelimitedInput(input).map(builder)
    val errorMessage = "Equip Param Weapon"
    val map = all.map(x => keyMaker(x) -> x).toMap
  }
}

trait DataManager[Key, T] {
  val all: Stream[T]
  val errorMessage: String
  val map: Map[Key, T]
  def find(key: Key): Error Xor T =
    Xor.fromOption(map.get(key), Error.NotFound(errorMessage, key))
}
