package ds3ar.ui

import scalatags.generic._
import shapeless.tag.@@

sealed trait ClassNames
sealed trait Colspan

class TagsHelper[Builder, Output <: FragT, FragT](
  val bundle: Bundle[Builder, Output, FragT]
) {
  import bundle.{all => tag}
  import bundle.all._

  val spacer = div(cls := "mdl-layout-spacer")

  def switch(id0: String, content: String) =
    label(cls := "mdl-switch mdl-js-switch", `for` := id0,
      tag.input(tpe := "checkbox", id := id0, cls := "mdl-switch__input",
        span(cls := "mdl-switch__label", content)))

  def input(id0: String, content: String) =
    div(
      cls := "mdl-textfield mdl-js-textfield mdl-textfield--floating-label",
      tag.input(cls := "mdl-textfield__input", tpe := "number", id := id0),
      label(cls := "mdl-textfield__label", `for` := id0, content)
    )

  def cardButton(id0: String, content: String) =
    a(id := id0, cls := "mdl-button mdl-js-button mdl-button--primary", content)

  def select(id0: String, header: String, items: List[String], selected: Int) =
    div(
      cls := "mdl-textfield mdl-js-textfield mdl-textfield--floating-label getmdl-select",

      tag.input(cls := "mdl-textfield__input", tpe := "text", id := id0, value := items(selected), readonly),
      label(`for` := id0, i(cls := "mdl-icon-toggle__label material-icons", "keyboard_arrow_down")),
      label(`for` := id0, cls := "mdl-textfield__label", header),
      ul(`for` := id0, cls := "mdl-menu mdl-menu--bottom-left mdl-js-menu",
        items.map { item =>
          li(cls := "mdl-menu__item", item)
        })
    )

  def headerRows(headers: List[List[(Int @@ Colspan, String)]]): List[Frag] =
    headers.map { row =>
      tr(row.map {
        case (colspan0, content) =>
          th(colspan := colspan0.toString, content)
      })
    }

  def contentRows(content: List[List[(String @@ ClassNames, String)]]): List[Frag] =
    content.map { row =>
      tr(row.map {
        case (classNames, content) =>
          td(cls := classNames.toString, content)
      })
    }

  def table(
    headerRows0: List[Frag],
    contentRows0: List[Frag]
  ) =
    tag.table(
      cls := "mdl-cell mdl-cell--12-col mdl-shadow--2dp",
      thead(headerRows0),
      tbody(contentRows0)
    )

  // tr(
  //   th(),
  //   th(colspan := 6, "Attack Rating"),
  //   th(colspan := 3, "Effects")
  // )
  // tr(
  //   th(cls := "mdl-data-table__cell--non-numeric", "Weapon"),
  //   th("Physical"),
  //   th("Magic"),
  //   th("Fire"),
  //   th("Lightning"),
  //   th("Dark"),
  //   th("Total"),
  //   th("Bleed"),
  //   th("Poison"),
  //   th("Frost")
  // )
}
