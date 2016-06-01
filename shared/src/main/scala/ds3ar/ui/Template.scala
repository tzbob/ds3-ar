package ds3ar.ui

import ds3ar.ir._
import ds3ar.model._
import scalatags.generic._
import shapeless.tag.@@

class Template[Builder, Output <: FragT, FragT](
  val bundle: Bundle[Builder, Output, FragT]
) {
  import bundle.all._
  val helper = new TagsHelper(bundle)

  def optimalClasses(results: List[OptimalClass.Result]): Frag = {
    val headerContent = List("Class", "SL") ::: LevelFields.fieldAbbrevs.all
    val headerRows = tr(headerContent.map(c => th(c)))

    def row(r: OptimalClass.Result) = {
      val tds = r.startingClass.name ::
        r.soulLevel.toString ::
        r.levels.all.map(_.toString)
      tr(tds.map(c => td(c)))
    }

    val contentRows = results.map(row)
    helper.table(List(headerRows), contentRows)
  }
}
