package hlt.name.site

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import hlt.name.site.dal.{DoubleSettingRepository, InputRepository, OutputAggregateRepository, TemperatureRepository}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

import scala.collection.JavaConversions._

@Controller
class RootController {

  private val logger = LoggerFactory.getLogger(getClass)

  @Autowired
  private var outputAggregateRepository: OutputAggregateRepository = _

  @Autowired
  private var inputRepository: InputRepository = _

  @Autowired
  private var doubleSettingRepository: DoubleSettingRepository = _

  @Autowired
  private var temperatureRepository: TemperatureRepository = _

  @GetMapping(value = Array("/"))
  def index(): ModelAndView = {
    val result = new ModelAndView("index")

    val today = LocalDate.now()
    val recentTemp = temperatureRepository.findTodayLastTemperature(LocalDate.now())
    val recentStats = outputAggregateRepository.aggregateOutputStats()
    val recentItem = recentStats.find(t => t.outputDate == today)
    val todayInputs = inputRepository.findTodayItems(LocalDate.now())
    val hasVigantol = todayInputs.exists(_.vigantol)
    val canStartFeeding = inputRepository.canStartFeeding
    val lowPampers = doubleSettingRepository.getPampersCount < 10
    result.addObject("todayOutput", recentItem.orNull)
    result.addObject("hasVigantol", hasVigantol)
    result.addObject("hasTemperature", recentTemp.isPresent)
    result.addObject("canStartFeeding", canStartFeeding)
    result.addObject("canStopFeeding", !canStartFeeding)
    result.addObject("lowPampers", lowPampers)
    val vigantolMessage = if (hasVigantol) {
      "YES"
    } else {
      val lastVigantol = inputRepository.findLastVigantol()
      if (lastVigantol.isPresent) {
        val t = lastVigantol.get().startTime
        val tm = t.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"))
        s"No, last time it was at $tm"
      } else {
        "never?"
      }
    }

    val temperatureMessage = if (recentTemp.isPresent) {
      recentTemp.get.temperature.toString
    } else {
      "Not measured yet!"
    }

    result.addObject("temperatureMessage", temperatureMessage)
    result.addObject("vigantolMessage", vigantolMessage)
    result
  }
}
