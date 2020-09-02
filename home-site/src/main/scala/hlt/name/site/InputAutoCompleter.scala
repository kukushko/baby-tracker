package hlt.name.site

import java.time.{Duration, LocalDateTime}

import hlt.name.site.dal.InputRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class InputAutoCompleter {

  private val logger = LoggerFactory.getLogger(getClass)
  @Autowired
  private var inputRepository: InputRepository = _

  @Scheduled(fixedRate = 5000)
  def stopInputs(): Unit = {
    try {
      inputRepository.findLatest match {
        case None =>
        case Some(x) =>
          if (x.endTime == null) {
            val duration = Duration.between(x.startTime, LocalDateTime.now())
            if (duration.toHours >= 1) {
              logger.warn("input is open too long, closing.")
              x.endTime = LocalDateTime.now()
              x.comment = "Auto-stopped"
              inputRepository.save(x)
            }
          }
      }
    }
    catch {
      case x: Throwable =>
        logger.error("Error during input auto-close", x)
    }

  }

}
