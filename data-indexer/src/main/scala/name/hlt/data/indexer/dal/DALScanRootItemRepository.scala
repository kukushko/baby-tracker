package name.hlt.data.indexer.dal

import java.util.Optional

import org.springframework.data.repository.CrudRepository

trait DALScanRootItemRepository extends CrudRepository[DALScanRootItem, Integer] {

  def findByRoot(root: String): Optional[DALScanRootItem]

}
