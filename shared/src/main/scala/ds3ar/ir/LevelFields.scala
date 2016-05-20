package ds3ar.ir

import cats._

case class LevelFields[T](
  strength: T,
  dexterity: T,
  intelligence: T,
  faith: T,
  luck: T) {

  def sum(implicit ev: Numeric[T]) =
    List(strength, dexterity, intelligence, faith, luck).sum
}

object LevelFields {
  implicit val levelFieldsApply: Apply[LevelFields] =
    new Apply[LevelFields] {
      def map[A, B](fa: LevelFields[A])(f: A => B): LevelFields[B] =
        LevelFields(f(fa.strength),
          f(fa.dexterity),
          f(fa.intelligence),
          f(fa.faith),
          f(fa.luck))

      def ap[A, B](fa: LevelFields[A => B])(f: LevelFields[A]): LevelFields[B] =
        LevelFields(
          fa.strength(f.strength),
          fa.dexterity(f.dexterity),
          fa.intelligence(f.intelligence),
          fa.faith(f.faith),
          fa.luck(f.luck))
    }
}
