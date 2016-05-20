package ds3ar.ir

case class WeaponReinforcement(
  statModifiers: LevelFields[Float],
  damageModifiers: WeaponDamageFields[Float]
)
