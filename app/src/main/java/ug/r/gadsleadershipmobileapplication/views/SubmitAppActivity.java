package ug.r.gadsleadershipmobileapplication.views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONException;

import java.util.Objects;

import ug.r.gadsleadershipmobileapplication.R;
import ug.r.gadsleadershipmobileapplication.databinding.ActivitySubmitAppBinding;
import ug.r.gadsleadershipmobileapplication.utils.RetroInterface;
import ug.r.gadsleadershipmobileapplication.utils.RetrofitClient;

public class SubmitAppActivity extends AppCompatActivity {
    private ActivitySubmitAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_app);


        this.binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void submit() {
        String firstName = Objects.requireNonNull(this.binding.firstName.getText()).toString().trim();
        String lastName = Objects.requireNonNull(this.binding.lastName.getText()).toString().trim();
        String emailAddress = Objects.requireNonNull(this.binding.emailAddress.getText()).toString().trim();
        String gitLink = Objects.requireNonNull(this.binding.githubLink.getText()).toString().trim();

        RetrofitClient.getClient("").create(RetroInterface.class).submit_project(firstName, emailAddress, lastName, gitLink);
    }
}