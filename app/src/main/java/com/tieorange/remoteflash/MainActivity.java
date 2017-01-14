package com.tieorange.remoteflash;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private CameraTorch2 mTorch;
    private FcmAPI mFcmAPI;
    private String mRefreshedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initTorch();
        if (initExtras()) return;

        initRefreshToken();
        initFirebase();
        initFAB();
        initListeners();
        initRetrofit();


    }

    private boolean initExtras() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            turnOnFlash();
            return true;
        } else {
            return false;
        }
    }

    private void initRefreshToken() {
        mRefreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "MainActivity Refreshed token: " + mRefreshedToken);
    }

    private void initRetrofit() {
        String baseUrl = "https://fcm.googleapis.com/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mFcmAPI = retrofit.create(FcmAPI.class);
    }

    private void sendNotification() {
        String json = "{\"notification\": " +
                "{\"title\":\"Title\",\"text\":\"Hello\"}," +
                "\"to\":\"cWMy5Z3bgaU:APA91bHxZoZh5ndAWN6H_oFa0_K7Hunavv-xGQLZJGCyaKaFbfPIOdv8XG9ozFMLzInZ6qsWUQe_TuUwyrN1EZvEIPhTJ7E1kXywxM-4Mao3P-Eai8a_L4LXdRHJQTBCRbi9KLksrhz7\"}";
        String jsonNew = "{\n" +
                "    \"to\" : " + mRefreshedToken + ",\n" +
                "    \"notification\" : {\n" +
                "      \"body\" : \"great match!\",\n" +
                "      \"title\" : \"Portugal vs. Denmark\"\n" +
                "    }\n" +
                "  }";
        mFcmAPI.send(getJsonMessage()).enqueue(new Callback<FcmResponse>() {
            @Override
            public void onResponse(Call<FcmResponse> call, Response<FcmResponse> response) {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<FcmResponse> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    private String getJsonMessage() {
        Gson gson = new Gson();
        String result;
        String to = mRefreshedToken;

        Notification notification = new Notification("hello", "world");
        FcmNotification fcmNotification = new FcmNotification(notification, to);
        result = gson.toJson(fcmNotification, FcmNotification.class);
//        String jsonResult = "{\"notification\":{\"body\":\"hello\",\"title\":\"world\"},\"to\":\"cWMy5Z3bgaU:APA91bHxZoZh5ndAWN6H_oFa0_K7Hunavv-xGQLZJGCyaKaFbfPIOdv8XG9ozFMLzInZ6qsWUQe_TuUwyrN1EZvEIPhTJ7E1kXywxM-4Mao3P-Eai8a_L4LXdRHJQTBCRbi9KLksrhz7\"}";

        return result;
    }

    private void initListeners() {
//        if (!Build.MODEL.equals("Nexus 5")) return;

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(Boolean.class);
                processResponse(value);
                Log.d(TAG, "isOn: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void processResponse(Boolean isOn) {
        if (isOn) turnOnFlash();
        else turnOffFlash();
    }

    private void initTorch() {
        // TODO: 1/14/17 check if nexus 5
        mTorch = new CameraTorch2(MainActivity.this);
        mTorch.init();
    }

    private void turnOnFlash() {
        mTorch.toggle(true);
    }

    private void turnOffFlash() {
        mTorch.toggle(false);
    }

    private void initFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toggleFlash();
                sendNotification();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void toggleFlash() {
        // Write a message to the database
        mRef.setValue(true);
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("isOn");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
