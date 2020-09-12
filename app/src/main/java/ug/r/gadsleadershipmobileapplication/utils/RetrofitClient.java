package ug.r.gadsleadershipmobileapplication.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private Call<String> call;
    private FragmentActivity activity;
    private DialogFragment dialog;
    private Response server;

    public RetrofitClient(@NonNull Call<String> call, @Nullable FragmentActivity activity,
                          @Nullable DialogFragment dialog, @NonNull Response response) {
        this.call = call;
        this.activity = activity;
        this.dialog = dialog;
        this.server = response;

        this.process();
    }

    private void hide_dialog() {
        if (this.activity != null && this.dialog.isVisible()) {
            this.dialog.dismiss();
        }
    }

    private void process() {
        if (this.activity != null) {
            this.dialog.show(activity.getSupportFragmentManager(), "network_dialog");
        }

        this.call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull retrofit2.Response<String> response) {
                hide_dialog();
                try {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        server.response(response.body(), response.code());

                    } else {
                        ResponseBody error_body = response.errorBody();
                        if (error_body != null) {
                            InputStream stream = error_body.byteStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                            StringBuilder builder = new StringBuilder();
                            String line = reader.readLine();
                            while (line != null) {
                                builder.append(line).append("\n");
                                line = reader.readLine();
                            }
                            String message = builder.toString();
                            JSONObject json = message.isEmpty() ? new JSONObject() : new JSONObject(message);
                            server.response(json.toString(), response.code());

                        } else {
                            server.response(new JSONObject().toString(), response.code());
                        }
                    }
                } catch (Exception exception) {
                    try {
                        server.response(new JSONObject().put("error", exception.getLocalizedMessage()).toString(), response.code());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                try {
                    server.response(
                            new JSONObject()
                                    .put("error", throwable.getLocalizedMessage())
                                    .put("network", throwable instanceof IOException).toString(),
                            500
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Retrofit getClient(String url) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();


        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public interface Response {
        void response(String response, Integer code) throws JSONException;
    }
}

