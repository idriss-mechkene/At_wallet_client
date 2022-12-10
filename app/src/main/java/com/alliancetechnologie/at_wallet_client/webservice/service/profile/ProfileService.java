package com.alliancetechnologie.at_wallet_client.webservice.service.profile;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestAlimentationAccount;
import com.alliancetechnologie.at_wallet_client.payload.RequestUpdatePassword;
import com.alliancetechnologie.at_wallet_client.payload.RequestUpdateProfile;
import com.alliancetechnologie.at_wallet_client.payload.ResponseAlimentationAccount;
import com.alliancetechnologie.at_wallet_client.webservice.retrofit.WebService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileService {

    public Completable updatePassword(String token, RequestUpdatePassword requestUpdatePassword) {
        return WebService.getInstance().getApi().update_password(token, requestUpdatePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<User> updateProfile(String token, RequestUpdateProfile requestUpdateProfile) {
        return WebService.getInstance().getApi().update_profile(token, requestUpdateProfile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseAlimentationAccount> alimentationAccount(String token, RequestAlimentationAccount requestAlimentationAccount) {
        return WebService.getInstance().getApi().allimenter_account(token, requestAlimentationAccount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
