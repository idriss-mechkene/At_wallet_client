package com.alliancetechnologie.at_wallet_client.webservice.retrofit;

import com.alliancetechnologie.at_wallet_client.entity.Operator;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestAlimentationAccount;
import com.alliancetechnologie.at_wallet_client.payload.RequestInscription;
import com.alliancetechnologie.at_wallet_client.payload.RequestLogin;
import com.alliancetechnologie.at_wallet_client.payload.RequestPayment;
import com.alliancetechnologie.at_wallet_client.payload.RequestRestPassword;
import com.alliancetechnologie.at_wallet_client.payload.RequestTopUp;
import com.alliancetechnologie.at_wallet_client.payload.RequestUpdatePassword;
import com.alliancetechnologie.at_wallet_client.payload.RequestUpdateProfile;
import com.alliancetechnologie.at_wallet_client.payload.ResponseAlimentationAccount;
import com.alliancetechnologie.at_wallet_client.payload.ResponseDetailsQR;
import com.alliancetechnologie.at_wallet_client.payload.ResponseVerification;
import com.alliancetechnologie.at_wallet_client.utils.Constants;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface API {

    @Headers({
            "Accept: application/json"
    })

    //**********Autentification**********
    @POST("Login/Login")
    Single<User> login(@Body RequestLogin requestLogin);

    @POST("Login/Inscription")
    Single<ResponseVerification> registration(@Body RequestInscription requestInscription);

    @POST("Login/VerificationInscription")
    Completable verification_registration(@Query("requestNumber") Long requestNumber, @Query("verificationCode") String verificationCode);

    @POST("Login/MotDePasseOublier")
    Single<ResponseVerification> forget_password(@Query("email") String email);

    @POST("Login/RegenererCodeVerification")
    Single<ResponseVerification> resend_code(@Query("requestNumber") Long requestNumber, @Query("email") String email);

    @POST("Login/ResetPassword")
    Completable rest_password(@Body RequestRestPassword requestRestPassword);

    //**********Payment**********
    @POST("paymentQRCode")
    Single<Transactions> payment_QR(@Header(Constants.TOKEN_HEADER) String token, @Body RequestPayment requestPayment);

    @POST("DetailpaymentQRCode" )
    Single<ResponseDetailsQR> payment_detail_QR(@Header(Constants.TOKEN_HEADER) String token, @Body RequestPayment requestPayment);

    //**********Profile**********
    @PUT("UpdatePassword")
    Completable update_password(@Header(Constants.TOKEN_HEADER) String token, @Body RequestUpdatePassword requestUpdatePassword);

    @PUT("UpdateProfil ")
    Single<User> update_profile(@Header(Constants.TOKEN_HEADER) String token, @Body RequestUpdateProfile requestUpdateProfile);

    @PUT("AlimentationCompte")
    Single<ResponseAlimentationAccount> allimenter_account(@Header(Constants.TOKEN_HEADER) String token, @Body RequestAlimentationAccount requestAlimentationAccount);

    //**********Transaction**********
    @GET("GetTransactionsByCompte")
    Observable<List<Transactions>> get_transactionBy_IdAccount(@Header(Constants.TOKEN_HEADER) String token, @Query("accountId") Long accountId);

    @GET("GetLast5TransactionsByCompte")
    Observable<List<Transactions>> get_last5_transactionBy_IdAccount(@Header(Constants.TOKEN_HEADER) String token, @Query("accountId") Long accountId);

    //**********Recharge**********
    @GET("GetListOperateur")
    Observable<List<Operator>> get_operator(@Header(Constants.TOKEN_HEADER) String token);

    @POST("TopUp")
    Single<Transactions> recharge_topUp(@Header(Constants.TOKEN_HEADER) String token, @Body RequestTopUp requestTopUp);


}
