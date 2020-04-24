package org.dpppt.android.app.inform.networking;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dpppt.android.app.BuildConfig;
import org.dpppt.android.app.inform.model.AuthenticationCodeRequestModel;
import org.dpppt.android.app.inform.model.AuthenticationCodeResponseModel;
import org.dpppt.android.sdk.backend.ResponseCallback;
import org.dpppt.android.sdk.internal.backend.ResponseException;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthCodeRepository {

	private AuthCodeService authCodeService;

	public AuthCodeRepository(@NonNull Context context) {

		OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
		okHttpBuilder.addInterceptor(chain -> {
			Request request = chain.request()
					.newBuilder()
					.build();
			return chain.proceed(request);
		});

		int cacheSize = 5 * 1024 * 1024; // 5 MB
		Cache cache = new Cache(context.getCacheDir(), cacheSize);
		okHttpBuilder.cache(cache);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BuildConfig.AUTH_CODE_URL)
				.client(okHttpBuilder.build())
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		authCodeService = retrofit.create(AuthCodeService.class);
	}

	public void getAccessToken(@NonNull AuthenticationCodeRequestModel authenticationCode,
			@NonNull ResponseCallback<AuthenticationCodeResponseModel> callbackListener) {

		authCodeService.getAccessToken(authenticationCode).enqueue(new Callback<AuthenticationCodeResponseModel>() {
			@Override
			public void onResponse(Call<AuthenticationCodeResponseModel> call,
					Response<AuthenticationCodeResponseModel> response) {
				if (response.isSuccessful()) {
					callbackListener.onSuccess(response.body());
				} else {
					if (response.code() == 404) {
						onFailure(call, new InvalidCodeError());
					} else {
						onFailure(call, new ResponseException(response.raw()));
					}
				}
			}

			@Override
			public void onFailure(Call<AuthenticationCodeResponseModel> call, Throwable t) {
				callbackListener.onError(t);
			}
		});
	}

}