package name.hlt.data.indexer

import com.beust.jcommander.JCommander
import name.hlt.data.indexer.dal.{DALScanRootFileInfoRepository, DALScanRootItemRepository}
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.{Bean, Configuration}

import scala.collection.JavaConversions._

@Configuration
@EnableAutoConfiguration
class Config {

  @Bean
  def baseParameters: BaseParameters = new BaseParameters

  @Bean
  def commander(context: ApplicationContext, baseParameters: BaseParameters): JCommander = {
    val commander = JCommander.newBuilder()
      .addObject(baseParameters)

    for ((name, bean) <- context.getBeansOfType(classOf[Command])) {
      val p = bean.getClass.getAnnotation(classOf[CommandParameters])
      commander.addCommand(name, context.getBean(p.value()))
    }

    val r = commander.build()
    r.parse(Program.arguments: _*)
    r
  }

  @Bean
  def program(context: ApplicationContext, baseParameters: BaseParameters, commander: JCommander): Program =
    new Program(context, baseParameters, commander)

  @Bean
  def indexRootCommandParameters: IndexRootCommandParameters = new IndexRootCommandParameters

  @Bean(name = Array("index-root"))
  def indexRootCommand(fileInfoRepository: DALScanRootFileInfoRepository, rootRepository: DALScanRootItemRepository, indexRootCommandParameters: IndexRootCommandParameters): Command =
    new IndexRootCommand(fileInfoRepository, rootRepository, indexRootCommandParameters)
}
