package idv.jay.coindesk.repository;

import idv.jay.coindesk.repository.entity.JpaCurrency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCurrencyRepository extends CrudRepository<JpaCurrency, String> {}
