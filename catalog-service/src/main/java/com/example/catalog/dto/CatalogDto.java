package com.example.catalog.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class CatalogDto implements Serializable {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;


}
