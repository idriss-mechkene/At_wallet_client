package com.alliancetechnologie.at_wallet_client.payload;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class RequestUpdatePassword {

    private Long userId;
    private String lastPassword;
    private String newPassword;
    private boolean focerupdate;
}
