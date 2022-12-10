package com.alliancetechnologie.at_wallet_client.payload;

import lombok.Data;

@Data
public class ResponseDetailsQR {

    private String montant;
    private String prestataire;
    private String refpaiement;
    private String ville;
}
