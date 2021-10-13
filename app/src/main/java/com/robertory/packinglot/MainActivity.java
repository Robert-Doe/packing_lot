package com.robertory.packinglot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Set<String> activeSensors=new HashSet<>();
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView txtResponse = findViewById(R.id.txtResponse);
        FirebaseUtil.openFbReference("Authors",this);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.waziup.io/api/v2/devices/EspPARKING/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WaziupApi api= retrofit.create(WaziupApi.class);
        spinner = (Spinner) findViewById(R.id.spinner);
        Call<List<Sensor>> call = api.getSensors();

        call.enqueue(new Callback<List<Sensor>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Sensor>> call, Response<List<Sensor>> response) {
                try {
                    if(!response.isSuccessful()){
                       // txtResponse.setText("Code : "+response.code());
                        Log.d("MainActivity : Loading", "onResponse: "+response.code());
                        return;
                    }
//                    List<Sensor> sensors=response.body();

                    activeSensors= response.body().stream()
                            .map(Sensor::getName)
                            .map(name->name.split("#")[0])
                            .collect(Collectors.toSet());

// Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,new ArrayList<String>(activeSensors));

                            //new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,activeSensors);
// Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                    spinner.setAdapter(adapter);



                    //String content="";
                    /*for(Sensor sensor:sensors){
                        content+=sensor.getId()+"\n";
                        content+=sensor.getName()+"\n";
                        content+=sensor.getUnit()+"\n";
                        if(sensor.getValue()!=null)
                        content+=sensor.getValue().getValue()+"\n";
                        txtResponse.setText(content);
                    }*/

                }catch (Exception ex){
                    Log.d("Error : ",ex.getMessage());
                }


            }

            @Override
            public void onFailure(Call<List<Sensor>> call, Throwable t) {
                    //txtResponse.setText(t.getMessage());
                Log.d("Failure MainActivity", "onFailure: "+t.getMessage());

            }
        });
       /* Gson gson=new Gson();

        String json=" {\n" +
                "    \"quantity_kind\": \"Distance\",\n" +
                "    \"value\": {\n" +
                "      \"date_received\": \"2021-06-19T14:38:03Z\",\n" +
                "      \"value\": \"0\"\n" +
                "    },\n" +
                "    \"name\": \"PARKINGLOT\",\n" +
                "    \"id\": \"SPD\",\n" +
                "    \"calib\": {\n" +
                "      \"linear\": {\n" +
                "        \"enabled\": true,\n" +
                "        \"value_max\": {\n" +
                "          \"sensor_value\": 300,\n" +
                "          \"real_value\": 0\n" +
                "        },\n" +
                "        \"value_min\": {\n" +
                "          \"sensor_value\": 900,\n" +
                "          \"real_value\": 100\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"unit\": \"Centimetre\",\n" +
                "    \"sensor_kind\": \"DistanceDevice\"\n" +
                "  }";*/


        //Sensor sensor=gson.fromJson(json,Sensor.class);

        Button btnSearchSpace=findViewById(R.id.btnSearchSpace);
        btnSearchSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parkIntent=new Intent(MainActivity.this,ShowSpace.class);
                if(spinner.getSelectedItemPosition()!=-1){
                    parkIntent.putExtra("park_name",spinner.getSelectedItem().toString());
                    startActivity(parkIntent);
                }else{
                    Toast.makeText(MainActivity.this, "Select Park to Proceed", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();

        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.attachListener();
    }
}
