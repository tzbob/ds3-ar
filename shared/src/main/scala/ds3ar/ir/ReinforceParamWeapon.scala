package ds3ar.ir

import cats.data.Xor
import ds3ext.{ds3ext => ProtoBuf}

object ReinforceParamWeapon {
  private val protoBufferData =
    ProtoBufferHelper.read(ProtoBuf.ReinforceParamWeapon)
  lazy val all = protoBufferData.map(ReinforceParamWeapon.apply)
  lazy val map = all.map(x => x.normalizedId -> x).toMap

  def find(key: Int): Error Xor ReinforceParamWeapon =
    Xor.fromOption(map.get(key), Error.NotFound("Reinforce Param Weapon", key))
}

case class ReinforceParamWeapon(private val rep: ProtoBuf.ReinforceParamWeapon) extends AnyVal {
  def normalizedId: Int = rep.id / 100 * 100 + rep.materialSetId2

  def specialEffectId1: Int = rep.spEffectId1
  def specialEffectId2: Int = rep.spEffectId2
  def specialEffectId3: Int = rep.spEffectId3

  def reinforcement: WeaponReinforcement = {
    val statModifiers = LevelFields(
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
