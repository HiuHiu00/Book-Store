package entity;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Book {
    private int BookID;
    private String Title;
    private String ISBN13;
    private Date Publication_date;
    private int PublisherID;
    private int Stock;
    private double Price;
    private String Description;
    private int AuthorID;
    private int DiscountID;
}
