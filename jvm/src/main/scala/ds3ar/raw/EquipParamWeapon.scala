package ds3ar.raw

import cats.data.Xor
import cats.syntax.all._

import kantan.csv.generic._ // case class decoder derivation

object EquipParamWeapon {
  implicit val pr = ParamReader.reader[EquipParamWeapon]("/EquipParamWeaponOut.csv")
}

case class EquipParamWeapon(
  id: Int,
  index: Int,

  behaviorVariationId: Int,
  sortId: Int,
  weight: Float,
  correctStrength: Float,
  correctAgility: Float,
  correctMagic: Float,
  correctFaith: Float,
  physGuardCutRate: Float,
  magGuardCutRate: Float,
  fireGuardCutRate: Float,
  thunGuardCutRate: Float,
  spEffectBehaviorId1: Int,
  spEffectBehaviorId2: Int,
  spEffectBehaviorId3: Int,
  residentSpEffectId1: Int,
  residentSpEffectId2: Int,
  residentSpEffectId3: Int,
  maxDurability: Short,
  attackBasePhysics: Short,
  attackBaseMagic: Short,
  attackBaseFire: Short,
  attackBaseThunder: Short,
  attackBaseStamina: Short,
  stability: Short,
  reinforceTypeId: Short,
  physicsStatFunc: Byte,
  properStrength: Byte,
  properAgility: Byte,
  properMagic: Byte,
  properFaith: Byte,
  magicStatFunc: Byte,
  fireStatFunc: Byte,
  thunderStatFunc: Byte,
  attackBaseDark: Short,
  darkStatFunc: Byte,
  poisonStatFunc: Byte,
  bleedStatFunc: Byte,
  properLuck: Byte,
  correctLuck: Float,
  weaponClass: Short,
  aecpId: Int) {

  val coefficients: LevelFields[Float] =
    LevelFields(correctStrength, correctAgility, correctMagic, correctFaith, correctLuck)

  val baseDamage: WeaponDamageFields[Short] =
    WeaponDamageFields(
      attackBasePhysics,
      attackBaseMagic,
      attackBaseFire,
      attackBaseThunder,
      attackBaseDark)

  def weaponReinforcement(upgradeLevel: Int): Option[ReinforceParamWeapon] = {
    val reinforceParams = ParamReader.read[ReinforceParamWeapon]

    val searchKey = reinforceTypeId + upgradeLevel
    reinforceParams.find(_.id == searchKey)
  }

  def reinforcedWeapon(upgradeLevel: Int): Option[WeaponReinforcement] = {
    val weaponReinforceParams = weaponReinforcement(upgradeLevel)

    for {
      wrp <- weaponReinforceParams
    } yield {
      val statModifiers = LevelFields(
        wrp.correctStrengthRate,
        wrp.correctAgilityRate,
        wrp.correctMagicRate,
        wrp.correctFaithRate,
        wrp.correctLuckRate)

      val damageModifiers = WeaponDamageFields(
        wrp.physicsAtkRate,
        wrp.magicAtkRate,
        wrp.fireAtkRate,
        wrp.thunderAtkRate,
        wrp.darkAtkRate)

      WeaponReinforcement(statModifiers, damageModifiers)
    }
  }

  def reinforcedBase(upgradeLevel: Int): Option[WeaponDamageFields[Float]] =
    for {
      reinforcement <- reinforcedWeapon(upgradeLevel)
    } yield (baseDamage |@| reinforcement.damageModifiers) map (_ * _)

  def reinforcedAR(levels: LevelFields[Int], upgradeLevel: Int): Option[WeaponDamageFields[Double]] =
    for {
      coef <- weaponDamageCoefficientSums(levels, upgradeLevel)
      base <- reinforcedBase(upgradeLevel)
    } yield (coef |@| base) map (_ * _)

  val readAecp: Option[AttackElementCorrectParam] = {
    val aecps = ParamReader.read[AttackElementCorrectParam]
    aecps.find(_.id == this.aecpId)
  }

  val readAecpWeaponDamageFields: Option[WeaponDamageFields[LevelFields[Boolean]]] =
    for {
      aecp <- readAecp
    } yield {
      WeaponDamageFields(
        LevelFields(
          aecp isSet 0,
          aecp isSet 1,
          aecp isSet 2,
          aecp isSet 3,
          aecp isSet 4),

        LevelFields(
          aecp isSet 5,
          aecp isSet 6,
          aecp isSet 7,
          aecp isSet 8,
          aecp isSet 9),

        LevelFields(
          aecp isSet 10,
          aecp isSet 11,
          aecp isSet 12,
          aecp isSet 13,
          aecp isSet 14),

        LevelFields(
          aecp isSet 15,
          aecp isSet 16,
          aecp isSet 17,
          aecp isSet 18,
          aecp isSet 19),

        LevelFields(
          aecp isSet 20,
          aecp isSet 21,
          aecp isSet 22,
          aecp isSet 23,
          aecp isSet 24))
    }

  lazy val readStatFunctions: Option[DamageFields[CalcCorrectGraph]] = {
    val ccgs = ParamReader.read[CalcCorrectGraph]

    def find(sf: Int): Option[CalcCorrectGraph] = ccgs.find(_.id == sf)

    for {
      psf <- find(this.physicsStatFunc)
      msf <- find(this.magicStatFunc)
      fsf <- find(this.fireStatFunc)
      lsf <- find(this.thunderStatFunc)
      dsf <- find(this.darkStatFunc)
      bsf <- find(this.bleedStatFunc)
      poisonSf <- find(this.poisonStatFunc)
    } yield {
      DamageFields(psf, msf, fsf, lsf, dsf, bsf, poisonSf)
    }
  }

  def spEffectParam(upgradeLevel: Int): Option[(SpEffectParam, SpEffectParam, SpEffectParam)] = {
    val spEffectParams = ParamReader.read[SpEffectParam]
    val weaponReinforcementParams = weaponReinforcement(upgradeLevel)

    def find(key: Int): Option[SpEffectParam] =
      if (key == -1) Some(SpEffectParam(0, 0, 0, 0, 0, 0, 0))
      else spEffectParams.find(_.id == key)

    for {
      wrp <- weaponReinforcementParams

      key1 = this.spEffectBehaviorId1 + wrp.spEffectId1
      eoh1 = find(key1)

      key2 = this.spEffectBehaviorId2 + wrp.spEffectId2
      eoh2 = find(key2)

      key3 = this.spEffectBehaviorId3 + wrp.spEffectId3
      eoh3 = find(key3)

      a <- eoh1
      b <- eoh2
      c <- eoh3
    } yield (a, b, c)
  }

  def effectCoefficientSums(levels: LevelFields[Int], upgradeLevel: Int): Option[EffectFields[Double]] = {
    for {
      statFunctions <- readStatFunctions
      reinforcement <- reinforcedWeapon(upgradeLevel)

      // FIXME: Ignoring eoh3, speculation that eoh3 models effect on hit effects that affect the user
      (eoh1, eoh2, _) <- spEffectParam(upgradeLevel)
    } yield {
      val bleedUseWpn = this.correctLuck * 0.01
      val poisonUseWpn = bleedUseWpn

      val bleedStatWt = statFunctions.bleed(levels.luck)
      val poisonStatWt = statFunctions.poison(levels.luck)

      def coef(useWpn: Double, statWt: Double) = 1 + useWpn * reinforcement.statModifiers.luck * statWt

      val bleedCoef = coef(bleedUseWpn, bleedStatWt)
      val poisonCoef = coef(poisonUseWpn, poisonStatWt)

      val bleedBase = eoh1.bleedBase + eoh2.bleedBase
      val poisonBase = eoh1.poisonBase + eoh2.poisonBase

      val bleedTotal = bleedBase * bleedCoef
      val poisonTotal = poisonBase * poisonCoef
      val frostTotal = eoh1.frostBase + eoh2.frostBase

      EffectFields(bleedTotal, poisonTotal, frostTotal)
    }
  }

  def weaponDamageCoefficientSums(levels: LevelFields[Int], upgradeLevel: Int): Option[WeaponDamageFields[Double]] = {
    val statCoefficient = for {
      aecp <- readAecp
      weaponDamageAecpFields <- readAecpWeaponDamageFields
      statFunctions <- readStatFunctions
      rw <- reinforcedWeapon(upgradeLevel)
    } yield {
      val weaponDamageStatFunctions = statFunctions.asWeaponDamageFields

      (weaponDamageStatFunctions |@| aecp.betas |@| weaponDamageAecpFields) map { (statFunction, beta, aecpFields) =>
        val useWpns = (aecpFields |@| coefficients) map { (isSet, coef) =>
          if (isSet) 0.01 * coef else 0
        }

        val statWts = levels.map(x => statFunction(x))

        (useWpns |@| rw.statModifiers |@| beta |@| statWts) map { (useWpn, mod, beta, statWt) =>
          useWpn * mod * beta * statWt * 0.01
        }
      }
    }

    statCoefficient.map(_.map { x =>
      1 + x.sum
    })
  }
}

