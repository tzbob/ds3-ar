package ds3ar.raw

import kantan.csv.generic._ // case class decoder derivation

object ReinforceParamWeapon {
  implicit val pr = ParamReader.reader[ReinforceParamWeapon]("/ReinforceParamWeaponOut.csv")
  lazy val all = ParamReader.read[ReinforceParamWeapon]
}

case class ReinforceParamWeapon(
  id: Int,
  index: Int,

  physicsAtkRate: Float,
  magicAtkRate: Float,
  fireAtkRate: Float,
  thunderAtkRate: Float,
  staminaAtkRate: Float,
  saWeaponAtkRate: Float,
  saDurabilityRate: Float,
  correctStrengthRate: Float,
  correctAgilityRate: Float,
  correctMagicRate: Float,
  correctFaithRate: Float,
  physicsGuardCutRate: Float,
  magicGuardCutRate: Float,
  fireGuardCutRate: Float,
  thunderGuardCutRate: Float,
  poisonGuardResistRate: Float,
  diseaseGuardResistRate: Float,
  bloodGuardResistRate: Float,
  curseGuardResistRate: Float,
  staminaGuardDefRate: Float,
  spEffectId1: Byte,
  spEffectId2: Byte,
  spEffectId3: Byte,
  residentSpEffectId1: Byte,
  residentSpEffectId2: Byte,
  residentSpEffectId3: Byte,
  materialSetId1: Byte,
  materialSetId2: Byte,
  darkAtkRate: Float,
  darkGuardResistRate: Float,
  correctLuckRate: Float,
  unknown1: Float,
  unknown2: Float,
  unknown3: Float
) {
  val normalizedId: Int = id / 100 * 100 + materialSetId2
}
