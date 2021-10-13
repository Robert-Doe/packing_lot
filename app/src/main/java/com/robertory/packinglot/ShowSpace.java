package com.robertory.packinglot;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowSpace extends AppCompatActivity {

    List<Sensor> activeSensors=new ArrayList<Sensor>();
    //TextView tvParkName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_space);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent payIntent=new Intent(ShowSpace.this,PayActivity.class);
                Snackbar.make(view, "Ready for payment", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(payIntent);
            }
        });


        RecyclerView rvSpaces=findViewById(R.id.rvSpaces);

        Bundle extras = getIntent().getExtras();
        String selectedPark=null;
        if(extras == null) {
            selectedPark= null;
        } else {
            selectedPark= extras.getString("park_name");
        }

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.waziup.io/api/v2/devices/EspPARKING/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WaziupApi api= retrofit.create(WaziupApi.class);
        //tvParkName=findViewById(R.id.tvParkName);
        Call<List<Sensor>> call = api.getSensors();

        //String finalSelectedPark = selectedPark;
        String finalSelectedPark = selectedPark;
        call.enqueue(new Callback<List<Sensor>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Sensor>> call, Response<List<Sensor>> response) {
                try {
                    if(!response.isSuccessful()){
//                        txtResponse.setText("Code : "+response.code());
                        Log.d("Error Cumulus : ",String.valueOf(response.code()));
                        return;
                    }
                    //List<Sensor> sensors=response.body();
                    activeSensors=response.body().stream()
                            .filter((sensor) -> finalSelectedPark.equals(sensor.getName().split("#")[0]))
                            .collect(Collectors.toList());

                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ShowSpace.this,2);
                    rvSpaces.setLayoutManager(layoutManager);

                    SpaceListAdapter adapter = new SpaceListAdapter(ShowSpace.this, activeSensors);
                    rvSpaces.setAdapter(adapter);

//                    String content="";
//                    for(Sensor sensor:activeSensors){
//                        content+=sensor.getId()+"\n";
//                        content+=sensor.getName()+"\n";
//                        content+=sensor.getUnit()+"\n";
//                        if(sensor.getValue()!=null)
//                            content+=sensor.getValue().getValue()+"\n";
//                        txtResponse.setText(content);
//                    }
                }catch (Exception ex){
                    Log.d("Error : ",ex.getMessage());
                }


            }

            @Override
            public void onFailure(Call<List<Sensor>> call, Throwable t) {
                //txtResponse.setText(t.getMessage());

                Log.d("Failure Cumulus : ",t.getMessage());
            }
        });



    }
}