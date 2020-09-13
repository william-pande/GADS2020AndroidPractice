package ug.r.gadsleadershipmobileapplication.views;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ug.r.gadsleadershipmobileapplication.R;
import ug.r.gadsleadershipmobileapplication.databinding.ActivitySubmitAppBinding;
import ug.r.gadsleadershipmobileapplication.utils.MyDialogFragment;
import ug.r.gadsleadershipmobileapplication.utils.RetroInterface;
import ug.r.gadsleadershipmobileapplication.utils.RetrofitClient;

public class SubmitAppActivity extends AppCompatActivity {
    private ActivitySubmitAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_app);

        this.setSupportActionBar(this.binding.toolbar);
        this.binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitAppActivity.this.finish();
            }
        });


        this.binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitAppActivity.this.submit();
            }
        });
    }

    private void submit() {
        final String firstName = Objects.requireNonNull(this.binding.firstName.getText()).toString().trim();
        final String lastName = Objects.requireNonNull(this.binding.lastName.getText()).toString().trim();
        final String emailAddress = Objects.requireNonNull(this.binding.emailAddress.getText()).toString().trim();
        final String gitLink = Objects.requireNonNull(this.binding.githubLink.getText()).toString().trim();


        MyDialogFragment.newInstance(MyDialogFragment.DialogType.CONFIRM, "Are you sure?",
                new MyDialogFragment.Confirm() {
                    @Override
                    public void confirm(boolean confirmed) {
                        if (confirmed) {
                            RetrofitClient.getClient("").create(RetroInterface.class)
                                    .submit_project(firstName, emailAddress, lastName, gitLink)
                                    .enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                                        }
                                    });
                        }
                    }
                }, this);
    }
}