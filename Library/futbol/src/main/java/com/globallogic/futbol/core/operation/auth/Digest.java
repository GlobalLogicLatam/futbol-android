package com.globallogic.futbol.core.operation.auth;

import android.content.Context;

import com.globallogic.futbol.core.interfaces.IStrategyCallback;

/**
 * Created by Facundo Mengoni on 6/4/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
// ToDo
public class Digest {
    private boolean connect = false;

    public boolean requireAuthentication() {
        return false;
    }

    public void setAuthentication() {
    }

    public void doRequest(final Context aContext, IStrategyCallback callback) {
        /*            MyLog.d(TAG, String.format("User: %s Pass: %s", getUser(), getPass()));
            b = b.addHeader(getUser(), getPass());
            final String id;
            if (SharedPreferencesHelper.hasDeviceId()) {
                id = SharedPreferencesHelper.getDeviceId().toString();
                b = b.addHeader("device", id);
            } else {
                id = "";
            }
            b.onHeaders(new HeadersCallback() {
                @Override
                public void onHeaders(HeadersResponse headers) {
                    connect = true;
                    Header authenticate = getAuthenticationHeader(headers);
                    if (authenticate != null) {
                        Builders.Any.B b = Ion.with(aContext).load(getMethod(), getUrl()).setTimeout(Operation.TIMEOUT_MILLISECONDS)
                                .addHeader("platform", "android")
                                .addHeader(authenticate.getName(), authenticate.getValue());
                        if (!TextUtils.isEmpty(id)) {
                            MyLog.d(TAG, String.format("Device id: %s", id));
                            b = b.addHeader("device", id);
                        }
                        getResponse(b, callback);
                    } else {
                        MyLog.i(TAG, String.format("Headers: %s", headers.getHeaders().toString()));
                        callback.parseResponse(new HeaderException("Headers error. Strategy: " + toString()), null);
                    }
                }
            }).asString().setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    if (!connect)
                        callback.parseResponse(e, null);
                }
            });
*/
    }

    /*    protected Header getAuthenticationHeader(HeadersResponse headers) {
        try {
            String challenge = headers.getHeaders().get(AUTH.WWW_AUTH).replace(AUTH.WWW_AUTH.concat(": "), "");
            Header authChallenge = new BasicHeader(AUTH.WWW_AUTH, challenge);
            HttpRequest request = getHttpRequest();
            Credentials cred = new UsernamePasswordCredentials(mUser, mPass);
            AuthScheme authscheme = new DigestScheme();
            authscheme.processChallenge(authChallenge);
            return authscheme.authenticate(cred, request);
        } catch (Exception e) {
            MyLog.e(TAG, e.getMessage(), e);
        }
        return null;
    }
*/
    public String getUser() {
        return null;
    }

    public String getPass() {
        return null;
    }
}
