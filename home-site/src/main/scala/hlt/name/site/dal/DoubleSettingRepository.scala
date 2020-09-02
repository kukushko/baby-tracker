package hlt.name.site.dal

import org.springframework.data.repository.CrudRepository

trait DoubleSettingRepository extends CrudRepository[DALDoubleSetting, Integer] {

  def findByName(name: String): java.util.List[DALDoubleSetting]

  def getSetting(name: String, defaultValue: Double): Double = {
    val items = findByName(name)
    if (items.size() > 0)
      items.get(0).value
    else {
      defaultValue
    }
  }

  def getPampersWeight: Double = getSetting(
    DoubleSettingRepository.PAMPERS_WEIGHT_SETTING,
    DoubleSettingRepository.DEFAULT_PARMERS_WEIGHT)
}

object DoubleSettingRepository {
  val PAMPERS_WEIGHT_SETTING : String = "Pampers Weight"
  val DEFAULT_PARMERS_WEIGHT : Double = 16
}
