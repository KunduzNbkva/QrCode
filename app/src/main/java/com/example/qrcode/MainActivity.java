package com.example.qrcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qrcode.data.model.User;
import com.example.qrcode.data.userApi.UserService;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
   private Button scanButton,generateButton;
   private TextView qrLog,qrPass;
    IntentResult scanResult;
    String [] auth;
    String log,pass;
    ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton=findViewById(R.id.scan_btn);
        generateButton=findViewById(R.id.generate_btn);
        qrLog=findViewById(R.id.logText);
        qrPass=findViewById(R.id.passText);
        avatar=findViewById(R.id.avatar);
         final GenerateCodeFragment fragment=new GenerateCodeFragment();


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanQRCode();
            }
        });
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).addToBackStack(null).commit();
                generateButton.setVisibility(View.INVISIBLE);
                scanButton.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void scanQRCode() {
        IntentIntegrator intentIntegrator=new IntentIntegrator(MainActivity.this);
        intentIntegrator.initiateScan();
        auth();
    }
    private void auth() {
        String userName =qrLog.getText().toString();
        String userPass = qrPass.getText().toString();

        String authHeader = Credentials.basic(userName, userPass);

        UserService.getService().getUser(authHeader).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("tag", "User: " + response.body().getLogin());
                    Toast.makeText(MainActivity.this, "Login: "+response.body().getLogin(), Toast.LENGTH_SHORT).show();
                    Glide.with(MainActivity.this).load(response.body().getAvatarUrl()).centerCrop().into(avatar);
                } else {
                    Log.d("tag", "response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("tag", "Error");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         scanResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(scanResult!=null){
           auth =scanResult.getContents().split(",");
            log=auth[0];
            pass=auth[1];
            qrLog.setText(auth[0]);
            qrPass.setText(auth[1]);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}