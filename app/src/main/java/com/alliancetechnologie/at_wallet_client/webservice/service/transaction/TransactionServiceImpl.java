package com.alliancetechnologie.at_wallet_client.webservice.service.transaction;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.alliancetechnologie.at_wallet_client.entity.Transactions;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import retrofit2.HttpException;

public class TransactionServiceImpl {

    private final MutableLiveData<List<Transactions>> mutableLiveData = new MutableLiveData<>();
    private final TransactionService transactionService;
    private TransactionServiceInterface transactionServiceInterface;
    Disposable disposable;

    public TransactionServiceImpl() {
        this.transactionService = new TransactionService();
    }

    public void setTransactionServiceInterface(TransactionServiceInterface transactionServiceInterface) {
        this.transactionServiceInterface = transactionServiceInterface;
    }

    public MutableLiveData<List<Transactions>> GetTransactions(String token, Long accountId) {
        disposable = transactionService.getTransactionByAccountId(token, accountId)
                .subscribeWith(new DisposableObserver<List<Transactions>>() {
                    @Override
                    public void onNext(@NonNull List<Transactions> transactions) {
//                        Log.d("TAG", "onSuccess: " + transactions.size());
                        mutableLiveData.setValue(transactions);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            try {
                                HttpException error = (HttpException) e;
                                //transactionServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                Log.d("TAG", "onError: " + "/ " + error.code());
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            }
                        } else {
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "onComplete: ");
                    }
                });
        return mutableLiveData;
    }

    public MutableLiveData<List<Transactions>> GetLast5Transaction(String token, Long accountId) {
        disposable = transactionService.get5LastTransactionByAccountId(token, accountId)
                .subscribeWith(new DisposableObserver<List<Transactions>>() {
                    @Override
                    public void onNext(@NonNull List<Transactions> transactions) {
//                        Log.d("TAG", "onSuccess: " + transactions.size());
                        mutableLiveData.setValue(transactions);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            try {
                                HttpException error = (HttpException) e;
                                //transactionServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                Log.d("TAG", "onError: " + "/ " + error.code());
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            }
                        } else {
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "onComplete: ");
                    }
                });
        return mutableLiveData;
    }
}
