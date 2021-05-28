package com.balam.awalevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Declaracion de variables
    EditText etContra, etUsuario;
    ElasticButton btnInicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarControles();
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etUsuario.getText().toString() == "" || etContra.getText().toString() == "" ){
                   // System.out.println(etUsuario.getText()+" "+etContra.getText());
                    Toast.makeText(MainActivity.this, "ERROR: No puede haber campos vacíos.", Toast.LENGTH_SHORT).show();
                }else{
                    logueo();
                }
            }
        });
    }

    private void logueo() {
        String url = Uri.parse(Config.URL + "login.php")
                .buildUpon()
                .appendQueryParameter("user", etUsuario.getText().toString())
                .appendQueryParameter("pass", etContra.getText().toString())
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
                Toast.makeText(MainActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                System.out.println("Tipo de error: "+error.toString());
            }
        }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(peticion);
    }

    private void respuesta(JSONObject response) {
        try {
            System.out.println("LO QUE TRAE EL JSON: "+response.getString("login"));

            if (response.getString("login").compareTo("s") == 0) {
                etUsuario.setText("");
                etContra.setText("");
                startActivity(new Intent(this,InicioActivity.class));
            } else {
                Toast.makeText(this, "Error de usuario/contraseña", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
    }

    private void cargarControles() {
        //Cajas de texto
        etUsuario = findViewById(R.id.etUsuario);
        etContra = findViewById(R.id.etContra);
        //Boton
        btnInicio = findViewById(R.id.btnInicio);
    }
}