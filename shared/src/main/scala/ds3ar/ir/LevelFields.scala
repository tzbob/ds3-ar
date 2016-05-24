package ds3ar.ir

import cats._

trait LevelFields[T] extends OffensiveLevelFields[T] {
  val vigor: T
  val attunement: T
  val endurance: T
  val vitality: T

  val strength: T
  val dexterity: T
  val intelligence: T
  val faith: T
  val luck: T

  def sum(implicit ev: Numeric[T]) =
    List(vigor, attunement, endurance, vitality,
      strength, dexterity, intelligence, faith, luck).sum
}

object LevelFields {
  def apply[T](
    vigor0: T,
    attunement0: T,
    endurance0: T,
    vitality0: T,
    strength0: T,
    dexterity0: T,
    intelligence0: T,
    faith0: T,
    luck0: T
  ): LevelFields[T] = new LevelFields[T] {
    val vigor = vigor0
    val attunement = attunement0
    val endurance = endurance0
    val vitality = vitality0

    val strength = strength0
    val dexterity = dexterity0
    val intelligence = intelligence0
    val faith = faith0
    val luck = luck0
  }

  implicit val levelFieldsApply: Apply[LevelFields] =
    new Apply[LevelFields] {
      def map[A, B](fa: LevelFields[A])(f: A => B): LevelFields[B] =
        LevelFields(
          f(fa.vigor),
          f(fa.attunement),
          f(fa.endurance),
          f(fa.vitality),
          f(fa.strength),
          f(fa.dexterity),
          f(fa.intelligence),
          f(fa.faith),
          f(fa.luck)
        )

      def ap[A, B](fa: LevelFields[A => B])(f: LevelFields[A]): LevelFields[B] =
        LevelFields(
          fa.vigor(f.vigor),
          fa.attunement(f.attunement),
          fa.endurance(f.endurance),
          fa.vitality(f.vitality),
          fa.strength(f.strength),
          fa.dexterity(f.dexterity),
          fa.intelligence(f.intelligence),
          fa.faith(f.faith),
          fa.luck(f.luck)
        )
    }
}
