package com.robertory.packinglot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnMomo){
            Snackbar.make(v, "Payment Successful", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }
}