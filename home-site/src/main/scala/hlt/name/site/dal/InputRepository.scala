package hlt.name.site.dal

import java.time.LocalDate
import java.util.Optional

import org.springframework.data.domain.{PageRequest, Pageable}
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

trait InputRepository extends CrudRepository[DALInput, Integer] {

  @Query(nativeQuery = true, value = "select * from dalinput where date(start_time)=:d")
  def findTodayItems(@Param(value = "d") date: LocalDate): java.util.List[DALInput]

  @Query(nativeQuery = true, value = "select * from dalinput where vigantol=1 order by start_time desc limit 1")
  def findLastVigantol(): Optional[DALInput]

  def findByOrderByStartTimeDesc(page: Pageable): java.util.List[DALInput]

  def findLatest: Option[DALInput] = {
    val pr = PageRequest.of(0, 1)
    val items = findByOrderByStartTimeDesc(pr)
    if (items.isEmpty)
      None
    else
      Some(items.get(0))
  }

  def canStartFeeding: Boolean = {
    val latest = findLatest
    latest match {
      case None => true
      case Some(x) => x.endTime != null
    }
  }
}
