package ds3ar.ui

import ds3ar.ir._
import scalatags.generic._
import shapeless.tag.@@

class MainPage[Builder, Output <: FragT, FragT](
  val bundle: Bundle[Builder, Output, FragT]
) {
  import bundle.all._

  val async = "async".emptyAttr
  val defer = "defer".emptyAttr
  val helper = new TagsHelper(bundle)

  val myHead = Html { _ =>
    head(
      meta(charset := "utf-8"),
      meta(name := "description", content := "A front-end template that helps you build fast, modern mobile web apps."),
      meta(name := "viewport", content := "width=device-width, initial-scale=1.0, minimum-scale=1.0"),
      bundle.tags2.title("Dark Souls III Attack Calculator"),
      link(rel := "shortcut icon", href := "images/favicon.png"),
      link(rel := "stylesheet", href := "https://fonts.googleapis.com/icon?family=Material+Icons"),
      link(rel := "stylesheet", href := "getmdl-select.min.css"),
      link(rel := "stylesheet", href := "material.min.css"),
      link(rel := "stylesheet", href := "styles.css")
    )
  }

  val myHeader = Html { _ =>
    header(
      cls := "mdl-color-text--primary",
      h1("DARK SOULS III"),
      h3("Attack Rating")
    )
  }

  val myLevelInput = LevelFields(
    Html(id => helper.input(id, "Vig")),
    Html(id => helper.input(id, "Att")),
    Html(id => helper.input(id, "End")),
    Html(id => helper.input(id, "Vit")),
    Html(id => helper.input(id, "Str")),
    Html(id => helper.input(id, "Dex")),
    Html(id => helper.input(id, "Int")),
    Html(id => helper.input(id, "Fth")),
    Html(id => helper.input(id, "Lck"))
  )

  val myCompute = Html { id =>
    helper.cardButton(id, "Compute Attack Rating Table")
  }

  val my2hSwitch = Html(id => helper.switch(id, "2H"))

  val myWeaponDamageHeaders = WeaponDamageFields(
    Html(id0 => th(id := id0, "Physical")),
    Html(id0 => th(id := id0, "Magic")),
    Html(id0 => th(id := id0, "Fire")),
    Html(id0 => th(id := id0, "Lightning")),
    Html(id0 => th(id := id0, "Dark"))
  )

  val myWeaponDamageTotalHeader = Html(id0 => th(id := id0, "Total"))

  val myEffectHeaders = EffectFields(
    Html(id0 => th(id := id0, "Bleed")),
    Html(id0 => th(id := id0, "Poison")),
    Html(id0 => th(id := id0, "Frost"))
  )

  val page =
    html(
      lang := "en",

      myHead.html,

      body(
        cls := "mdl-demo mdl-color--grey-100 mdl-color-text--grey-700 mdl-base",

        myHeader.html,

        div(
          cls := "mdl-layout__content mdl-grid",

          div(
            cls := "input-card mdl-cell mdl-shadow--2dp mdl-cell--12-col mdl-card",
            div(id := "progress", cls := "mdl-progress mdl-js-progress"),
            div(cls := "mdl-card__title mdl-card--expand"),

            form(
              id := "stats", action := "#",
              div(myLevelInput.all.map(_.html))
            ),

            div(
              cls := "mdl-card__actions mdl-card--border",

              myCompute.html,
              helper.spacer,
              my2hSwitch.html
            )
          ),

          table(
            cls := "mdl-cell mdl-cell--12-col mdl-shadow--2dp sortable-theme-bootstrap",
            thead(
              tr(
                th(),
                th(colspan := 6, "Attack Rating"),
                th(colspan := 3, "Effects")
              ),
              tr(
                th(cls := "mdl-data-table__cell--non-numeric", "Weapon") ::
                  myWeaponDamageHeaders.all.map(_.html) :::
                  myWeaponDamageTotalHeader.html ::
                  myEffectHeaders.all.map(_.html)
              )
            ),
            tbody( // Names.map.toList.sortBy(_._1).map {
            //   case (id, name) =>
            //     tr(
            //       td(cls := "mdl-data-table__cell--non-numeric", name),
            //       td(id),
            //       td(id),
            //       td(id),
            //       td(id),
            //       td(id),
            //       td(id),
            //       td(id),
            //       td(id),
            //       td(id)
            //     )
            // }
            )
          )
        ),

        script(async, defer, src := "material.min.js"),
        script(async, defer, src := "getmdl-select.min.js"),
        script(async, defer, src := "jvm/src/main/resources/main-fastopt.js")
      )
    )

}
