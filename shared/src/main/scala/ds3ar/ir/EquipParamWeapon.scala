package ds3ar.ir

import cats.data.Xor
import cats.syntax.all._

import ds3ext.{ds3ext => ProtoBuf}

object EquipParamWeapon {
  private val protoBufferData = ProtoBufferHelper.read(ProtoBuf.EquipParamWeapon)
  lazy val all = protoBufferData.map(EquipParamWeapon.apply)
  lazy val map = all.map(x => x.rep.id -> x).toMap

  def find(key: Int): Error Xor EquipParamWeapon =
    Xor.fromOption(map.get(key), Error.NotFound("Equip Param Weapon", key))
}

case class EquipParamWeapon(rep: ProtoBuf.EquipParamWeapon) extends AnyVal {
  def coefficients: LevelFields[Float] =
    LevelFields(
      rep.correctStrength,
      rep.correctAgility,
      rep.correctMagic,
      rep.correctFaith,
      rep.correctLuck
    )

  def base: WeaponDamageFields[Int] =
    WeaponDamageFields(
      rep.attackBasePhysics,
      rep.attackBaseMagic,
      rep.attackBaseFire,
      rep.attackBaseThunder,
      rep.attackBaseDark
    )

  private def readReinforceParamWeapon(upgradeLevel: Int): Error Xor ReinforceParamWeapon = {
    val reinforceTypeId = rep.reinforceTypeId
    val searchKey = reinforceTypeId + upgradeLevel
    val find = ReinforceParamWeapon.find _

    find(searchKey) orElse find(searchKey - 1) orElse find(reinforceTypeId)
  }

  def reinforcedWeapon(upgradeLevel: Int): Error Xor WeaponReinforcement =
    readReinforceParamWeapon(upgradeLevel).map(_.reinforcement)

  def reinforcedBase(upgradeLevel: Int): Error Xor WeaponDamageFields[Float] =
    for {
      reinforcement <- reinforcedWeapon(upgradeLevel)
    } yield (base |@| reinforcement.damageModifiers) map (_ * _)

  private def readAecp: Error Xor AttackElementCorrectParam =
    AttackElementCorrectParam.find(rep.aecpId)

  def readAecpWeaponDamageFields: Error Xor WeaponDamageFields[LevelFields[Boolean]] =
    readAecp.map(_.weaponDamageFieldsIsSet)

  def readStatFunctions: Error Xor DamageFields[CalcCorrectGraph] = {
    def find = CalcCorrectGraph.find _
    for {
      psf <- find(rep.physicsStatFunc)
      msf <- find(rep.magicStatFunc)
      fsf <- find(rep.fireStatFunc)
      lsf <- find(rep.thunderStatFunc)
      dsf <- find(rep.darkStatFunc)
      bsf <- find(rep.bleedStatFunc)
      poisonSf <- find(rep.poisonStatFunc)
    } yield {
      DamageFields(psf, msf, fsf, lsf, dsf, bsf, poisonSf)
    }
  }

  def spEffectParam(upgradeLevel: Int): Error Xor (EffectFields[Int], EffectFields[Int], EffectFields[Int]) = {
    val weaponReinforcementParams = readReinforceParamWeapon(upgradeLevel)

    def find(key: Int): Error Xor EffectFields[Int] =
      // FIXME: Hardcoded '-1' and missing Effect?
      if (key == -1 || key == 130071303) EffectFields(0, 0, 0).right
      else SpEffectParam.find(key).map(_.base)

    for {
      wrp <- weaponReinforcementParams

      key1 = rep.spEffectBehaviorId1 + wrp.specialEffectId1
      eoh1 = find(key1)

      key2 = rep.spEffectBehaviorId2 + wrp.specialEffectId2
      eoh2 = find(key2)

      key3 = rep.spEffectBehaviorId3 + wrp.specialEffectId3
      eoh3 = find(key3)

      a <- eoh1
      b <- eoh2
      c <- eoh3
    } yield (a, b, c)
  }

  def effects(levels: LevelFields[Int], upgradeLevel: Int): Error Xor EffectFields[Double] = {
    for {
      statFunctions <- readStatFunctions
      reinforcement <- reinforcedWeapon(upgradeLevel)

      // FIXME: Ignoring eoh3, speculation that eoh3 models effect on hit effects that affect the user
      sparams <- spEffectParam(upgradeLevel)
    } yield {
      val (eoh1, eoh2, _) = sparams

      val bleedUseWpn = rep.correctLuck * 0.01
      val poisonUseWpn = bleedUseWpn

      val bleedStatWt = statFunctions.bleed(levels.luck)
      val poisonStatWt = statFunctions.poison(levels.luck)

      def coef(useWpn: Double, statWt: Double) =
        1 + useWpn * reinforcement.statModifiers.luck * statWt

      val bleedCoef = coef(bleedUseWpn, bleedStatWt)
      val poisonCoef = coef(poisonUseWpn, poisonStatWt)

      val bleedBase = eoh1.bleed + eoh2.bleed
      val poisonBase = eoh1.poison + eoh2.poison

      val bleedTotal = bleedBase * bleedCoef
      val poisonTotal = poisonBase * poisonCoef
      val frostTotal = eoh1.frost + eoh2.frost

      EffectFields(bleedTotal, poisonTotal, frostTotal)
    }
  }

  def weaponDamageCoefficientSums(levels: LevelFields[Int], upgradeLevel: Int): Error Xor WeaponDamageFields[Double] = {
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

  def reinforcedAR(levels: LevelFields[Int], upgradeLevel: Int): Error Xor WeaponDamageFields[Double] =
    for {
      coef <- weaponDamageCoefficientSums(levels, upgradeLevel)
      base <- reinforcedBase(upgradeLevel)
    } yield (coef |@| base) map (_ * _)

}
