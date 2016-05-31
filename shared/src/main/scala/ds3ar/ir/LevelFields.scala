package ds3ar.ir

import cats._

// extends OffensiveLevelFields[T]

case class LevelFields[T](
  vigor: T,
  attunement: T,
  endurance: T,
  vitality: T,
  strength: T,
  dexterity: T,
  intelligence: T,
  faith: T,
  luck: T
) {
  val offensive =
    OffensiveLevelFields(
      strength,
      dexterity,
      intelligence,
      faith,
      luck
    )

  def sum(implicit ev: Numeric[T]) = all.sum

  lazy val all: List[T] = List(
    vigor,
    attunement,
    endurance,
    vitality,
    strength,
    dexterity,
    intelligence,
    faith,
    luck
  )
}

object LevelFields {
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
