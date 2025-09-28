package cz.ivosahlik.ecommerce.payload;

import lombok.Data;

@Data
public class OrderStatusUpdateDto {
    private String status;
}
