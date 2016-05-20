package ds3ar.ir

import cats.data.Xor
import ds3ext.ds3ext
import java.io.InputStream

object CalcCorrectGraph {
  def manager(in: InputStream): DataManager[Int, CalcCorrectGraph] =
    DataManager(in, ds3ext.CalcCorrectGraph, "Attack Element Correct Param")(
      CalcCorrectGraph.apply
    )(_.rep.id)
}

case class CalcCorrectGraph(private val rep: ds3ext.CalcCorrectGraph) extends AnyVal {
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
