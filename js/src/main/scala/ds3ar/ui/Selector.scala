package ds3ar.ui

import org.scalajs.dom

object Selector {
  def byHtml[Frag](html: Html[Frag]): dom.Element =
    dom.document.getElementById(html.id)
}
