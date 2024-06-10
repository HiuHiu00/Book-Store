package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Cart {
    private int CartID;
    private int AccountID;
    private int ShippingMethodID;
}
