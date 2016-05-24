package ds3ar.ir

import java.io.InputStream
import scala.concurrent.ExecutionContext.Implicits.global

import cats.syntax.all._
import cats.std.future._
import scala.concurrent.Future

object ResourceDataManager {
  def fromName[A](companion: A)(streamer: String => Future[InputStream], base: String, version: String): Future[InputStream] = {
    val simpleNameForDs3Ext = companion.getClass.getName.drop("ds3ar.ir.".length).dropRight(1)
    val name = s"$base/$version/${simpleNameForDs3Ext}Out.dat"
    streamer(name)
  }

  def epwStream = fromName(EquipParamWeapon)_
  def rpwStream = fromName(ReinforceParamWeapon)_
  def aecpStream = fromName(AttackElementCorrectParam)_
  def ccgStream = fromName(CalcCorrectGraph)_
  def sepStream = fromName(SpEffectParam)_

  def equipParamWeaponManagerFor(streamer: String => Future[InputStream], base: String, version: String): Future[DataManager[Int, EquipParamWeapon]] =
    (epwStream(streamer, base, version)
      |@| rpwStream(streamer, base, version)
      |@| aecpStream(streamer, base, version)
      |@| ccgStream(streamer, base, version)
      |@| sepStream(streamer, base, version)) map (EquipParamWeapon.manager)
}
