package ds3ar.ir

import cats.data.Xor
import cats.syntax.all._

import ds3ext.ds3ext
import java.io.InputStream

object EquipParamWeapon {
  def manager(
    epwStream: InputStream,
    rpwStream: InputStream,
    aecpStream: InputStream,
    ccgStream: InputStream,
    sepStream: InputStream
  ): DataManager[Int, EquipParamWeapon] = {
    val rpwM = ReinforceParamWeapon.manager(rpwStream)
    val aecpM = AttackElementCorrectParam.manager(aecpStream)
    val ccgM = CalcCorrectGraph.manager(ccgStream)
    val sepM = SpEffectParam.manager(sepStream)

    DataManager(epwStream, ds3ext.EquipParamWeapon, "Equip Param Weapon")(
      x => EquipParamWeapon(x, rpwM, aecpM, ccgM, sepM)
    )(_.rep.id)
  }
}

case class EquipParamWeapon(
  private val rep: ds3ext.EquipParamWeapon,
  private val rpwManager: DataManager[Int, ReinforceParamWeapon],
  private val aecpManager: DataManager[Int, AttackElementCorrectParam],
  private val ccgManager: DataManager[Int, CalcCorrectGraph],
  private val sepManager: DataManager[Int, SpEffectParam]
) {
  val name: Error Xor String = Names(rep.id)

  private def coefficients: OffensiveLevelFields[Float] =
    OffensiveLevelFields(
      rep.correctStrength,
      rep.correctAgility,
      rep.correctMagic,
      rep.correctFaith,
      rep.correctLuck
    )

  private def base: WeaponDamageFields[Int] =
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
    val find = rpwManager.find _

    find(searchKey) orElse find(searchKey - 1) orElse find(reinforceTypeId)
  }

  private def reinforcedWeapon(upgradeLevel: Int): Error Xor WeaponReinforcement =
    readReinforceParamWeapon(upgradeLevel).map(_.reinforcement)

  /**
   * Returns the reinforced base stats.
   *
   * Takes the regular base values (starting numbers, i.e. +0 version for all
   * damage types (physical, magic, etc.)) and computes the increased values for
   * +upgradeLevel weapons by multiplying with the damage modifiers obtained
   * through reinforcing a weapon.
   *
   * @param upgradeLevel targeted upgrade level (normalization for +5 is accounted for, i.e. +10 -> +5)
   * @return
   * reinforced base stats or an error if there is no corresponding entry in
   * the reinforcement table
   */
  def reinforcedBase(upgradeLevel: Int): Error Xor WeaponDamageFields[Float] =
    for {
      reinforcement <- reinforcedWeapon(upgradeLevel)
    } yield (base |@| reinforcement.damageModifiers) map (_ * _)

  private val readAecp: Error Xor AttackElementCorrectParam =
    aecpManager.find(rep.aecpId)

  private val readAecpWeaponDamageFields: Error Xor WeaponDamageFields[OffensiveLevelFields[Boolean]] =
    readAecp.map(_.weaponDamageFieldsIsSet)

  private val readStatFunctions: Error Xor DamageFields[CalcCorrectGraph] = {
    def find = ccgManager.find _
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

  /**
   * Returns the reinforced effect base stats.
   *
   * Looks up reinforced effect base stats for the three special effect
   * behaviors.
   * @param upgradeLevel
   * @return
   * the reinforced base stats an error if no corresponding entries are found in
   * the reinforcement tables
   */
  private def effectBase(upgradeLevel: Int): Error Xor (EffectFields[Int], EffectFields[Int], EffectFields[Int]) = {
    val weaponReinforcementParams = readReinforceParamWeapon(upgradeLevel)

    def find(key: Int): Error Xor EffectFields[Int] =
      // FIXME: Hardcoded '-1' and missing Effect?
      if (key == -1 || key == 130071303) EffectFields(0, 0, 0).right
      else sepManager.find(key).map(_.base)

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

  /**
   * Returns the final effect values for a user with an upgraded weapon.
   *
   * Computes the total effect value by multiplying the coefficients with the
   * base values. Computes the coefficients by multiplying the weapon effect
   * efficiency (derived from luck), the luck modifiers from the reinforced
   * weapon and the luck scaling coefficients computed with the luck stat
   * function from this weapon.
   * @param levels
   * @param upgradeLevel
   * @return
   * the complete effect values for a user with levels on a +upgradeLevel weapon
   * or an error if an entry is not available in a corresponding table
   */
  def effects(levels: OffensiveLevelFields[Int], upgradeLevel: Int): Error Xor EffectFields[Float] = {
    for {
      statFunctions <- readStatFunctions
      reinforcement <- reinforcedWeapon(upgradeLevel)

      // FIXME: Ignoring eoh3, speculation that eoh3 models effect on hit effects that affect the user
      sparams <- effectBase(upgradeLevel)
    } yield {
      val (eoh1, eoh2, _) = sparams

      val bleedUseWpn = rep.correctLuck * 0.01f
      val poisonUseWpn = bleedUseWpn

      val bleedStatWt = statFunctions.bleed(levels.luck)
      val poisonStatWt = statFunctions.poison(levels.luck)

      def coef(useWpn: Float, statWt: Float): Float =
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

  private def weaponDamageCoefficientSums(levels: OffensiveLevelFields[Int], upgradeLevel: Int): Error Xor WeaponDamageFields[Float] = {
    val statCoefficient = for {
      aecp <- readAecp
      weaponDamageAecpFields <- readAecpWeaponDamageFields
      statFunctions <- readStatFunctions
      rw <- reinforcedWeapon(upgradeLevel)
    } yield {
      val weaponDamageStatFunctions = statFunctions.asWeaponDamageFields

      (weaponDamageStatFunctions |@| aecp.betas |@| weaponDamageAecpFields) map { (statFunction, beta, aecpFields) =>
        val useWpns = (aecpFields |@| coefficients) map { (isSet, coef) =>
          if (isSet) 0.01f * coef else 0
        }

        val statWts = levels.map(x => statFunction(x))

        (useWpns |@| rw.statModifiers |@| beta |@| statWts) map { (useWpn, mod, beta, statWt) =>
          useWpn * mod * beta * statWt * 0.01f
        }
      }
    }

    statCoefficient.map(_.map { x =>
      1 + x.sum
    })
  }

  /**
   * Computes the reinforced attack rating for a user with an upgraded weapon
   * for each damage type.
   * @param levels
   * @param upgradeLevel
   * @return attack rating for each damage type or an error
   */
  def reinforcedAR(levels: OffensiveLevelFields[Int], upgradeLevel: Int): Error Xor WeaponDamageFields[Float] =
    for {
      coef <- weaponDamageCoefficientSums(levels, upgradeLevel)
      base <- reinforcedBase(upgradeLevel)
    } yield (coef |@| base) map (_ * _)

}
