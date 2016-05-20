package ds3ar.ir

import cats.data.Xor
import ds3ext.ds3ext
import java.io.InputStream

object SpEffectParam {
  def manager(in: InputStream): DataManager[Int, SpEffectParam] =
    DataManager(in, ds3ext.SpEffectParam, "Special Effect Parameters")(
      SpEffectParam.apply
    )(_.rep.id)
}

case class SpEffectParam(private val rep: ds3ext.SpEffectParam) extends AnyVal {
  def base: EffectFields[Int] = EffectFields(rep.bloodAttackPower, rep.poisonAttackPower, rep.frostAttackPower)
}
