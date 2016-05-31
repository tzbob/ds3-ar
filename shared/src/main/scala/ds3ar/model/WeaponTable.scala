package ds3ar.model

import cats.data.Xor
import cats.std.all._
import cats.syntax.all._
import scala.concurrent.Future

import ds3ar.ir._
import scala.concurrent.ExecutionContext.Implicits.global

object WeaponTable {
  def weaponTable(epws: Stream[EquipParamWeapon], levels: LevelFields[Int], upgradeLevel: Int): Future[Xor[Error, Stream[Weapon]]] =
    Future {
      val seqXors = epws.map { epw =>
        for {
          ar <- epw.reinforcedAR(levels.offensive, upgradeLevel)
          effs <- epw.effects(levels.offensive, upgradeLevel)
          name <- epw.name
        } yield Weapon(name, upgradeLevel, ar, effs)
      }
      seqXors.sequenceU
    }

  def partitionedWeaponTable(
    partSize: Int, levels: LevelFields[Int], upgradeLevel: Int
  )(implicit dm: DataManager[_, EquipParamWeapon]): Seq[Future[Xor[Error, Stream[Weapon]]]] = {
    val groupedStream = dm.all.grouped(partSize).toSeq
    groupedStream.map(epws => weaponTable(epws, levels, upgradeLevel))
  }
}
