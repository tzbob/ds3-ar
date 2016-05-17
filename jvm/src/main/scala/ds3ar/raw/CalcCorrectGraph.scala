package ds3ar.raw

object CalcCorrectGraph {
  implicit val pr = ParamReader.reader[CalcCorrectGraph]("/CalcCorrectGraphOut.csv")
}

case class CalcCorrectGraph(
  id: Int,
  index: Int,

  x0: Float,
  x1: Float,
  x2: Float,
  x3: Float,
  x4: Float,

  y0: Float,
  y1: Float,
  y2: Float,
  y3: Float,
  y4: Float,

  p0: Float,
  p1: Float,
  p2: Float,
  p3: Float,
  p4: Float,

  initInclinationSoul: Float,
  adjustmentValue: Float,
  boundryInclinationSoul: Float,
  boundryValue: Float
) {

  def apply(stat: Float): Double = {
    require(stat > 0 && stat <= 99)

    val result =
      if (stat < x1) statFunCore(stat, x0, x1, y0, y1, p0)
      else if (stat < x2) statFunCore(stat, x1, x2, y1, y2, p1)
      else if (stat < x3) statFunCore(stat, x2, x3, y2, y3, p2)
      else statFunCore(stat, x3, x4, y3, y4, p3)
    result / 100
  }

  private def statFunCore(
    stat: Float,
    xA: Float,
    xB: Float,
    yA: Float,
    yB: Float,
    p: Float
  ): Double = {
    val dx = (stat - xA) / (xB - xA)

    val fdx =
      if (p > 0) Math.pow(dx, p)
      else 1 - Math.pow(1 - dx, -p)

    yA + fdx * (yB - yA)
  }

}
