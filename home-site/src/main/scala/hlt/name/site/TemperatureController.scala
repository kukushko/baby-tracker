package hlt.name.site

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import hlt.name.site.dal.{DALTemperature, TemperatureRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping(value = Array("/temp"))
class TemperatureController {

  private val PAGE_SIZE = 10

  @Autowired
  private var temperatureRepository: TemperatureRepository = _

  @GetMapping(value = Array("/editForm/{id}"))
  def editForm(@PathVariable id: Int): ModelAndView =  {
    val item = temperatureRepository.findById(id)
    if (!item.isPresent) {
      throw new ResourceNotFoundException(s"temperature with id $id not found")
    }

    val r = item.get()
    val result = new ModelAndView("temp/editForm")

    result.addObject("item", r)
    result
  }

  @PostMapping(value = Array("/add"))
  def add(@RequestParam(required = false) temperature: Double,
          @RequestParam(required = false) comment: String): String = {
    val item = new DALTemperature
    item.temperatureTime = LocalDateTime.now()
    item.temperature = temperature
    item.comment = comment
    temperatureRepository.save(item)
    "redirect:/temp/list"
  }

  @PostMapping(value = Array("/update"))
  def update(@RequestParam id: Int,
             @RequestParam(required = false)
             @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
             temperatureTime: LocalDateTime,
             @RequestParam temperature: Double,
             @RequestParam(required = false) comment: String): String = {
    val t = temperatureRepository.findById(id)
    if (!t.isPresent) {
      throw new ResourceNotFoundException(s"temperature with id $id not found")
    }
    val item = t.get()
    if (temperatureTime != null) {
      item.temperatureTime = temperatureTime
    }
    item.temperature = temperature
    item.comment = comment
    temperatureRepository.save(item)
    "redirect:/temp/list"
  }

  @PostMapping(value = Array("delete"))
  def delete(@RequestParam id: Int): String = {
    temperatureRepository.deleteById(id)
    "redirect:/temp/list"
  }

  @GetMapping(value = Array("/list"))
  def index(@RequestParam(required = false) page: Integer): ModelAndView = {
    val result = new ModelAndView("temp/list")

    //    val testItem = new DALMaxOutput
    //    testItem.outputTime = LocalDateTime.now()
    //    testItem.softOutput = true
    //    testItem.comment = "test item"
    //    outputRepository.save(testItem)

    val pageReq = PageRequest.of(page, PAGE_SIZE)
    val temps = temperatureRepository.findByOrderByTemperatureTimeDesc(pageReq)
    val count = temperatureRepository.count()
    var pageCount = count / PAGE_SIZE
    if (count % PAGE_SIZE > 0) {
      pageCount += 1
    }
    result.addObject("items", temps)
    result.addObject("hasMorePages", page + 1 < pageCount)
    result.addObject("hasLessPages", page > 0)
    result.addObject("nextPage", page + 1)
    result.addObject("prevPage", page - 1)
    result
  }

  @GetMapping(value = Array("/addForm"))
  def addForm(): String = "temp/addForm"
}
