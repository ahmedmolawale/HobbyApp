package ng.org.nacoss.www.hobbyapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.InjectView;
import ng.org.nacoss.www.hobbyapp.R;
import ng.org.nacoss.www.hobbyapp.adapter.HobbiesAdapter;
import ng.org.nacoss.www.hobbyapp.model.Data;
import ng.org.nacoss.www.hobbyapp.model.Hobby;
import ng.org.nacoss.www.hobbyapp.model.User;
import ng.org.nacoss.www.hobbyapp.rest.ApiClient;
import ng.org.nacoss.www.hobbyapp.rest.ApiInterface;
import ng.org.nacoss.www.hobbyapp.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ng.org.nacoss.www.hobbyapp.utils.Utility.SUCCESS_STATUS;

public class Dashboard extends AppCompatActivity {


    @InjectView(R.id.hobbies_recyclerview)
    RecyclerView hobbyRecyclerView;
    @InjectView(R.id.add_hobby)
    EditText addHobby;
    @InjectView(R.id.add_hobby_button)
    Button addHobbyButton;
    @InjectView(R.id.dashboard_progress_bar)
    ProgressBar progressBar;
    @InjectView(R.id.no_hobbies)
    TextView noHobbies;

    RecyclerView.LayoutManager layoutManager;
    HobbiesAdapter hobbiesAdapter;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        layoutManager = new LinearLayoutManager(this);
        hobbyRecyclerView.setLayoutManager(layoutManager);
        hobbyRecyclerView.setHasFixedSize(true);

        Intent intent = getIntent();
        data = null;
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            data = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }
        if (data != null && data.getHobbies().size() == 0) {
            noHobbies.setVisibility(View.VISIBLE);
        } else {
            hobbiesAdapter = new HobbiesAdapter(data.getHobbies());
            hobbyRecyclerView.setAdapter(hobbiesAdapter);
        }
        addHobbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHobby();
            }
        });
    }

    private void addHobby() {
        this.addHobby.setError(null);
        String hobby = this.addHobby.getText().toString();

        boolean cancel = false;
        if (TextUtils.isEmpty(hobby)) {
            this.addHobby.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (cancel) {
            this.addHobby.requestFocus();
        } else {
            callApiToAddHobby(this.data.getUsername(), hobby);
        }
    }

    private void callApiToAddHobby(final String username, String hobby) {

        if (!Utility.isOnline(this)) {

            String title = "Connection";
            String message = "No internet connection. Please try again.";
            Utility.displayMessage(this, title, message);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<User> callToApi = apiInterface.addHobbyToUser(username, hobby);
        final Call<User> callToApi2 = apiInterface.getHobbies(username);
        callToApi.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


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
                    Utility.displayMessage(Dashboard.this, errorTitle, errorMessage);
                    return;
                } else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status == SUCCESS_STATUS) {
                        //send sms and email
                        //reload the hobbies
                        doIt(callToApi2);
                        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorTitle = "Error";
                String errorMessage = "Data request failed.";
                Utility.displayMessage(Dashboard.this, errorTitle, errorMessage);
            }
        });
    }
    private void doIt(Call<User> callToApi){

        callToApi.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar.setVisibility(View.INVISIBLE);
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
                    Utility.displayMessage(Dashboard.this, errorTitle, errorMessage);
                    return;
                } else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status == SUCCESS_STATUS) {
                        hobbiesAdapter = new HobbiesAdapter(response.body().getData().getHobbies());
                        hobbiesAdapter.notifyDataSetChanged();
                        hobbyRecyclerView.setAdapter(hobbiesAdapter);

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }
}
