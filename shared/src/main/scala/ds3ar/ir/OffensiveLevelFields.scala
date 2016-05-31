package ds3ar.ir

import cats._

case class OffensiveLevelFields[T](
  strength: T,
  dexterity: T,
  intelligence: T,
  faith: T,
  luck: T
) {
  def sum(implicit ev: Numeric[T]) =
    List(strength, dexterity, intelligence, faith, luck).sum
}

object OffensiveLevelFields {
  implicit val levelFieldsApply: Apply[OffensiveLevelFields] =
    new Apply[OffensiveLevelFields] {
      def map[A, B](fa: OffensiveLevelFields[A])(f: A => B): OffensiveLevelFields[B] =
        OffensiveLevelFields(
          f(fa.strength),
          f(fa.dexterity),
          f(fa.intelligence),
          f(fa.faith),
          f(fa.luck)
        )

      def ap[A, B](fa: OffensiveLevelFields[A => B])(f: OffensiveLevelFields[A]): OffensiveLevelFields[B] =
        OffensiveLevelFields(
          fa.strength(f.strength),
          fa.dexterity(f.dexterity),
          fa.intelligence(f.intelligence),
          fa.faith(f.faith),
          fa.luck(f.luck)
        )
    }
}
