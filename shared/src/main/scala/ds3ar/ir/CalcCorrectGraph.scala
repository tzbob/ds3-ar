package ds3ar.ir

import cats.data.Xor
import ds3ext.{ds3ext => ProtoBuf}

object CalcCorrectGraph {
  private val protoBufferData =
    ProtoBufferUtility.read(ProtoBuf.CalcCorrectGraph)
  lazy val all = protoBufferData.map(CalcCorrectGraph.apply)
  lazy val map = all.map(x => x.rep.id -> x).toMap

  def find(key: Int): Error Xor CalcCorrectGraph =
    Xor.fromOption(map.get(key), Error.NotFound("Calc Correct Graph", key))
}

case class CalcCorrectGraph(rep: ProtoBuf.CalcCorrectGraph) extends AnyVal {
  def apply(stat: Float): Float = {
    require(stat > 0 && stat <= 99)

    val (xA, xB, yA, yB, p) =
      if (stat < rep.stageMaxVal1)
        (rep.stageMaxVal0, rep.stageMaxVal1, rep.stageMaxGrowVal0, rep.stageMaxGrowVal1, rep.adjPtMaxGrowVal0)
      else if (stat < rep.stageMaxVal2)
        (rep.stageMaxVal1, rep.stageMaxVal2, rep.stageMaxGrowVal1, rep.stageMaxGrowVal2, rep.adjPtMaxGrowVal1)
      else if (stat < rep.stageMaxVal3)
        (rep.stageMaxVal2, rep.stageMaxVal3, rep.stageMaxGrowVal2, rep.stageMaxGrowVal3, rep.adjPtMaxGrowVal2)
      else
        (rep.stageMaxVal3, rep.stageMaxVal4, rep.stageMaxGrowVal3, rep.stageMaxGrowVal4, rep.adjPtMaxGrowVal3)

    statFunCore(stat, xA, xB, yA, yB, p) / 100
  }

  private def statFunCore(
    stat: Float,
    xA: Float,
    xB: Float,
    yA: Float,
    yB: Float,
    p: Float
  ): Float = {
    val dx = (stat - xA) / (xB - xA)

    val fdx =
      if (p > 0) Math.pow(dx, p)
      else 1 - Math.pow(1 - dx, -p)

    yA + fdx.toFloat * (yB - yA)
  }
}
