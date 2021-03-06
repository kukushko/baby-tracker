package hlt.name.site

import java.time.LocalDateTime
import java.util.Optional

import hlt.name.site.dal.{DALMaxOutput, DoubleSettingRepository, OutputAggregateRepository, OutputRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{PageRequest, Sort}
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping(value = Array("/outputs"))
class OutputController {

  private val PAGE_SIZE = 10
  private val OUTPUT_LIST_REDIRECT = "redirect:/outputs/list"
  private val log = org.slf4j.LoggerFactory.getLogger(getClass)

  @Autowired
  private var outputRepository: OutputRepository = _

  @Autowired
  private var outputAggregateRepository: OutputAggregateRepository = _

  @Autowired
  private var doubleSettingsRepository: DoubleSettingRepository = _

  @GetMapping(value = Array("/addForm"))
  def addForm(): ModelAndView = {
    val result = new ModelAndView("outputs/addForm")
    result.addObject("defaultWipeCount", doubleSettingsRepository.getWipesPerOutput.toInt)
    result
  }

  @GetMapping(value = Array("/editForm/{id}"))
  def editForm(@PathVariable id: Int): ModelAndView =  {
    val item = outputRepository.findById(id)
    if (!item.isPresent) {
      throw new ResourceNotFoundException(s"output with id $id not found")
    }

    val r = item.get()
    val result = new ModelAndView("outputs/editForm")

    result.addObject("item", r)
    result
  }

  @PostMapping(value = Array("/add"))
  def add(@RequestParam(required = false) hardOutput: Boolean,
          @RequestParam(required = false) softOutput: Boolean,
          @RequestParam(required = false) comment: String,
          @RequestParam(required = false) wipeCount: Int,
          @RequestParam(required = false) totalWeight: Double): String = {
    val pampersWeight = doubleSettingsRepository.getPampersWeight
    val item = new DALMaxOutput
    item.outputTime = LocalDateTime.now()
    item.softOutput = softOutput
    item.hardOutput = hardOutput
    item.comment = if (comment == null) "" else comment
    item.weight = totalWeight - pampersWeight
    item.pampersWeight = pampersWeight
    item.wipeCount = wipeCount
    outputRepository.save(item)
    doubleSettingsRepository.decrementPampersCount()
    doubleSettingsRepository.decrementWipeCount(wipeCount)
    OUTPUT_LIST_REDIRECT
  }

  @PostMapping(value = Array("/update"))
  def update(@RequestParam id: Int,
             @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
             @RequestParam(required = false) outputTime: LocalDateTime,
             @RequestParam(required = false) hardOutput: Boolean,
             @RequestParam(required = false) softOutput: Boolean,
             @RequestParam(required = false) comment: String,
             @RequestParam(required = false) weight: Double,
             @RequestParam(required = false) wipeCount: Int,
             @RequestParam(required = false) pampersWeight: Double): String = {
    val t = outputRepository.findById(id)
    if (!t.isPresent) {
      throw new ResourceNotFoundException(s"output with id $id not found")
    }
    val item = t.get()
    item.softOutput = softOutput
    item.hardOutput = hardOutput
    item.outputTime = outputTime
    item.comment = if (comment == null) "" else comment
    item.weight = weight
    val wipeDelta = wipeCount-item.wipeCount
    item.wipeCount = wipeCount
    item.pampersWeight = pampersWeight
    outputRepository.save(item)
    doubleSettingsRepository.decrementWipeCount(wipeDelta)
    OUTPUT_LIST_REDIRECT
  }

  @PostMapping(value = Array("delete"))
  def delete(@RequestParam id: Int): String = {
    val t = outputRepository.findById(id)
    if (t.isPresent) {
      val item = t.get()
      // return wipes back
      doubleSettingsRepository.decrementWipeCount(-item.wipeCount)
      // return pampers back
      doubleSettingsRepository.incrementPampersCount()
      // save
      outputRepository.deleteById(id)
    }

    OUTPUT_LIST_REDIRECT
  }

  @GetMapping(value = Array("/dailyStats"))
  def dailyStats(): ModelAndView = {
    val items = outputAggregateRepository.aggregateOutputStats()
    val result = new ModelAndView("outputs/dailyStats")
    result.addObject("outputs", items)
    result
  }

  @GetMapping(value = Array("/list"))
  def index(@RequestParam(required = false) page: Integer): ModelAndView = {
    val result = new ModelAndView("outputs/list")

    val pageReq = PageRequest.of(page, PAGE_SIZE)
    val outputs = outputRepository.findByOrderByOutputTimeDesc(pageReq)
    val count = outputRepository.count()
    var pageCount = count / PAGE_SIZE
    if (count % PAGE_SIZE > 0) {
      pageCount += 1
    }
    result.addObject("outputs", outputs)
    result.addObject("hasMorePages", page + 1 < pageCount)
    result.addObject("hasLessPages", page > 0)
    result.addObject("nextPage", page + 1)
    result.addObject("prevPage", page - 1)
    result
  }

}
