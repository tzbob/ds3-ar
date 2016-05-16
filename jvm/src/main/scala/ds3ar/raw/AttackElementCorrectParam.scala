package ds3ar.raw

import kantan.codecs.Result
import kantan.csv.{DecodeError, RowDecoder}
import scala.util.Try

object AttackElementCorrectParam {
  implicit val pr = ParamReader.reader[AttackElementCorrectParam]("/AttackElementCorrectParamOut.csv")

  val seqRowDecoder = implicitly[RowDecoder[Array[Double]]]
  implicit val decoder = seqRowDecoder.mapResult { rawData =>

    val aecpTry = Try {
      val id = rawData(0).toInt
      val index = rawData(1).toInt
      val bitmask = rawData(2).toInt
      val shorts = rawData.drop(3).map(_.toShort)

      AttackElementCorrectParam(id, index, bitmask, shorts)
    }

    Result.fromTry(aecpTry).leftMap(DecodeError.TypeError)
  }
}

case class AttackElementCorrectParam(
  id: Int,
  index: Int,
  bitmask: Int,
  values: Seq[Short]
) {

  def isSet(index: Int): Boolean = {
    val n = Math.pow(2, index).toInt
    (bitmask & n) == n
  }

  val physicalBetas: LevelFields[Short] = {
    val str = values(25)
    val dex = values(26)
    val int = values(27)
    val fai = values(28)
    val lck = values(29)

    LevelFields(str, dex, int, fai, lck)
  }

  val magicBetas: LevelFields[Short] =
    LevelFields(
      values(30),
      values(31),
      values(32),
      values(33),
      values(34)
    )

  val fireBetas: LevelFields[Short] =
    LevelFields(
      values(35),
      values(36),
      values(37),
      values(38),
      values(39)
    )

  val lightningBetas: LevelFields[Short] =
    LevelFields(
      values(40),
      values(41),
      values(42),
      values(43),
      values(44)
    )

  val darkBetas: LevelFields[Short] =
    LevelFields(
      values(45),
      values(46),
      values(47),
      values(48),
      values(49)
    )

  val betas: WeaponDamageFields[LevelFields[Short]] =
    WeaponDamageFields(
      physicalBetas,
      magicBetas,
      fireBetas,
      lightningBetas,
      darkBetas
    )
}
