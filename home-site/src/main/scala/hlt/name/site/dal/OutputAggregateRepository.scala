package hlt.name.site.dal

import java.time.LocalDate

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

trait OutputAggregateRepository extends Repository[DALOutputAggregate, LocalDate]{

  @Query(value = "select\n  date(output_time) as output_date,\n  sum(weight) as total_weight\nFROM dalmax_output\ngroup by output_date\norder by output_date desc\nLIMIT 10", nativeQuery = true)
  def aggregateOutputStats(): java.util.List[DALOutputAggregate]

}
