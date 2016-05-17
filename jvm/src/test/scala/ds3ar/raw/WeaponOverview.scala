package ds3ar.raw

case class WeaponOverview(
  id: Int,

  baseARPhysical: Short,
  baseARMagic: Short,
  baseARFire: Short,
  baseARLightning: Short,
  baseARDark: Short,

  physicalAR: Float,
  magicAR: Float,
  fireAR: Float,
  lightningAR: Float,
  darkAR: Float,
  totalAR: Float,

  bleed: Float,
  poison: Float,
  frost: Float
) {

  val ar: WeaponDamageFields[Float] =
    WeaponDamageFields(
      physicalAR,
      magicAR,
      fireAR,
      lightningAR,
      darkAR
    )

  val effects: EffectFields[Float] =
    EffectFields(bleed, poison, frost)
}
