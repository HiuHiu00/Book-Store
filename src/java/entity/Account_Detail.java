package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Account_Detail {
    private int AccountDetailID;
    private String Username;
    private String Address;
    private boolean Gender;
    private String PhoneNumber;
    private String ImagePath;
    private int AccountID;
}
