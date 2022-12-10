package com.alliancetechnologie.at_wallet_client.webservice.service.auth;

import android.content.Context;
import android.util.Log;

import com.alliancetechnologie.at_wallet_client.R;
import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestInscription;
import com.alliancetechnologie.at_wallet_client.payload.RequestLogin;
import com.alliancetechnologie.at_wallet_client.payload.RequestRestPassword;
import com.alliancetechnologie.at_wallet_client.payload.ResponseVerification;

import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public class AuthServiceImpl {

    private final AuthService authService;
    private final AuthServiceInterface authServiceInterface;
    private final Context context;

    public AuthServiceImpl(Context context, AuthServiceInterface authServiceInterface) {
        this.authServiceInterface = authServiceInterface;
        this.authService = new AuthService();
        this.context = context;
    }

    public void Login(RequestLogin requestLogin) {
        authService.login(requestLogin)
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull User user) {
                        Log.d("TAG", "onSuccess: " + user.toString());
                        authServiceInterface.onSuccessLogin(user);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        authServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 404:
                                        authServiceInterface.onError("Email et/ou mot de passe incorrect(s)", error.code());
                                        break;
                                    case 500:
                                        authServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    case 800:
                                        authServiceInterface.onError("Le numéro de carte d'identité est déjà utilisé", error.code());
                                        break;
                                    case 939:
                                        authServiceInterface.onError("Cet e-mail est déjà existé avec un autre utilisateur.", error.code());
                                        break;
                                    case 940:
                                        authServiceInterface.onError("Erreur au niveau de l'envoi d'email", error.code());
                                        break;
                                    case 946:
                                        authServiceInterface.onError("Email Invalid", error.code());
                                        break;
//                                    case 947:
//                                        authServiceInterface.onError("Le compte choisi existe avec un autre utilisateur.", error.code());
//                                        break;
                                    case 948:
                                        authServiceInterface.onError("Numéro de Téléphone est Invalide.", error.code());
                                        break;
                                    default:
                                        authServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }

    public void Registration(RequestInscription requestInscription) {
        authService.registration(requestInscription)
                .subscribe(new SingleObserver<ResponseVerification>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ResponseVerification responseVerification) {
                        Log.e("TAG", "onSuccess: " + responseVerification.toString());
                        authServiceInterface.onSuccessRegistration(responseVerification);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        authServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 500:
                                        authServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    case 939:
                                        authServiceInterface.onError("Cet e-mail est déjà existé avec un autre utilisateur.", error.code());
                                        break;
                                    case 940:
                                        authServiceInterface.onError("Erreur au niveau de l'envoi d'email", error.code());
                                        break;
                                    case 946:
                                        authServiceInterface.onError("Email Invalid", error.code());
                                        break;
                                    case 947:
                                        authServiceInterface.onError("Le CIN choisi existe Déjà.", error.code());
                                        break;
                                    case 948:
                                        authServiceInterface.onError("Numéro de Téléphone est Invalide.", error.code());
                                        break;
                                    default:
                                        authServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }

    public void VerifyRegistration(Long requestCode, String verificationCode) {
        authService.verificationRegistration(requestCode, verificationCode)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        authServiceInterface.onCompleteVerifyRegistration();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        authServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 500:
                                        authServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    case 940:
                                        authServiceInterface.onError("Erreur au niveau de l'envoi d'email", error.code());
                                        break;
                                    case 941:
                                        authServiceInterface.onError("Demande est déjà expirée.", error.code());
                                        break;
                                    case 942:
                                        authServiceInterface.onError("Numéro demande ou code verification est invalid.", error.code());
                                        break;
                                    case 949:
                                        authServiceInterface.onError("Demande déjà Valide", error.code());
                                        break;
                                    default:
                                        authServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }

    public void ForgetPassword(String email) {
        authService.forgetPassword(email)
                .subscribe(new SingleObserver<ResponseVerification>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ResponseVerification responseVerification) {
                        Log.e("TAG", "onSuccess: " + responseVerification.toString());
                        authServiceInterface.onSuccessForgetPassword(responseVerification);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        authServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 404:
                                        authServiceInterface.onError("Email et/ou mot de passe incorrect(s)", error.code());
                                        break;
                                    case 500:
                                        authServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    case 940:
                                        authServiceInterface.onError("Erreur au niveau de l'envoi d'email", error.code());
                                        break;
                                    case 943:
                                        authServiceInterface.onError("Informations non compatibles. Ou demande déjà valide", error.code());
                                        break;
                                    case 944:
                                        authServiceInterface.onError("Aucun utilisateur avec ce mail.", error.code());
                                        break;
                                    case 948:
                                        authServiceInterface.onError("Numéro de Téléphone est Invalide.", error.code());
                                        break;
                                    default:
                                        authServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }

    public void ResendVerificationCode(Long requestCode, String email) {
        authService.resendCode(requestCode, email)
                .subscribe(new SingleObserver<ResponseVerification>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ResponseVerification responseVerification) {
                        Log.e("TAG", "onSuccess: " + responseVerification.toString());
                        authServiceInterface.onSuccessResendCode(responseVerification);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        authServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 500:
                                        authServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    case 940:
                                        authServiceInterface.onError("Erreur au niveau de l'envoi d'email", error.code());
                                        break;
                                    case 941:
                                        authServiceInterface.onError("Demande est déjà expirée.", error.code());
                                        break;
                                    case 942:
                                        authServiceInterface.onError("Numéro demande ou code verification est invalid.", error.code());
                                        break;
                                    default:
                                        authServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }

    public void RestPassword(RequestRestPassword requestRestPassword) {
        authService.restPassword(requestRestPassword)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete: ");
                        authServiceInterface.onCompleteRestPassword();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                Log.e("TAG", "message: " + error.message() + "/ " + error.code());
                                switch (error.code()) {
                                    case 400:
                                        authServiceInterface.onError("Bad request", error.code());
                                        break;
                                    case 500:
                                        authServiceInterface.onError(context.getString(R.string.error_serveur), error.code());
                                        break;
                                    case 940:
                                        authServiceInterface.onError("Erreur au niveau de l'envoi d'email", error.code());
                                        break;
                                    case 942:
                                        authServiceInterface.onError("Numéro demande ou code verification est invalid.", error.code());
                                        break;
                                    default:
                                        authServiceInterface.onError(Objects.requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string(), error.code());
                                }
                            } else {
                                authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                                Log.e("TAG", "throwable: " + "/ " + e.getMessage());
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.e("TAG", "onError: " + "/ " + e.getMessage());
                            authServiceInterface.onError(context.getString(R.string.error_connexion), 0);
                        }
                    }
                });
    }
}
