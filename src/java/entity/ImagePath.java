package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ImagePath {
    private int ImagePathID;
    private String ImagePath;
    private int BookID;
}
