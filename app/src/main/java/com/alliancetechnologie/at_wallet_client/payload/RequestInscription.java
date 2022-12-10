package com.alliancetechnologie.at_wallet_client.payload;

import lombok.Data;

@Data
public class RequestInscription {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String identityCard;
}
