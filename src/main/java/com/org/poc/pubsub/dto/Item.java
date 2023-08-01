package com.org.poc.pubsub.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Item implements Serializable {

  @Serial private static final long serialVersionUID = -8326484564085676974L;
  private String itemNumber;
  private String itemDescription;
  private String countryOfOrigin;
  private String skuId;
}
