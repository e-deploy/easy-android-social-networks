package com.victormoraes.br.libsocialnetworking;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by victormoraes on 05/02/18.
 */

public class FacebookUser {

    private String name;
    private String email;
    private String id;

    @NonNull
    public String getName() {
        return TextUtils.isEmpty(name) ? "" : name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getEmail() {
        return TextUtils.isEmpty(email) ? "" : email.trim();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    public String getId() {
        return TextUtils.isEmpty(id) ? "" : id.trim();
    }

    public void setId(String id) {
        this.id = id;
    }
}
