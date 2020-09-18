package name.hlt.data.indexer.dal

import javax.persistence._

import scala.beans.BeanProperty

@Entity
class DALScanRootItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Int = _

  @Column(name = "root", length = 1024, unique = true)
  @BeanProperty
  var root: String = _

}
