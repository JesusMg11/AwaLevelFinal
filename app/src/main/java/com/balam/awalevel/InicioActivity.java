package com.balam.awalevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.SpeedView;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONObject;

public class InicioActivity extends AppCompatActivity {

    //Declaración de variables
    ElasticButton btnSalir, btnCargar ;
    PointerSpeedometer speedometer;
    //Conversor
    int porRecibido;
    int porMediador;
    int porFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        cargarControles();
        //Inicializamos el indicador y seteamos las unidades en porcentaje
        speedometer.setUnit(" %");

        //Eventos de Botones
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(InicioActivity.this, "Nos vemos pronto.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // speedometer.speedTo((float) (Math.random()*100));
               // Toast.makeText(InicioActivity.this, "¡Actualizado!", Toast.LENGTH_SHORT).show();
                actualizar();
            }
        });
    }

    private void actualizar() {
        String url = Uri.parse(Config.URL + "sensor.php")
                .buildUpon()
                .appendQueryParameter("id", "0")
                .build().toString();

        System.out.println("DIRECCION: "+url);

        JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        respuesta(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InicioActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                System.out.println("Tipo de error: "+error.toString());
            }
        }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(peticion);
    }

    private void respuesta(JSONObject response) {
        try {
            System.out.println("LO QUE TRAE EL JSON: "+response.getString("porcentaje_ni"));
            //speedometer.speedTo(Integer.parseInt(response.getString("porcentaje_ni")));
            porRecibido = Integer.parseInt(response.getString("porcentaje_ni"));
            porMediador = (porRecibido * 100) / 75; //75 es la distancia en cm del inicio al final del contenedor de agua
            porFinal = 100 - porMediador;
            speedometer.speedTo(porFinal);
            Toast.makeText(InicioActivity.this, "¡Actualizado!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){}
    }

    private void cargarControles() {
        //Botones
        btnSalir = findViewById(R.id.btnSalir);
        btnCargar = findViewById(R.id.btnCargar);
        //Indicador
        speedometer = findViewById(R.id.speedView);
    }
}