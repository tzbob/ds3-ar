package ds3ar.raw

case class EquipParamProtector(
  id: Int,
  index: Int,
  sortId: Int,
  sellValue: Int,
  weight: Float,
  poisonResist: Short,
  toxicResist: Short,
  bleedResist: Short,
  curseResist: Byte,
  physicalDef: Float,
  slashDef: Float,
  strikeDef: Float,
  thrustDef: Float,
  magicDef: Float,
  fireDef: Float,
  lightningDef: Float,
  durability: Short,
  durabilityMax: Short,
  poise: Float,
  darkDef: Float,
  frostResist: Short
)
