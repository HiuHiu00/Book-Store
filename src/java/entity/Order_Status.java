package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Order_Status {
    private int OrderStatusID;
    private String Status;
}
