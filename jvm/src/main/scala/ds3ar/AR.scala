package ds3ar

import ds3ar.ir._
import ds3ar.ui._

import cats.syntax.all._
import java.io.PrintWriter

import shapeless._
import shapeless.ops.record._

import ds3ext.ds3ext

object Main {
  val mainPage = new MainPage(scalatags.Text)

  def main(args: Array[String]): Unit = {
    new PrintWriter("site/index.html") {
      print(mainPage.page.render)
      close()
    }
    new PrintWriter("site/dev-index.html") {
      print(mainPage.devPage.render)
      close()
    }
  }

  // val vig = 14
  // val att = 6
  // val end = 12
  // val vit = 11
  // val str = 16
  // val dex = 9
  // val int = 8
  // val fth = 9
  // val lck = 11

  // val base = 7

  // val target = 60
  // val usedPoints = (27 - vit) + (27 - end)
  // val pointsLeft = target - usedPoints - base

  // val warrior = LevelFields(str, dex, int, fth, lck)

  // def cap(lvl: Int) = if (lvl > 99) 99 else lvl

  // val levels = for {
  //   addStr <- 0 until pointsLeft
  //   addDex <- 0 until pointsLeft
  //   addLck <- 0 until pointsLeft
  //   if addStr + addDex + addLck == pointsLeft
  // } yield {
  //   LevelFields(addStr, addDex, 0, 0, addLck).map2(warrior)(_ + _)
  // }

  // val epws = FileResourceDataManager.equipParamWeaponManagerFor("v1.6").all

  // val ars = for {
  //   weapon <- epws.par
  //   lvls <- levels.par
  // } yield {
  //   val lvlOpt =
  //     if (weapon.rep.id.toString.endsWith("1500")) lvls.copy(luck = cap(lvls.luck + 10))
  //     else lvls

  //   val ar = weapon.reinforcedAR(lvlOpt.copy(strength = cap((lvlOpt.strength * 1.5).toInt)), 10).toOption.get

  //   (Names.map.get(weapon.rep.id).getOrElse(weapon.rep.id.toString), ar.sum, lvls)
  // }

  // val result = ars.groupBy(_._1).par.mapValues(_.toVector.sortBy(_._2 * -1).head)

  // result.values.toList.sortBy(_._2).foreach(println)

}
