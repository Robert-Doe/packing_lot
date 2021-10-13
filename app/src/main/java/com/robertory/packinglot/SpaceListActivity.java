package com.robertory.packinglot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpaceListActivity extends AppCompatActivity {

    List<Sensor> activeSensors=new ArrayList<Sensor>();
    TextView tvParkName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_list);
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
        tvParkName=findViewById(R.id.tvParkName);
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

                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(SpaceListActivity.this,2);
                    rvSpaces.setLayoutManager(layoutManager);

                    SpaceListAdapter adapter = new SpaceListAdapter(SpaceListActivity.this, activeSensors);
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