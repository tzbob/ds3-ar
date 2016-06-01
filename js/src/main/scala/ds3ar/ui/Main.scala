package ds3ar.ui

import ds3ar.model.OptimalClass
import scala.concurrent.ExecutionContext.Implicits.global

import cats.syntax.all._

import scala.scalajs.js.JSApp
import org.scalajs.dom
import scala.util.Try
import scalatags.JsDom.all._

import ds3ar.ir._
import ds3ar.ui._

object Main extends JSApp {
  val mainPage = new MainPage(scalatags.JsDom)
  val template = mainPage.template

  val epwCurrent = AjaxResourceDataManager.equipParamWeaponManagerFor("v1.6")
  val levelInputs = mainPage.myLevelInput.map(Selector.byHtml)

  def calculateOptimalClass(ev: Any) = {
    val levelValuesRaw = levelInputs.map(_.asInstanceOf[dom.html.Input].value)
    val levelValuesInt = levelValuesRaw.map { lvl =>
      Try(lvl.toInt).toOption
    }
    val optimalClass = OptimalClass.compute(levelValuesInt)

    val table = template.optimalClasses(optimalClass).render
    val target = Selector.byHtml(mainPage.myClassResults)

    println(target)
    println(target.firstChild)

    target.replaceChild(table, target.firstChild)
  }

  def main(): Unit = {
    levelInputs.map(_.addEventListener("input", calculateOptimalClass _))
    println("Main has been executed")
    // AjaxResourceDataManager.equipParamWeaponManagerFor("v1.6").foreach { manager =>
    //   manager.all.foreach { epw =>
    //     val ar = epw.reinforcedAR(warrior, 10)
    //     ar.foreach { arFields =>
    //       val elem = ar2Html(arFields).render
    //       dom.document.body.appendChild(elem)
    //     }
    //   }
    // }
  }
}
