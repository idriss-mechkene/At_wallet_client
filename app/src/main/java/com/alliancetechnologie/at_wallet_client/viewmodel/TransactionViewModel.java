package com.alliancetechnologie.at_wallet_client.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.webservice.service.transaction.TransactionServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.transaction.TransactionServiceInterface;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private final TransactionServiceImpl transactionService;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        this.transactionService = new TransactionServiceImpl();
    }

    public void setTransactionServiceInterface(TransactionServiceInterface transactionServiceInterface) {
        this.transactionService.setTransactionServiceInterface(transactionServiceInterface);
    }

    public LiveData<List<Transactions>> getLast5Transaction(String token, Long accountId) {
        return transactionService.GetLast5Transaction(token, accountId);
    }

    public LiveData<List<Transactions>> getTransactions(String token, Long accountId) {
        return transactionService.GetTransactions(token, accountId);
    }
}
