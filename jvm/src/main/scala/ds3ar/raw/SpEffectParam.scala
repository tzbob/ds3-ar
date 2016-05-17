package ds3ar.raw

import kantan.csv.generic._ // case class decoder derivation

object SpEffectParam {
  implicit val pr = ParamReader.reader[SpEffectParam]("/SpEffectParamOut.csv")
  lazy val all = ParamReader.read[SpEffectParam]
}

case class SpEffectParam(
  id: Int,
  index: Int,
  poisonBase: Short,
  strangeValue: Int,
  bleedBase: Short,
  strangeEnum: Byte,
  frostBase: Short
)
