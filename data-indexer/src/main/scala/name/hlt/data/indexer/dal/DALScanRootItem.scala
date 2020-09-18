package name.hlt.data.indexer.dal

import javax.persistence._

import scala.beans.BeanProperty

@Entity
class DALScanRootItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Int = _

  @UniqueConstraint(columnNames = Array("root"))
  @Column(name = "root", length = 1024)
  @BeanProperty
  var root: String = _

}
