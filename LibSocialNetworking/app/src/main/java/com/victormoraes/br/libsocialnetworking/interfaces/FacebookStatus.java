package com.victormoraes.br.libsocialnetworking.interfaces;

import android.support.annotation.NonNull;

import com.victormoraes.br.libsocialnetworking.FacebookUser;

/**
 * Created by victormoraes on 05/02/18.
 */

public interface FacebookStatus {
    void onSucess(@NonNull FacebookUser facebookUser);

    void onError(@NonNull String error);

    void onCancelLogin();
}
