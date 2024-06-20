package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Discount {
    private int DiscountID;
    private Double DiscountPercent;
    private int DiscountAvailabel;
}
