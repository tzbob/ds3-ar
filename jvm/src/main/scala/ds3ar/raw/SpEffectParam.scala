package ds3ar.raw

object SpEffectParam {
  implicit val pr = ParamReader.reader[SpEffectParam]("/SpEffectParamOut.csv")
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
