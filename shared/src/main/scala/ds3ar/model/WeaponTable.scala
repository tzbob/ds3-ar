package ds3ar.model

import cats.data.Xor
import scala.concurrent.Future

import ds3ar.ir._
import scala.concurrent.ExecutionContext.Implicits.global

object WeaponTable {
  def weaponTable(
    levels: LevelFields[Int],
    upgradeLevel: Int
  )(
    implicit
    dm: DataManager[_, EquipParamWeapon]
  ): (Stream[Error], Stream[Weapon]) = {

    val epws = dm.all
    val seqXors = epws.map { epw =>
      for {
        ar <- epw.reinforcedAR(levels.offensive, upgradeLevel)
        effs <- epw.effects(levels.offensive, upgradeLevel)
        name <- epw.name
      } yield {
        Weapon(name, upgradeLevel, ar, effs)
      }
    }

    val errors = seqXors.collect {
      case Xor.Left(err) => err
    }
    val weapons = seqXors.collect {
      case Xor.Right(wps) => wps
    }

    (errors, weapons)
  }
}
