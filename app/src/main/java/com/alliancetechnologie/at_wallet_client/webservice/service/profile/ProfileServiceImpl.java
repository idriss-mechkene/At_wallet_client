package com.alliancetechnologie.at_wallet_client.webservice.service.profile;

import android.content.Context;
import android.util.Log;

import com.alliancetechnologie.at_wallet_client.R;
import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestAlimentationAccount;
import com.alliancetechnologie.at_wallet_client.payload.RequestUpdatePassword;
import com.alliancetechnologie.at_wallet_client.payload.RequestUpdateProfile;
import com.alliancetechnologie.at_wallet_client.payload.ResponseAlimentationAccount;

import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public class ProfileServiceImpl {
    private final ProfileService profileService;
    private final ProfileServiceInterface profileServiceInterface;
    Context context;

    public ProfileServiceImpl(Context context, ProfileServiceInterface profileServiceInterface) {
        this.profileServiceInterface = profileServiceInterface;
        this.profileService = new ProfileService();
        this.context = context;
    }

    public void updateProfile(String token, RequestUpdateProfile requestUpdateProfile) {
        profileService.updateProfile(token, requestUpdateProfile)
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull User user) {
                        Log.d("TAG", "onSuccess: " + user.toString());
                        profileServiceInterface.onSuccessUpdateProfile(user);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        profileServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 401:
                                        profileServiceInterface.onError("Utilisateur non autorisé", error.code());
                                        break;
                                    case 500:
                                        profileServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    default:
                                        profileServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                profileServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            profileServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }

    public void updatePassword(String token, RequestUpdatePassword requestUpdatePassword) {
        profileService.updatePassword(token, requestUpdatePassword)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "onComplete: ");
                        profileServiceInterface.onSuccessUpdatePassword();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        profileServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 401:
                                        profileServiceInterface.onError("Utilisateur non autorisé", error.code());
                                        break;
                                    case 500:
                                        profileServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    default:
                                        profileServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                profileServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            profileServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }

    public void alimentationAccount(String token, RequestAlimentationAccount requestAlimentationAccount) {
        profileService.alimentationAccount(token, requestAlimentationAccount)
                .subscribe(new SingleObserver<ResponseAlimentationAccount>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ResponseAlimentationAccount responseAlimentationAccount) {
                        Log.e("TAG", "onSuccess: "+responseAlimentationAccount.toString() );
                        profileServiceInterface.onSuccessAlimenterAccount(responseAlimentationAccount);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        profileServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 401:
                                        profileServiceInterface.onError("Utilisateur non autorisé", error.code());
                                        break;
                                    case 500:
                                        profileServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    default:
                                        profileServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                profileServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            profileServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }
}
