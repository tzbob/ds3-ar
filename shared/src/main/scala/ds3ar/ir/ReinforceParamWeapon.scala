package ds3ar.ir

import cats.data.Xor
import ds3ext.ds3ext
import java.io.InputStream

object ReinforceParamWeapon {
  def manager(in: InputStream): DataManager[Int, ReinforceParamWeapon] =
    DataManager(in, ds3ext.ReinforceParamWeapon, "Reinforce Param Weapon")(
      ReinforceParamWeapon.apply
    )(_.normalizedId)
}

case class ReinforceParamWeapon(private val rep: ds3ext.ReinforceParamWeapon) extends AnyVal {
  def normalizedId: Int = rep.id / 100 * 100 + rep.materialSetId2

  def specialEffectId1: Int = rep.spEffectId1
  def specialEffectId2: Int = rep.spEffectId2
  def specialEffectId3: Int = rep.spEffectId3

  def reinforcement: WeaponReinforcement = {
    val statModifiers = OffensiveLevelFields(
      rep.correctStrengthRate,
      rep.correctAgilityRate,
      rep.correctMagicRate,
      rep.correctFaithRate,
      rep.correctLuckRate
    )

    val damageModifiers = WeaponDamageFields(
      rep.physicsAtkRate,
      rep.magicAtkRate,
      rep.fireAtkRate,
      rep.thunderAtkRate,
      rep.darkAtkRate
    )

    WeaponReinforcement(statModifiers, damageModifiers)
  }
}
