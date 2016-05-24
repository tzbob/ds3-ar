package ds3ar.ir

import cats._

trait OffensiveLevelFields[T] {
  val strength: T
  val dexterity: T
  val intelligence: T
  val faith: T
  val luck: T

  def sumOffensive(implicit ev: Numeric[T]) =
    List(strength, dexterity, intelligence, faith, luck).sum
}

object OffensiveLevelFields {
  def apply[T](
    strength0: T,
    dexterity0: T,
    intelligence0: T,
    faith0: T,
    luck0: T
  ): OffensiveLevelFields[T] = new OffensiveLevelFields[T] {
    val strength = strength0
    val dexterity = dexterity0
    val intelligence = intelligence0
    val faith = faith0
    val luck = luck0
  }

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
