package ds3ar.ir

case class StartingClass(
  name: String,
  level: Int,

  levels: LevelFields[Int]
)

object StartingClass {
  val knight = StartingClass("knight", 9, LevelFields(12, 10, 11, 15, 13, 12, 9, 9, 7))
  val mercenary = StartingClass("mercenary", 8, LevelFields(11, 12, 11, 10, 10, 16, 10, 8, 9))
  val warrior = StartingClass("warrior", 7, LevelFields(14, 6, 12, 11, 16, 9, 8, 9, 11))
  val herald = StartingClass("herald", 9, LevelFields(12, 10, 9, 12, 12, 11, 8, 13, 11))
  val thief = StartingClass("thief", 5, LevelFields(10, 11, 10, 9, 9, 13, 10, 8, 14))
  val assassin = StartingClass("assassin", 10, LevelFields(10, 14, 11, 10, 10, 14, 11, 9, 10))
  val sorcerer = StartingClass("sorcerer", 6, LevelFields(9, 16, 9, 7, 7, 12, 16, 7, 12))
  val pyromancer = StartingClass("pyromancer", 8, LevelFields(11, 12, 10, 8, 12, 9, 14, 14, 7))
  val cleric = StartingClass("cleric", 7, LevelFields(10, 14, 9, 7, 12, 8, 7, 16, 13))
  val deprived = StartingClass("deprived", 1, LevelFields(10, 10, 10, 10, 10, 10, 10, 10, 10))

  val all = List(
    knight, mercenary, warrior, herald, thief,
    assassin, sorcerer, pyromancer, cleric, deprived
  )
}
