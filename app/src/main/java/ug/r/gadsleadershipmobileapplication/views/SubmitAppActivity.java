package ug.r.gadsleadershipmobileapplication.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.io.IOException;
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

        if (this.invalidName(firstName)) {
            Toast.makeText(this, "Enter a valid first name", Toast.LENGTH_LONG).show();
        } else if (this.invalidName(lastName)) {
            Toast.makeText(this, "Enter a valid last name", Toast.LENGTH_LONG).show();
        } else if (this.invalidEmail(emailAddress)) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_LONG).show();
        } else if (!android.util.Patterns.WEB_URL.matcher(gitLink).matches()) {
            Toast.makeText(this, "Enter a valid github link", Toast.LENGTH_LONG).show();
        } else {
            MyDialogFragment.newInstance(MyDialogFragment.DialogType.CONFIRM, "Are you sure?",
                    new MyDialogFragment.Confirm() {
                        @Override
                        public void confirm(boolean confirmed) {
                            if (confirmed) {
                                RetrofitClient.getClient("https://docs.google.com/forms/d/e/")
                                        .create(RetroInterface.class)
                                        .submit_project(firstName, emailAddress, lastName, gitLink)
                                        .enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                                if (response.isSuccessful()) {
                                                    MyDialogFragment.newInstance(MyDialogFragment.DialogType.SUCCESS,
                                                            "Submission Successful",
                                                            null, SubmitAppActivity.this);
                                                } else {
                                                    MyDialogFragment.newInstance(MyDialogFragment.DialogType.ERROR,
                                                            "Submission not successful",
                                                            null, SubmitAppActivity.this);
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                                MyDialogFragment.newInstance(MyDialogFragment.DialogType.ERROR,
                                                        (t instanceof IOException)
                                                                ? "Network error, please check your connection and then retry"
                                                                : t.getLocalizedMessage(),
                                                        null, SubmitAppActivity.this);
                                            }
                                        });
                            }
                        }
                    }, this);
        }
    }

    private boolean invalidEmail(String email_address) {
        return email_address.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email_address).matches();
    }


    private boolean invalidName(String name) {
        String expression = "^[a-zA-Z\\s]+";
        return !name.matches(expression);
    }
}