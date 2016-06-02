package ds3ar.model

import ds3ar.ir._
import cats.syntax.all._

object OptimalClass {
  val defaults = StartingClass.all.map { sc =>
    Result(sc, sc.level, sc.levels)
  }

  case class Result(
    startingClass: StartingClass,
    soulLevel: Int,
    levels: LevelFields[Int]
  )

  def compute(levels0: LevelFields[Int]): List[Result] = {
    val soulLevelToClass = StartingClass.all.map { clazz =>
      val diff = levels0.map2(clazz.levels)(_ - _)
      val diffPositive = diff.map(Math.max(_, 0))

      val actualLevels = clazz.levels.map2(levels0)(Math.max)
      val soulLevel = diffPositive.sum + clazz.level

      Result(clazz, soulLevel, actualLevels)
    }

    soulLevelToClass.sortBy(_.soulLevel)
  }
}
