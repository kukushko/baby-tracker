package hlt.name.site.dal

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

trait OutputRepository extends CrudRepository[DALMaxOutput, Integer] {

  def findByOrderByOutputTimeDesc(page: Pageable): java.util.List[DALMaxOutput]
}
