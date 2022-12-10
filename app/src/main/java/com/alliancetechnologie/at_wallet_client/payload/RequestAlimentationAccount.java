package com.alliancetechnologie.at_wallet_client.payload;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class RequestAlimentationAccount {

    private Long accountId;
    private String amount;
}
