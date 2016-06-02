package ds3ar.ui

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

case class Html[Frag](mk: Html.Id => Frag) {
  val id = Html.newId()
  lazy val html = mk(id)
}

object Html {
  type Id = String
  private val counter = new AtomicInteger
  private def newId(): Id = counter.incrementAndGet().toString()
}
