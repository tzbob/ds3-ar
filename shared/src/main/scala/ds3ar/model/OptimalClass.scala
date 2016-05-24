package ds3ar.model

import ds3ar.ir._
import cats.syntax.all._

object OptimalClass {
  def compute(levels: LevelFields[Int]): StartingClass = {
    val soulLevelToClass = StartingClass.all.map { clazz =>
      val diff = levels.map2(clazz)(_ - _)
      (diff.sum + clazz.level) -> clazz // soul level
    }

    val bestClass = soulLevelToClass.maxBy(_._1)

    bestClass._2
  }
}
