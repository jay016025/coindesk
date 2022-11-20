package idv.jay.coindesk.core.repository;

import idv.jay.coindesk.core.entity.Currency;
import java.util.Optional;

public interface CurrencyRepository {

  /*
    新增幣別
   */
  void createCurrency(Currency currency);

  /*
    幣別查詢(單筆)
   */
  Optional<Currency> findById(String code);
  
  /*
    修改幣別
   */

  Currency updateCurrency(Currency currency);
  /*
    刪除幣別
   */

  void deleteById(String code);
}
