package com.alliancetechnologie.at_wallet_client.webservice.service.payement;

import android.content.Context;
import android.util.Log;

import com.alliancetechnologie.at_wallet_client.R;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.RequestPayment;
import com.alliancetechnologie.at_wallet_client.payload.ResponseDetailsQR;

import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public class PaymentServiceImp {

    private final PaymentService paymentService;
    private final PaymentServiceInterface paymentServiceInterface;
    Context context;

    public PaymentServiceImp(Context context, PaymentServiceInterface paymentServiceInterface) {
        this.paymentServiceInterface = paymentServiceInterface;
        this.paymentService = new PaymentService();
        this.context = context;
    }


    public void PaymentQr(String token, RequestPayment requestPayment) {
        paymentService.payment(token, requestPayment)
                .subscribe(new SingleObserver<Transactions>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Transactions transactions) {
                        Log.d("TAG", "onSuccess: " + transactions.toString());
                        paymentServiceInterface.onSuccessPayment(transactions);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        paymentServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 401:
                                        paymentServiceInterface.onError("Utilisateur non autorisé", error.code());
                                        break;
                                    case 500:
                                        paymentServiceInterface.onError(context.getString(R.string.error_connexion), error.code());
                                        break;
                                    case 900:
                                        paymentServiceInterface.onError("Solde Insuffisant", error.code());
                                        break;
                                    case 901:
                                        paymentServiceInterface.onError("QR Code n'est pas valide", error.code());
                                        break;
                                    case 950:
                                        paymentServiceInterface.onError("Mode Paiement incorrect", error.code());
                                        break;
                                    case 951:
                                        paymentServiceInterface.onError("Transaction déjà effectuer", error.code());
                                        break;
                                    case 952:
                                        paymentServiceInterface.onError("QR Code N'existe pas", error.code());
                                        break;
                                    case 953:
                                        paymentServiceInterface.onError("REF N'existe pas", error.code());
                                        break;
                                    default:
                                        paymentServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                paymentServiceInterface.onError(context.getString(R.string.error_serveur), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            paymentServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }

    public void detailsQR(String token, RequestPayment requestPayment) {
        paymentService.payment_detail_QR(token, requestPayment)
                .subscribe(new SingleObserver<ResponseDetailsQR>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ResponseDetailsQR responseDetailsQR) {
                        Log.d("TAG", "onSuccess: " + responseDetailsQR.toString());
                        paymentServiceInterface.onSuccessDetailQR(responseDetailsQR);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        paymentServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 401:
                                        paymentServiceInterface.onError("Utilisateur non autorisé", error.code());
                                        break;
                                    case 500:
                                        paymentServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    case 901:
                                        paymentServiceInterface.onError("QR Code n'est pas valide", error.code());
                                        break;
                                    case 950:
                                        paymentServiceInterface.onError("Mode Paiement incorrect", error.code());
                                        break;
                                    default:
                                        paymentServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                paymentServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            paymentServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }
}
