package ng.org.nacoss.www.hobbyapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import ng.org.nacoss.www.hobbyapp.utils.Utility;
import ng.org.nacoss.www.hobbyapp.model.User;
import ng.org.nacoss.www.hobbyapp.rest.ApiClient;
import ng.org.nacoss.www.hobbyapp.rest.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ng.org.nacoss.www.hobbyapp.utils.Utility.SUCCESS_STATUS;

public class Registration extends AppCompatActivity {

    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.email)
    TextView email;
    @InjectView(R.id.password)
    TextView password;
    @InjectView(R.id.confirm_password)
    TextView confirmPassword;
    @InjectView(R.id.reg_button)
    Button register;
    @InjectView(R.id.register_progress_bar)
    ProgressBar progressBar;


    private View focusView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        ButterKnife.inject(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptReg();
            }
        });


    }

    private void attemptReg() {
        // Reset errors.
        username.setError(null);
        email.setError(null);
        password.setError(null);
        confirmPassword.setError(null);

        // Store values at the time of the reg attempt.
        String username = this.username.getText().toString().trim();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String confirmPassword = this.confirmPassword.getText().toString();

        boolean cancel = false;
        focusView = null;
        if (TextUtils.isEmpty(username)) {

            this.username.setError(getString(R.string.error_field_required));
            focusView = this.username;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {

            this.email.setError(getString(R.string.error_field_required));
            focusView = this.email;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {

            this.password.setError(getString(R.string.error_field_required));
            focusView = this.password;
            cancel = true;
        }
        if (TextUtils.isEmpty(confirmPassword)) {

            this.confirmPassword.setError(getString(R.string.error_field_required));
            focusView = this.confirmPassword;
            cancel = true;
        }


        if (!TextUtils.isEmpty(email) && !Utility.validateEmail(email)) {
            //validate the user mail
            this.email.setError(getString(R.string.error_invalid_email));
            focusView = this.email;
            cancel = true;
        }

//        if (!TextUtils.isEmpty(password) && !Utility.validatePassword(password)) {
//            //validate the user password
//            this.password.setError(getString(R.string.error_invalid_password));
//            focusView = this.password;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if(password.equals(confirmPassword))
            callApiToRegister(username,email,password);
            else
                Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_LONG).show();

        }


    }
    private void callApiToRegister(String username,String email,String password) {


        if (!Utility.isOnline(this)) {

            String title = "Connection";
            String message = "No internet connection. Please try again.";
            Utility.displayMessage(Registration.this,title, message);

            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


        Call<User> callToApi = apiInterface.registerUser(username, email, password);

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
                    Utility.displayMessage(Registration.this,errorTitle, errorMessage+responseBody);

                } else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();

                    if (status == SUCCESS_STATUS) {
                        Toast toast = Toast.makeText(getApplicationContext(),message , Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        finish();
                        startLoginActivity();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),message , Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorTitle = "Error";
                String errorMessage = "Data request failed.";
                Utility.displayMessage(Registration.this,errorTitle, errorMessage+ t.getMessage());
            }
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(Registration.this,SignIn.class);
        startActivity(intent);
    }


}
