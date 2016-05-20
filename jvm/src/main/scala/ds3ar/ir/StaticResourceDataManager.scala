package ds3ar.ir

import java.io.InputStream


object StaticResourceDataManager {
  def fromName[A](companion: A): InputStream = {
    val simpleNameForDs3Ext = companion.getClass.getName.drop("ds3ar.ir.".length).dropRight(1)
    val name = s"/${simpleNameForDs3Ext}Out.dat"
    getClass().getResourceAsStream(name)
  }

  val epwStream = fromName(EquipParamWeapon)
  val rpwStream = fromName(ReinforceParamWeapon)
  val aecpStream = fromName(AttackElementCorrectParam)
  val ccgStream = fromName(CalcCorrectGraph)
  val sepStream = fromName(SpEffectParam)

  val equipParamWeaponManager: DataManager[Int, EquipParamWeapon] =
    EquipParamWeapon.manager(epwStream, rpwStream, aecpStream, ccgStream, sepStream)
}
