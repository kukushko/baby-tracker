package hlt.name.site.dal

import java.time.LocalDateTime

import javax.persistence._

import scala.beans.BeanProperty

@Entity
class DALTemperature {

  @Id
  @BeanProperty
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Int = _

  @Column(nullable = false)
  @BeanProperty
  var temperatureTime: LocalDateTime = _

  @Column(nullable = false)
  @BeanProperty
  var temperature: Double = _

  @Column
  @BeanProperty
  var comment: String = _
}
