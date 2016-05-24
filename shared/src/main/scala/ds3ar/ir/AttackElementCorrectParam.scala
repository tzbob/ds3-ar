package ds3ar.ir

import cats.data.Xor
import ds3ext.ds3ext
import java.io.InputStream

object AttackElementCorrectParam {
  def manager(in: InputStream): DataManager[Int, AttackElementCorrectParam] =
    DataManager(in, ds3ext.AttackElementCorrectParam, "Attack Element Correct Param")(
      AttackElementCorrectParam.apply
    )(_.rep.id)
}

case class AttackElementCorrectParam(private val rep: ds3ext.AttackElementCorrectParam) extends AnyVal {
  private def isSet(index: Int): Boolean = {
    val n = Math.pow(2, index).toInt
    (rep.bitmask & n) == n
  }

  def weaponDamageFieldsIsSet: WeaponDamageFields[OffensiveLevelFields[Boolean]] =
    WeaponDamageFields(
      OffensiveLevelFields(
        this isSet 0,
        this isSet 1,
        this isSet 2,
        this isSet 3,
        this isSet 4
      ),

      OffensiveLevelFields(
        this isSet 5,
        this isSet 6,
        this isSet 7,
        this isSet 8,
        this isSet 9
      ),

      OffensiveLevelFields(
        this isSet 10,
        this isSet 11,
        this isSet 12,
        this isSet 13,
        this isSet 14
      ),

      OffensiveLevelFields(
        this isSet 15,
        this isSet 16,
        this isSet 17,
        this isSet 18,
        this isSet 19
      ),

      OffensiveLevelFields(
        this isSet 20,
        this isSet 21,
        this isSet 22,
        this isSet 23,
        this isSet 24
      )
    )

  def physicalBetas: OffensiveLevelFields[Int] = {
    val str = rep.v25
    val dex = rep.v26
    val int = rep.v27
    val fai = rep.v28
    val lck = rep.v29

    OffensiveLevelFields(str, dex, int, fai, lck)
  }

  def magicBetas: OffensiveLevelFields[Int] =
    OffensiveLevelFields(
      rep.v30,
      rep.v31,
      rep.v32,
      rep.v33,
      rep.v34
    )

  def fireBetas: OffensiveLevelFields[Int] =
    OffensiveLevelFields(
      rep.v35,
      rep.v36,
      rep.v37,
      rep.v38,
      rep.v39
    )

  def lightningBetas: OffensiveLevelFields[Int] =
    OffensiveLevelFields(
      rep.v40,
      rep.v41,
      rep.v42,
      rep.v43,
      rep.v44
    )

  def darkBetas: OffensiveLevelFields[Int] =
    OffensiveLevelFields(
      rep.v45,
      rep.v46,
      rep.v47,
      rep.v48,
      rep.v49
    )

  def betas: WeaponDamageFields[OffensiveLevelFields[Int]] =
    WeaponDamageFields(
      physicalBetas,
      magicBetas,
      fireBetas,
      lightningBetas,
      darkBetas
    )
}
