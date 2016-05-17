package ds3ar.raw

import cats.data.Xor

import kantan.csv.generic._
import cats.syntax.all._

import org.scalatest.FunSuite

import org.scalatest.Matchers._

class EquipParamWeaponTest extends FunSuite {

  def equalUpTo[T: Numeric](x: WeaponDamageFields[T], y: WeaponDamageFields[T], precision: T) = {
    x.physical shouldBe y.physical +- precision
    x.magic shouldBe y.magic +- precision
    x.fire shouldBe y.fire +- precision
    x.lightning shouldBe y.lightning +- precision
    x.dark shouldBe y.dark +- precision
  }

  def testSheet(fileName: String, levels: LevelFields[Int], upgradeLevel: Int) = {
    val wo = ParamReader.read[WeaponOverview](fileName)

    val epws = ParamReader.read[EquipParamWeapon]
    val epwsMap = epws.map(x => x.id -> x).toMap

    wo.foreach { overview =>
      val epw = epwsMap(overview.id)
      val epwAR = epw.reinforcedAR(levels, upgradeLevel)

      epwAR match {
        case Xor.Right(ar) =>
          equalUpTo(ar.map(_.toFloat), overview.ar, 0.0002f)

        case Xor.Left(error) =>
          println(error)
      }
    }
  }

  test("compare +10 40/40/10/10/10 with sheets information") {
    val levels = LevelFields(40, 40, 10, 10, 10)
    val upgradeLevel = 10
    testSheet("/Ds3Ar-10-40-40-10-10-10.csv", levels, upgradeLevel)
  }
}
