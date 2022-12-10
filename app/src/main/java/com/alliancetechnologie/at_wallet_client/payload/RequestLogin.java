package com.alliancetechnologie.at_wallet_client.payload;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class    RequestLogin {

    private String email;
    private String password;
}
