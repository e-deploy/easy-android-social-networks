package com.victormoraes.br.libsocialnetworking.services;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.victormoraes.br.libsocialnetworking.FacebookUser;
import com.victormoraes.br.libsocialnetworking.interfaces.FacebookStatus;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by victormoraes on 05/02/18.
 */

public class FacebookLogin {

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";

    private static FacebookLogin instance;

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private boolean errorFacebook = false;

    private FacebookLogin() {
        instance = this;
    }

    public static FacebookLogin getInstance() {
        return instance == null ? new FacebookLogin() : instance;
    }

    private void loginFacebook(final Activity activity, @NonNull final FacebookStatus facebookStatus) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    final GraphResponse response) {
                                try {
                                    FacebookUser userFacebook = new FacebookUser();
                                    userFacebook.setId(object.optString(ID));
                                    userFacebook.setName(object.optString(FIRST_NAME) + " " + object.optString(LAST_NAME));
                                    userFacebook.setEmail(object.optString(EMAIL));
                                    facebookStatus.onSucess(userFacebook);
                                } catch (Exception e) {
                                    facebookStatus.onError("Erro desconhecido, tente novamente em instantes");
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                facebookStatus.onCancelLogin();
            }

            @Override
            public void onError(FacebookException e) {
                if (e instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null && !errorFacebook) {
                        errorFacebook = true;
                        LoginManager.getInstance().logOut();
                        loginFacebook(activity, facebookStatus);
                    } else {
                        facebookStatus.onError("Erro desconhecido, tente novamente em instantes");
                    }
                } else {
                    facebookStatus.onError("Erro desconhecido, tente novamente em instantes");
                }
            }
        });
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
    }

    public void onLogin(final Activity activity, @NonNull final FacebookStatus facebookStatus) {
        errorFacebook = false;
        loginFacebook(activity, facebookStatus);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
