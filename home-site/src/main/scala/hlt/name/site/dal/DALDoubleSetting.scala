package hlt.name.site.dal

import javax.persistence._

import scala.beans.BeanProperty

@Entity
class DALDoubleSetting {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Int = _

  @Column(nullable = false)
  @BeanProperty
  var name: String = _

  @Column(nullable = false)
  @BeanProperty
  var value: Double = _
}
