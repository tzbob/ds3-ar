package ds3ar.ir

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FileResourceDataManager {
  def equipParamWeaponManagerFor(version: String) = {
    ResourceDataManager.equipParamWeaponManagerFor(streamer, "", version)
  }

  private val streamer = (name: String) => Future {
    getClass().getResourceAsStream(name)
  }
}
