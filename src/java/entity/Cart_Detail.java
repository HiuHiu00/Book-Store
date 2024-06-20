package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Cart_Detail {
    private int CartDetailID;
    private int Quantity;
    private int CartID;
    private int BookID;
}
