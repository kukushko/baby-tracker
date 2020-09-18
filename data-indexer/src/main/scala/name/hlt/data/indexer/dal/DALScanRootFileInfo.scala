package name.hlt.data.indexer.dal

import java.time.LocalDateTime

import javax.persistence._

import scala.beans.BeanProperty

@Entity
@Table(
  uniqueConstraints = Array(new UniqueConstraint(columnNames = Array("root_id", "path")))
)
class DALScanRootFileInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Int = _

  @ManyToOne
  @JoinColumn(name = "root_id", nullable = false)
  @BeanProperty
  var root: DALScanRootItem = _

  @Column(nullable = false, length = 4096)//, columnDefinition = "varchar(4096) CHARACTER SET utf8 COLLATE utf8_unicode_ci not null")
  @BeanProperty
  var path: String = _

  @Column(nullable = false)
  @BeanProperty
  var createTime: LocalDateTime = _

  @Column(nullable = false)
  @BeanProperty
  var hash: String = _

  @Column(nullable = false)
  @BeanProperty
  var size: Long = _
}
