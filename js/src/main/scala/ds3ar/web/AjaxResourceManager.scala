package ds3ar.web

import ds3ar.ir._
import java.io.InputStream
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.ext.Ajax

import cats.syntax.all._
import cats.std.future._
import scala.concurrent.Future
import scala.scalajs.js.typedarray._

object AjaxResourceDataManager {
  def fromName[A](companion: A): Future[InputStream] = {
    val simpleNameForDs3Ext = companion.getClass.getName.drop("ds3ar.ir.".length).dropRight(1)
    val name = s"/js/src/main/resources/${simpleNameForDs3Ext}Out.dat"

    Ajax.get(url = name, responseType = "arraybuffer")
      .map(_.response.asInstanceOf[ArrayBuffer])
      .map(x => new ArrayBufferInputStream(x))
  }

  val epwStream = fromName(EquipParamWeapon)
  val rpwStream = fromName(ReinforceParamWeapon)
  val aecpStream = fromName(AttackElementCorrectParam)
  val ccgStream = fromName(CalcCorrectGraph)
  val sepStream = fromName(SpEffectParam)

  val equipParamWeaponManager: Future[DataManager[Int, EquipParamWeapon]] =
    (epwStream |@| rpwStream |@| aecpStream |@| ccgStream |@| sepStream) map (EquipParamWeapon.manager)
}
