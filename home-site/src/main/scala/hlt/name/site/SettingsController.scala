package hlt.name.site

import hlt.name.site.dal.DoubleSettingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping(value = Array("/settings"))
class SettingsController {

  @Autowired
  private var doubleSettingsRepository: DoubleSettingRepository = _

  @PostMapping(value = Array("/update"))
  def update(@RequestParam id: Int, @RequestParam settingType: String, @RequestParam value: String): String = {
    assert(settingType == "double", "sorry, only double settings are supported")
    val item = doubleSettingsRepository.findById(id)
    if (!item.isPresent) {
      throw new ResourceNotFoundException(s"setting with ID $id not found")
    }

    val x = item.get()
    x.value = value.toDouble
    doubleSettingsRepository.save(x)
    "redirect:/settings/list"
  }

  @GetMapping(value = Array("/editForm/{settingType}/{id}"))
  def editForm(@PathVariable settingType: String, @PathVariable id: Int): ModelAndView = {
    assert(settingType == "double", "sorry, other types are not supported")
    val result = new ModelAndView("settings/editForm")
    val item = doubleSettingsRepository.findById(id)
    if (!item.isPresent) {
      throw new ResourceNotFoundException(s"cannot find setting with ID $id")
    }

    result.addObject("item", item.get())

    result
  }

  @GetMapping(value = Array("/list"))
  def index(): ModelAndView = {
    val doubleItems = doubleSettingsRepository.findAll()
    val result = new ModelAndView("settings/list")
    result.addObject("doubleItems", doubleItems)
    result
  }

}
