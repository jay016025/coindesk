package idv.jay.coindesk.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CURRENCY")
public class JpaCurrency {
  @Id
  @Column(name = "CODE")
  private String code;
  @Column(name = "CURRENCY_NAME")
  private String currencyName;
}

