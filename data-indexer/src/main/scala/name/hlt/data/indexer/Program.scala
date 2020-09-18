package name.hlt.data.indexer

import org.springframework.boot.{CommandLineRunner, SpringApplication}
import org.springframework.context.ApplicationContext
import com.beust.jcommander.JCommander
import org.slf4j.LoggerFactory

class Program(context: ApplicationContext, parameters: BaseParameters, commander: JCommander) extends CommandLineRunner {
  private val log = LoggerFactory.getLogger(getClass)
  override def run(args: String*): Unit = {
    if (parameters.help) {
      println(commander.usage())
    } else {
      val commandName = commander.getParsedCommand
      if (commandName == null) {
        throw new IllegalArgumentException("command not supplied, see usage")
      } else {
        log.info(s"Running command '$commandName'")
        val command = context.getBean(commandName, classOf[Command])
        command.execute()
        log.info(s"Command '$commandName' executed successfully")
      }
    }
  }
}

object Program {

  private var _arguments: Array[String] = _

  def arguments: Array[String] = _arguments

  def main(args: Array[String]): Unit = {
    _arguments = args
    SpringApplication.run(classOf[Config], args: _*)
  }
}
