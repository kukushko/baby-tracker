package name.hlt.data.indexer

import name.hlt.data.indexer.dal.DALScanRootItemRepository
import org.slf4j.LoggerFactory

@CommandParameters(classOf[IndexRootCommandParameters])
class IndexRootCommand(rootRepository: DALScanRootItemRepository, parameters: IndexRootCommandParameters) extends Command {
  private val log = LoggerFactory.getLogger(getClass)
  override def execute(): Unit = {
    log.info("Command executed!")
  }
}
