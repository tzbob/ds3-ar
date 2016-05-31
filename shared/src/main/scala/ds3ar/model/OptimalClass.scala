package ds3ar.model

import ds3ar.ir._
import cats.syntax.all._

object OptimalClass {
  case class Result(
    startingClass: StartingClass,
    soulLevel: Int,
    levels: LevelFields[Int]
  )

  def compute(levels: LevelFields[Option[Int]]): List[Result] = {
    val levels0 = levels.map(_ getOrElse 0)

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
