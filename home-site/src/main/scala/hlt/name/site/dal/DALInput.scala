package hlt.name.site.dal

import java.time.{Duration, LocalDateTime, Period}

import javax.persistence._

import scala.beans.BeanProperty

@Entity
class DALInput {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Int = _

  @Column(nullable = false)
  @BeanProperty
  var startTime: LocalDateTime = _

  @Column(nullable = true)
  @BeanProperty
  var endTime: LocalDateTime = _

  @Column
  @BeanProperty
  var vigantol: Boolean = _

  @Column(nullable = true)
  @BeanProperty
  var comment: String = _

  def getMinutes(): Int = {
    if (endTime == null)
      Duration.between(startTime, LocalDateTime.now()).toMinutes.toInt
    else {
      val p = Duration.between(startTime, endTime)
      p.toMinutes.toInt
    }
  }

  def minutes: Int = getMinutes()
}
