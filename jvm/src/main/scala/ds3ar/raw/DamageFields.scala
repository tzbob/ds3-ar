package ds3ar.raw

/**
  * Created by bob on 16/05/2016.
  */
case class DamageFields[T](
  physical: T,
  magic: T,
  fire: T,
  lightning: T,
  dark: T,
  bleed: T,
  poison: T) {
  val asWeaponDamageFields =
    WeaponDamageFields(physical, magic, fire, lightning, dark)
}
