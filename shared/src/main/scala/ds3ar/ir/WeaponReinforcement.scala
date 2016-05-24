package ds3ar.ir

case class WeaponReinforcement(
  statModifiers: OffensiveLevelFields[Float],
  damageModifiers: WeaponDamageFields[Float]
)
