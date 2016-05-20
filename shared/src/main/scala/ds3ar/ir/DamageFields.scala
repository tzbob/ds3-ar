package ds3ar.ir

case class DamageFields[T](
  physical: T,
  magic: T,
  fire: T,
  lightning: T,
  dark: T,
  bleed: T,
  poison: T
) {
  val asWeaponDamageFields =
    WeaponDamageFields(physical, magic, fire, lightning, dark)
}
