package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Genre {
    private int GenreID;
    private String Genre;
    private int BookID;
    
    private String genreImageUrl;
}
