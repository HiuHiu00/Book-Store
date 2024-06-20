package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Shipping_Method {
    private int ShippingMethodID;
    private String Method;
    private Double Cost;
}
