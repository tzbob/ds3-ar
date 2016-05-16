package ds3ar.raw

import kantan.csv.generic._ // case class decoder derivation
import cats.syntax.all._

import org.scalatest.FunSuite

import org.scalatest.Matchers._

class EquipParamWeaponTest extends FunSuite {

  test("hollow bandits knife") {
    val epws = ParamReader.read[EquipParamWeapon]

    val levels = LevelFields(40, 40, 10, 10, 10)
    val upgradeLevel = 10

    val hbk = epws.find(_.id == 1011500).get

    val hbkBase = hbk.reinforcedBase(upgradeLevel).get
    assert(hbkBase === WeaponDamageFields(133, 0, 0, 0, 0))

    val hbkCoefs = hbk.weaponDamageCoefficientSums(levels, upgradeLevel).get
    hbkCoefs.physical shouldBe 1.6855 +- 0.002
    hbkCoefs.magic shouldBe 1
    hbkCoefs.fire shouldBe 1
    hbkCoefs.lightning shouldBe 1
    hbkCoefs.dark shouldBe 1

    val hbkAR = hbk.reinforcedAR(levels, upgradeLevel).get
    hbkAR.physical shouldBe 224.1715 +- 0.002
    hbkAR.magic shouldBe 0
    hbkAR.fire shouldBe 0
    hbkAR.lightning shouldBe 0
    hbkAR.dark shouldBe 0

    val hbkEffects = hbk.effectCoefficientSums(levels, upgradeLevel).get
    hbkEffects.bleed shouldBe 34.27285714 +- 0.002
  }

}
