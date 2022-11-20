package idv.jay.coindesk.repository.impl;

import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.repository.CurrencyRepository;
import idv.jay.coindesk.repository.JpaCurrencyRepository;
import idv.jay.coindesk.repository.entity.JpaCurrency;
import idv.jay.coindesk.share.mapper.CurrencyMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaCurrencyRepositoryImpl implements CurrencyRepository {

  @Autowired
  private JpaCurrencyRepository repository;

  @Override
  public void createCurrency(Currency currency) {
    JpaCurrency entity = 
        CurrencyMapper.INSTANCE.mapToJpaEntity(currency);
    repository.save(entity);
  }

  @Override
  public Optional<Currency> findById(String code) {
    Optional<JpaCurrency> entity = repository.findById(code);
    if(entity.isPresent()){
      Currency currency = CurrencyMapper.INSTANCE.mapToCurrency(entity.get());
      return Optional.of(currency);
    }
    return Optional.empty();
  }

  @Override
  public Currency updateCurrency(Currency currency) {
    JpaCurrency jpaCurrency = CurrencyMapper.INSTANCE.mapToJpaEntity(currency);
    JpaCurrency entity = repository.save(jpaCurrency);
    return CurrencyMapper.INSTANCE.mapToCurrency(entity);
  }

  @Override
  public void deleteById(String code) {
    repository.deleteById(code);
  }
}
