package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Publisher {
    private int PublisherID;
    private String PublisherName;
    private String PublisherImagePath;
}
