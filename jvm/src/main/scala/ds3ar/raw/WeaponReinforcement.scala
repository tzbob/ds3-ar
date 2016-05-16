package ds3ar.raw

case class WeaponReinforcement(
  statModifiers: LevelFields[Float],
  damageModifiers: WeaponDamageFields[Float]
)
