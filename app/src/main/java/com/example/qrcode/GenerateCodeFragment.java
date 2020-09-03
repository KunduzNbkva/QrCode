package com.example.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;

public class GenerateCodeFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private ImageView qrCode;
    private Button button;
    private EditText loginText,passwordText;

    public GenerateCodeFragment() {
    }
    public static GenerateCodeFragment newInstance(int position) {
        GenerateCodeFragment fragment = new GenerateCodeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generate_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qrCode=view.findViewById(R.id.imageView);
        button=view.findViewById(R.id.btn);
        loginText=view.findViewById(R.id.login_et);
        passwordText=view.findViewById(R.id.password_et);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQr();
            }
        });
    }
    private void generateQr() {
        QRCodeWriter writer=new QRCodeWriter();
        String login=loginText.getText().toString();
        String password=passwordText.getText().toString();
        String account=login+","+password;
        try {
            BitMatrix bitMatrix;
            if(!account.isEmpty()){
                bitMatrix=writer.encode(account, BarcodeFormat.QR_CODE,300,300);
            }else {
                bitMatrix=writer.encode("Error", BarcodeFormat.QR_CODE,300,300);
            }
            Bitmap bitmap=Bitmap.createBitmap(300,300, Bitmap.Config.RGB_565);
            for (int x = 0; x <300 ; x++) {
                for (int y = 0; y < 300; y++) {
                    int color;
                    if(bitMatrix.get(x,y)){
                        color= Color.BLACK;
                    }else {
                        color=Color.WHITE;
                    }
                    bitmap.setPixel(x,y, color);
                    qrCode.setImageBitmap(bitmap);
                }
            }


        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

}