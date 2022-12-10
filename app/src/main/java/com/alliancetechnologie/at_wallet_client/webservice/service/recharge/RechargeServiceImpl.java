package com.alliancetechnologie.at_wallet_client.webservice.service.recharge;

import android.content.Context;
import android.util.Log;

import com.alliancetechnologie.at_wallet_client.R;
import com.alliancetechnologie.at_wallet_client.entity.Operator;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.RequestTopUp;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import retrofit2.HttpException;

public class RechargeServiceImpl {

    private final RechargeService rechargeService;
    private final RechargeServiceInterface rechargeServiceInterface;
    Disposable disposable;
    Context context;

    public RechargeServiceImpl(Context context, RechargeServiceInterface rechargeServiceInterface) {
        this.rechargeServiceInterface = rechargeServiceInterface;
        this.rechargeService = new RechargeService();
        this.context = context;
    }

    public void getOperator(String token) {
        disposable = rechargeService.getOperator(token)
                .subscribeWith(new DisposableObserver<List<Operator>>() {
                    @Override
                    public void onNext(@NonNull List<Operator> operators) {
                        Log.d("TAG", "onNext: " + operators.size());
                        rechargeServiceInterface.onSuccessListOperator(operators);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        rechargeServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 401:
                                        rechargeServiceInterface.onError("Utilisateur non autorisé", error.code());
                                        break;
                                    case 500:
                                        rechargeServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    default:
                                        rechargeServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                rechargeServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            rechargeServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "onComplete: ");
                    }
                });
    }

    public void RechargeTopUp(String token, RequestTopUp requestTopUp) {
        rechargeService.rechargeTopUp(token, requestTopUp)
                .subscribe(new SingleObserver<Transactions>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Transactions transactions) {
                        Log.e("TAG", "onSuccess: " + transactions.toString());
                        rechargeServiceInterface.onSuccessTopUp(transactions);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        rechargeServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 401:
                                        rechargeServiceInterface.onError("Utilisateur non autorisé", error.code());
                                        break;
                                    case 500:
                                        rechargeServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    case 900:
                                        rechargeServiceInterface.onError("Solde Insuffisant", error.code());
                                        break;
                                    case 901:
                                        rechargeServiceInterface.onError("Montant Invalide", error.code());
                                        break;
                                    case 902:
                                        rechargeServiceInterface.onError("Erreur Service Sourcing", error.code());
                                        break;
                                    case 903:
                                        rechargeServiceInterface.onError("Problème Technique", error.code());
                                    case 904:
                                        rechargeServiceInterface.onError("Opérateur Invalide", error.code());
                                        break;
                                    default:
                                        rechargeServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                rechargeServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            rechargeServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }
}
