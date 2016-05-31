package ds3ar.model

import ds3ar.ir._

case class Weapon(
  name: String,
  upgradeLevel: Int,
  ar: WeaponDamageFields[Float],
  effects: EffectFields[Float]
)
