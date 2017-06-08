package ng.org.nacoss.www.hobbyapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ng.org.nacoss.www.hobbyapp.R;
import ng.org.nacoss.www.hobbyapp.model.Data;
import ng.org.nacoss.www.hobbyapp.model.User;
import ng.org.nacoss.www.hobbyapp.rest.ApiClient;
import ng.org.nacoss.www.hobbyapp.rest.ApiInterface;
import ng.org.nacoss.www.hobbyapp.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ng.org.nacoss.www.hobbyapp.utils.Utility.SUCCESS_STATUS;

public class SignIn extends AppCompatActivity {


    @InjectView(R.id.username_sign_in)
    TextView username;
    @InjectView(R.id.password_sign_in)
    TextView password;
    @InjectView(R.id.sign_in_button)
    Button signIn;
    @InjectView(R.id.register_on_sign_in)
    Button register;
    @InjectView(R.id.sign_in_progress_bar)
    ProgressBar progressBar;


    private View focusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);

        ButterKnife.inject(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistrationActivity();
            }
        });
    }

    private void attemptLogin() {

        this.username.setError(null);
        this.password.setError(null);

        // Store values at the time of the sign in attempt.
        String username = this.username.getText().toString().trim();
        String password = this.password.getText().toString();


        boolean cancel = false;
        focusView = null;
        if (TextUtils.isEmpty(username)) {
            this.username.setError(getString(R.string.error_field_required));
            focusView = this.username;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            this.password.setError(getString(R.string.error_field_required));
            focusView = this.password;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt sign in and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            callApiToLogin(username, password);

        }
    }

    private void callApiToLogin(String username, String password) {


        if (!Utility.isOnline(this)) {

            String title = "Connection";
            String message = "No internet connection. Please try again.";
            Utility.displayMessage(this, title, message);

            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


        Call<User> callToApi = apiInterface.signIn(username, password);

        callToApi.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                progressBar.setVisibility(View.INVISIBLE);
                //getting the response body
                User user = response.body();
                if (user == null) {

                    ResponseBody responseBody = response.errorBody();
                    String errorTitle;
                    String errorMessage;
                    if (responseBody != null) {
                        errorTitle = "Error";
                        errorMessage = "An error occurred.";
                    } else {
                        errorTitle = "Error";
                        errorMessage = "No data Received.";
                    }
                    Utility.displayMessage(SignIn.this, errorTitle, errorMessage);

                } else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();

                    if (status.equals(Utility.SUCCESS_STATUS)) {
                        Data data = response.body().getData();
                        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        startDashboardActivity(data);
                        SignIn.this.finish();

                    } else {
                        Utility.displayMessage(SignIn.this,status,message);
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorTitle = "Error";
                String errorMessage = "Data request failed.";
                Utility.displayMessage(SignIn.this, errorTitle, errorMessage);
            }
        });
    }
    private void startDashboardActivity(Data data) {

        Intent intent = new Intent(SignIn.this,Dashboard.class);
        intent.putExtra(Intent.EXTRA_TEXT,data);
        startActivity(intent);


    }
    private void startRegistrationActivity(){
        startActivity(new Intent(SignIn.this,Registration.class));


    }
}