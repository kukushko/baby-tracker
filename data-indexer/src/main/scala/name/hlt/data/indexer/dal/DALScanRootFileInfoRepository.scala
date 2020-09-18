package name.hlt.data.indexer.dal

import org.springframework.data.repository.CrudRepository

trait DALScanRootFileInfoRepository extends CrudRepository[DALScanRootFileInfo, Integer] {

  def findAllByRoot(root: DALScanRootItem): java.util.List[DALScanRootFileInfo]
}
