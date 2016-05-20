package ds3ar.ir

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

  def equalUpTo[T: Numeric](x: EffectFields[T], y: EffectFields[T], precision: T) = {
    x.poison shouldBe y.poison +- precision
    x.bleed shouldBe y.bleed +- precision
    x.frost shouldBe y.frost +- precision
  }

  def testSheet(fileName: String, levels: LevelFields[Int], upgradeLevel: Int) = {
    val wo = ParamReader.read[WeaponOverview](fileName)

    wo.foreach { overview =>
      val epw = EquipParamWeapon.find(overview.id).toOption.get
      val epwAR = epw.reinforcedAR(levels, upgradeLevel)

      epwAR match {
        case Xor.Right(ar) =>
          equalUpTo(ar.map(_.toFloat), overview.ar, 0.0002f)

        case Xor.Left(error) =>
          sys.error(error.toString)
      }

      val epwEff = epw.effects(levels, upgradeLevel)

      epwEff match {
        case Xor.Right(eff) =>
          equalUpTo(eff.map(_.toFloat), overview.effects, 0.0002f)

        case Xor.Left(error) =>
          sys.error(error.toString)
      }

    }
  }

  test("compare +10 40/40/10/10/10 with sheets information") {
    val levels = LevelFields(40, 40, 10, 10, 10)
    val upgradeLevel = 10
    testSheet("/Ds3Ar-10-40-40-10-10-10.csv", levels, upgradeLevel)
  }

  test("compare +5 5/30/80/90/65 with sheets information") {
    val levels = LevelFields(5, 30, 80, 90, 65)
    val upgradeLevel = 5
    testSheet("/Ds3Ar-5-5-30-80-90-65.csv", levels, upgradeLevel)
  }
}
