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

  def getWipeCount: Double = getSetting(
    DoubleSettingRepository.WIPE_COUNTER_SETTING,
    DoubleSettingRepository.DEFAULT_WIPE_COUNT
  )

  def getWipesPerOutput: Double = getSetting(
    DoubleSettingRepository.WIPES_PER_OUTPUT_SETTING,
    DoubleSettingRepository.DEFAULT_WIPES_PER_OUTPUT
  )

  private def decrement(name: String, count: Double): Unit = {
    val items = findByName(name)
    val item = if (items.size() > 0) {
      items.get(0)
    } else {
      val x = new DALDoubleSetting
      x.name = name
      x.value = 0
      x
    }
    item.value -= count
    save(item)
  }

  def decrementWipeCount(count: Double): Unit = {
    decrement(DoubleSettingRepository.WIPE_COUNTER_SETTING, count)
  }

  def decrementPampersCount(): Unit = {
    decrement(DoubleSettingRepository.PAMPERS_COUNTER_SETTING, 1)
  }
}

object DoubleSettingRepository {
  val PAMPERS_WEIGHT_SETTING : String = "Pampers Weight"
  val PAMPERS_COUNTER_SETTING : String = "Pampers Count"
  val WIPES_PER_OUTPUT_SETTING : String = "Wipes per Output"
  val WIPE_COUNTER_SETTING : String = "Wipe Count"
  val DEFAULT_PAMPERS_WEIGHT : Double = 16
  val DEFAULT_PAMPERS_COUNT : Double = 0
  val DEFAULT_WIPE_COUNT : Double = 0
  val DEFAULT_WIPES_PER_OUTPUT : Double = 2
}
