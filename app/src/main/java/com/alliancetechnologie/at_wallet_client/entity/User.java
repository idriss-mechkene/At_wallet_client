package com.alliancetechnologie.at_wallet_client.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long accountId;
    private String accountNumber;
    private String accountBalance;
    private String access_token;
    private String token_type;
    private String expire_in;
    private boolean user_pwd_forced_update;
}
