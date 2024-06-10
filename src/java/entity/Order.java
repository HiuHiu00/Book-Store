package entity;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Order {
    private int OrderID;
    private Date PurchaseDate;
    private Double TotalPrice;
    private int OrderStatusID;
    private int AccountID;
    private int CartID;
    private String Recipent;
    private String DeliveryAddress;
}
