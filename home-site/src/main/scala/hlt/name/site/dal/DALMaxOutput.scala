package hlt.name.site.dal

import java.time.LocalDateTime

import javax.persistence._

import scala.beans.BeanProperty

@Entity
class DALMaxOutput {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Int = _

  @Column(nullable = false)
  @BeanProperty
  var outputTime: LocalDateTime = _

  @Column(nullable = false)
  @BeanProperty
  var softOutput: Boolean = _

  @Column(nullable = false)
  @BeanProperty
  var hardOutput: Boolean = _

  @Column(nullable = false)
  @BeanProperty
  var weight: Double = _

  @Column(nullable = false)
  @BeanProperty
  var pampersWeight: Double = _

  @Column(nullable = true, length = 255)
  @BeanProperty
  var comment: String = _
}
