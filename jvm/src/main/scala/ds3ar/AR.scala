import ds3ar.ir._

import kantan.csv.generic._

import cats.syntax.all._

import ds3ext.ds3ext.EquipParamWeapon

object Main extends App {

  println(EquipParamWeapon())

 //  val target = 81
 //  val usedPoints = 27 - 14 + 27 - 12
 //  val pointsLeft = target - usedPoints

 //  val warrior = LevelFields(21, 14, 8, 9, 11)

 //  def cap(lvl: Int) = if (lvl > 99) 99 else lvl

 //  val levels = for {
 //    addStr <- 0 until pointsLeft
 //    addDex <- 0 until pointsLeft
 //    addLck <- 0 until pointsLeft
 //    if addStr + addDex + addLck == pointsLeft
 //  } yield {
 //    LevelFields(addStr, addDex, 0, 0, addLck).map2(warrior)(_ + _)
 // }

 //  println(levels.length)

 //  val epws = ParamReader.read[EquipParamWeapon]

 //  val ars = for {
 //    weapon <- epws
 //    lvl <- levels
 //  } yield {
 //    val levels0 =
 //      if (weapon.id.toString.endsWith("1500")) lvl.copy(luck = cap(lvl.luck + 10))
 //      else lvl

 //    val cappedLevels = levels0.copy(strength = (levels0.strength * 1.5).toInt).map(cap)

 //    val ar = weapon.reinforcedAR(cappedLevels, 10).toOption.get

 //    (weapon.weaponClass, weapon.id, lvl, ar.physical)
 //  }

 //  val grouped = ars.groupBy(_._2).mapValues(_.sortBy(_._4 * -1).head)

 //  grouped.values.toList.sortBy(_._3.luck).filter(_._4 > 400).foreach(println)

}
