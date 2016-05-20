package ds3ar.ir

import cats._

case class EffectFields[T](
  bleed: T,
  poison: T,
  frost: T
)

object EffectFields {
  implicit val effectFieldsApply: Apply[EffectFields] =
    new Apply[EffectFields] {
      def map[A, B](fa: EffectFields[A])(f: A => B): EffectFields[B] =
        EffectFields(
          f(fa.bleed),
          f(fa.poison),
          f(fa.frost)
        )

      def ap[A, B](fa: EffectFields[A => B])(f: EffectFields[A]): EffectFields[B] =
        EffectFields(
          fa.bleed(f.bleed),
          fa.poison(f.poison),
          fa.frost(f.frost)
        )
    }
}
