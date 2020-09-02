package hlt.name.site

import hlt.name.site.dal.{DALDoubleSetting, DoubleSettingRepository}
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SettingInitializer {

  private val logger = LoggerFactory.getLogger(getClass)

  @Autowired
  private var doubleSettingRepository: DoubleSettingRepository = _

  private def ensureSettingExist(name: String, value: => Double): Unit = {
    val items = doubleSettingRepository.findByName(name)
    if (items.size() == 0) {
      logger.info(s"Setting '$name' is not defined, setting default value")
      val s = new DALDoubleSetting
      s.name = name
      s.value = value
      doubleSettingRepository.save(s)
    }

  }

  @PostConstruct
  def ensureSettingsConfigured(): Unit = {
    ensureSettingExist(DoubleSettingRepository.PAMPERS_WEIGHT_SETTING, DoubleSettingRepository.DEFAULT_PAMPERS_WEIGHT)
    ensureSettingExist(DoubleSettingRepository.PAMPERS_COUNTER_SETTING, DoubleSettingRepository.DEFAULT_PAMPERS_COUNT)
  }

}
