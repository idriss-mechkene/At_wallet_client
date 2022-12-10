package com.alliancetechnologie.at_wallet_client.payload;

import lombok.Data;

@Data
public class RequestRestPassword {

    private String email;
    private String password;
    private String confirmPassword;
}
