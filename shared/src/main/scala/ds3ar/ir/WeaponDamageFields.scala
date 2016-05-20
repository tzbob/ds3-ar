package ds3ar.ir

import cats._

case class WeaponDamageFields[T](
  physical: T,
  magic: T,
  fire: T,
  lightning: T,
  dark: T
) {
  def sum(implicit ev: Numeric[T]) =
    List(physical, magic, fire, lightning, dark).sum
}

object WeaponDamageFields {
  implicit val weaponDamageFieldsApply: Apply[WeaponDamageFields] =
    new Apply[WeaponDamageFields] {
      def map[A, B](fa: WeaponDamageFields[A])(f: A => B): WeaponDamageFields[B] =
        WeaponDamageFields(
          f(fa.physical),
          f(fa.magic),
          f(fa.fire),
          f(fa.lightning),
          f(fa.dark)
        )

      def ap[A, B](fa: WeaponDamageFields[A => B])(f: WeaponDamageFields[A]): WeaponDamageFields[B] =
        WeaponDamageFields(
          fa.physical(f.physical),
          fa.magic(f.magic),
          fa.fire(f.fire),
          fa.lightning(f.lightning),
          fa.dark(f.dark)
        )
    }
}
