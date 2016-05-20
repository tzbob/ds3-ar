package ds3ar.ir

import cats.data.Xor
import ds3ext.{ds3ext => ProtoBuf}

object AttackElementCorrectParam {
  private val protoBufferData =
    ProtoBufferHelper.read(ProtoBuf.AttackElementCorrectParam)
  lazy val all = protoBufferData.map(AttackElementCorrectParam.apply)
  lazy val map = all.map(x => x.rep.id -> x).toMap

  def find(key: Int): Error Xor AttackElementCorrectParam =
    Xor.fromOption(map.get(key), Error.NotFound("Attack Element Correct Param", key))
}

case class AttackElementCorrectParam(rep: ProtoBuf.AttackElementCorrectParam) extends AnyVal {
  private def isSet(index: Int): Boolean = {
    val n = Math.pow(2, index).toInt
    (rep.bitmask & n) == n
  }

  def weaponDamageFieldsIsSet: WeaponDamageFields[LevelFields[Boolean]] =
    WeaponDamageFields(
      LevelFields(
        this isSet 0,
        this isSet 1,
        this isSet 2,
        this isSet 3,
        this isSet 4
      ),

      LevelFields(
        this isSet 5,
        this isSet 6,
        this isSet 7,
        this isSet 8,
        this isSet 9
      ),

      LevelFields(
        this isSet 10,
        this isSet 11,
        this isSet 12,
        this isSet 13,
        this isSet 14
      ),

      LevelFields(
        this isSet 15,
        this isSet 16,
        this isSet 17,
        this isSet 18,
        this isSet 19
      ),

      LevelFields(
        this isSet 20,
        this isSet 21,
        this isSet 22,
        this isSet 23,
        this isSet 24
      )
    )

  def physicalBetas: LevelFields[Int] = {
    val str = rep.v25
    val dex = rep.v26
    val int = rep.v27
    val fai = rep.v28
    val lck = rep.v29

    LevelFields(str, dex, int, fai, lck)
  }

  def magicBetas: LevelFields[Int] =
    LevelFields(
      rep.v30,
      rep.v31,
      rep.v32,
      rep.v33,
      rep.v34
    )

  def fireBetas: LevelFields[Int] =
    LevelFields(
      rep.v35,
      rep.v36,
      rep.v37,
      rep.v38,
      rep.v39
    )

  def lightningBetas: LevelFields[Int] =
    LevelFields(
      rep.v40,
      rep.v41,
      rep.v42,
      rep.v43,
      rep.v44
    )

  def darkBetas: LevelFields[Int] =
    LevelFields(
      rep.v45,
      rep.v46,
      rep.v47,
      rep.v48,
      rep.v49
    )

  def betas: WeaponDamageFields[LevelFields[Int]] =
    WeaponDamageFields(
      physicalBetas,
      magicBetas,
      fireBetas,
      lightningBetas,
      darkBetas
    )
}
