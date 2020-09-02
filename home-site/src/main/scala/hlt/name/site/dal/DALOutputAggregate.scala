package hlt.name.site.dal

import java.time.LocalDate

import javax.persistence.{Column, Entity, Id}

import scala.beans.BeanProperty

@Entity
class DALOutputAggregate {

  @Id
  @BeanProperty
  var outputDate: LocalDate = _

  @Column(nullable = false)
  @BeanProperty
  var totalWeight: Double = _
}
