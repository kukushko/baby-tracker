package hlt.name.site.dal

import java.time.LocalDate
import java.util.Optional

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

trait TemperatureRepository extends CrudRepository[DALTemperature, Integer] {
  @Query(nativeQuery = true, value = "select * from daltemperature where date(temperature_time)=:d order by temperature_time desc limit 1")
  def findTodayLastTemperature(@Param(value = "d") date: LocalDate): Optional[DALTemperature]

  def findByOrderByTemperatureTimeDesc(page: Pageable): java.util.List[DALTemperature]
}
