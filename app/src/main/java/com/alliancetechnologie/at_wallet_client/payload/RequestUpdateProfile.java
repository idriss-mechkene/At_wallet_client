package com.alliancetechnologie.at_wallet_client.payload;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class RequestUpdateProfile {

    private Long accountId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
