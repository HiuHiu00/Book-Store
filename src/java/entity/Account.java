package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Account {
    private int AccountID;
    private String Password;
    private String Email;
    private int RoleID;
}