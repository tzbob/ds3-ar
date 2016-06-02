package ds3ar.ui

import cats.data.Xor
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import cats.syntax.all._

import scala.scalajs.js.JSApp
import org.scalajs.dom
import scala.util.Try
import scalatags.JsDom.all._

import ds3ar.ir._
import ds3ar.model._
import ds3ar.ui._

object Main extends JSApp {
  val epwCurrent = AjaxResourceDataManager.equipParamWeaponManagerFor("v1.6")

  val mainPage = new MainPage(scalatags.JsDom)
  val template = mainPage.template

  object Progress {
    private val progressElement = Selector.byHtml(mainPage.myProgress)

    def start() = progressElement.classList.add("mdl-progress__indeterminate")
    def stop() = progressElement.classList.remove("mdl-progress__indeterminate")
  }

  val levelInputs = mainPage.myLevelInput.map(Selector.byHtml)

  def getCurrentLevels() = {
    val levelValuesRaw = levelInputs.map(_.asInstanceOf[dom.html.Input].value)
    val levelValuesInt = levelValuesRaw.map { lvl =>
      Try(lvl.toInt).toOption
    }
    levelValuesInt.map(_ getOrElse 0)
  }

  def is2h(): Boolean =
    Selector.byHtml(mainPage.my2hSwitch).asInstanceOf[dom.html.Input].checked

  def getCurrentScaledLevels() = {
    val lvls = getCurrentLevels()
    def cap(n: Int) = Math.min(99, n)
    if (is2h()) lvls.copy(strength = cap((lvls.strength * 1.5).toInt))
    else lvls
  }

  val classResults = Selector.byHtml(mainPage.myClassResults)

  def show(): Boolean =
    Selector.byHtml(mainPage.myShowAll).asInstanceOf[dom.html.Input].checked

  def reflectShowClasses() =
    if (show()) {
      classResults.classList.remove("hide-all-but-first")
      classResults.classList.add("show-all")
    } else {
      classResults.classList.remove("show-all")
      classResults.classList.add("hide-all-but-first")
    }

  def calculateOptimalClass() = {
    val optimalClass = OptimalClass.compute(getCurrentLevels())
    val table = template.optimalClasses(optimalClass).render

    classResults.replaceChild(table, classResults.firstChild)
  }

  def calculateWeaponTable() = {
    epwCurrent.foreach { implicit epwCurrent =>
      val (errors, weaponTable) =
        WeaponTable.weaponTable(getCurrentScaledLevels(), getCurrentWeaponLvl())

      errors.foreach(println)

      val table = Selector.byHtml(mainPage.myWeaponTableBody)
      val body = table.querySelector("tbody")

      val newBody = template.weaponsToRows(weaponTable.sortBy(_.ar.sum * -1))
      table.replaceChild(newBody.render, body)
    }
  }

  val upgradeLevel = Selector.byHtml(mainPage.myUpgradeLevel)
  val upgradeLevelText = Selector.byHtml(mainPage.myUpgradeLevelText)

  def getCurrentWeaponLvl() =
    upgradeLevel.asInstanceOf[dom.html.Input].value.toInt

  def syncUpgradeLevelText() = {
    val lvl = getCurrentWeaponLvl()
    val titLvl = lvl / 2
    upgradeLevelText.innerHTML = s"$titLvl / $lvl"
  }

  def main(): Unit = {
    levelInputs.map(_.addEventListener("input", (_: Any) => calculateOptimalClass()))

    upgradeLevel.addEventListener("input", (_: Any) => syncUpgradeLevelText())
    syncUpgradeLevelText()

    val computeButton = Selector.byHtml(mainPage.myCompute)
    computeButton.addEventListener("click", (_: Any) => calculateWeaponTable())
    calculateWeaponTable()

    val showButton = Selector.byHtml(mainPage.myShowAll)
    showButton.addEventListener("click", (_: Any) => reflectShowClasses())

    println("Main has been executed")
  }
}
