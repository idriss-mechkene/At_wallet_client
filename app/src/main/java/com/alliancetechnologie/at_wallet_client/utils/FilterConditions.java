package com.alliancetechnologie.at_wallet_client.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.ETransactionType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import lombok.Data;

@Data
public class FilterConditions {
    String minDate;
    String maxDate;
    Long minPrice;
    Long maxPrice;
    ETransactionType eTransactionType;

    @SuppressLint("SimpleDateFormat")
    public static boolean TransactionInConditions(Transactions transactions, FilterConditions filterConditions) {
        boolean test = true;
        if (filterConditions.eTransactionType != null && !filterConditions.eTransactionType.equals(transactions.getTransactionType())) {
            test = false;
        }
        if (filterConditions.minPrice != null && filterConditions.minPrice > Long.parseLong(transactions.getTransactionAmount())) {
            test = false;
        }
        if (filterConditions.maxPrice != null && filterConditions.maxPrice < Long.parseLong(transactions.getTransactionAmount())) {
            test = false;
        }
        try {
            if (filterConditions.minDate != null &&
                    Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(filterConditions.minDate))
                            .compareTo(new SimpleDateFormat("dd/MM/yyyy").parse(transactions.getTransactionDate()))
                            > 0) {
                test = false;

            }
            if (filterConditions.maxDate != null &&
                    Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(filterConditions.maxDate))
                            .compareTo(new SimpleDateFormat("dd/MM/yyyy").parse(transactions.getTransactionDate()))
                            < 0) {
                test = false;
            }

        } catch (ParseException ex) {
            Log.d("TAG", "TransactionInConditions: " + ex);
        }

        return test;
    }

}
