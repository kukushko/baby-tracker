package hlt.name.site

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import hlt.name.site.dal.{DALInput, InputRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping(value = Array("/inputs"))
class InputController {

  private val PAGE_SIZE = 10
  private val INPUT_LIST_REDIRECT = "redirect:/inputs/list"

  @Autowired
  private var inputRepository: InputRepository = _

  @GetMapping(value = Array("/start"))
  def start(): String = {
    val canStart = inputRepository.canStartFeeding
    if (!canStart) {
      throw new Exception("cannot start new feeding, current is still in progress")
    }
    val item = new DALInput
    item.startTime = LocalDateTime.now()
    inputRepository.save(item)
    INPUT_LIST_REDIRECT
  }

  @PostMapping(value = Array("/delete"))
  def delete(@RequestParam id: Int): String = {
    inputRepository.deleteById(id)
    INPUT_LIST_REDIRECT
  }

  @GetMapping(value = Array("/editForm/{id}"))
  def editForm(@PathVariable id: Int): ModelAndView = {
    val result = new ModelAndView("inputs/editForm")
    val item = inputRepository.findById(id)
    if (!item.isPresent)
      throw new ResourceNotFoundException(s"input with id $id not found")
    result.addObject("item", item.get())
    result
  }

  @PostMapping(value = Array("/update"))
  def update(@RequestParam id: Int,
             @RequestParam(required = false)
             @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
             startTime: LocalDateTime,
             @RequestParam(required = false) comment: String,
             @RequestParam(required = false) vigantol: Boolean,
             @RequestParam(required = false) minutes: Int): String = {
    val item = inputRepository.findById(id)
    if (!item.isPresent)
      throw new ResourceNotFoundException(s"input with id $id not found")
    val x = item.get()
    if (startTime != null) {
      x.startTime = startTime
    }
    x.endTime = x.startTime.plusMinutes(minutes)
    x.comment = comment
    x.vigantol = vigantol
    inputRepository.save(x)

    INPUT_LIST_REDIRECT
  }

  @GetMapping(value = Array("/stop"))
  def stop(): String = {
    val latest = inputRepository.findLatest
    latest match {
      case None =>
        throw new Exception("cannot stop - feeding not started")
      case Some(x) =>
        if (x.endTime == null) {
          x.endTime = LocalDateTime.now()
          inputRepository.save(x)
        }
    }

    INPUT_LIST_REDIRECT
  }

  @GetMapping(value = Array("/list"))
  def index(@RequestParam(required = false) page: Integer): ModelAndView = {
    val result = new ModelAndView("inputs/list")
    val pageReq = PageRequest.of(page, PAGE_SIZE)
    val inputs = inputRepository.findByOrderByStartTimeDesc(pageReq)
    val count = inputRepository.count()
    var pageCount = count / PAGE_SIZE
    if (count % PAGE_SIZE > 0) {
      pageCount += 1
    }
    val latest = inputRepository.findLatest
    val canStart = latest match {
      case None => true
      case Some(x) => x.endTime != null
    }
    result.addObject("inputs", inputs)
    result.addObject("hasMorePages", page + 1 < pageCount)
    result.addObject("hasLessPages", page > 0)
    result.addObject("canStart", canStart)
    result.addObject("canStop", !canStart)
    result.addObject("nextPage", page + 1)
    result.addObject("prevPage", page - 1)
    result
  }
}
