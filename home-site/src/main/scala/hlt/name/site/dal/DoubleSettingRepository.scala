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
    DoubleSettingRepository.DEFAULT_PAMPERS_WEIGHT)

  def getPampersCount: Double = getSetting(
    DoubleSettingRepository.PAMPERS_COUNTER_SETTING,
    DoubleSettingRepository.DEFAULT_PAMPERS_COUNT)

  def decrementPampersCount(): Unit = {
    val items = findByName(DoubleSettingRepository.PAMPERS_COUNTER_SETTING)
    val item = if (items.size() > 0) {
      items.get(0)
    } else {
      val x = new DALDoubleSetting
      x.name = DoubleSettingRepository.PAMPERS_COUNTER_SETTING
      x.value = 0
      x
    }
    item.value -= 1
    save(item)
  }
}

object DoubleSettingRepository {
  val PAMPERS_WEIGHT_SETTING : String = "Pampers Weight"
  val PAMPERS_COUNTER_SETTING : String = "Pampers Count"
  val DEFAULT_PAMPERS_WEIGHT : Double = 16
  val DEFAULT_PAMPERS_COUNT : Double = 0
}
