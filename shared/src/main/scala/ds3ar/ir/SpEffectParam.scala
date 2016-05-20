package ds3ar.ir

import cats.data.Xor
import ds3ext.{ds3ext => ProtoBuf}

object SpEffectParam {
  private val protoBufferData =
    ProtoBufferUtility.read(ProtoBuf.SpEffectParam)
  lazy val all = protoBufferData.map(SpEffectParam.apply)
  lazy val map = all.map(x => x.rep.id -> x).toMap

  def find(key: Int): Error Xor SpEffectParam =
    Xor.fromOption(map.get(key), Error.NotFound("Special Effect Parameters", key))
}

case class SpEffectParam(rep: ProtoBuf.SpEffectParam) extends AnyVal {
  def base: EffectFields[Int] = EffectFields(rep.bloodAttackPower, rep.poisonAttackPower, rep.frostAttackPower)
}
