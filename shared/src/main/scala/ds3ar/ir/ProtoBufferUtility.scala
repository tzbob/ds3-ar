package ds3ar.ir

import com.trueaccord.scalapb._

object ProtoBufferUtility {
  def read[A <: GeneratedMessage with Message[A]](companion: GeneratedMessageCompanion[A]): Stream[A] = {
    val simpleNameForDs3Ext = companion.getClass.getName.drop("ds3ext".length * 2 + 2).dropRight(1)
    read(companion, s"/${simpleNameForDs3Ext}Out.dat")
  }

  def read[A <: GeneratedMessage with Message[A]](companion: GeneratedMessageCompanion[A], resourceFileName: String): Stream[A] =
    companion.streamFromDelimitedInput(getClass().getResourceAsStream(resourceFileName))
}
