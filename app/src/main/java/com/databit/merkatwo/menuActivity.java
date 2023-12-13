package com.databit.merkatwo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class menuActivity extends AppCompatActivity {

    private String nombreUsuario;  // Variable para almacenar el nombre del usuario
    private static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageButton btnCarrito = findViewById(R.id.btnCarrito);
        btnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad del carrito al hacer clic en el botón o imagen
                Intent intent = new Intent(menuActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });
        // Obtener el nombre de usuario del Intent
        nombreUsuario = getIntent().getStringExtra("NOMBRE_USUARIO");

// Si no hay nombre de usuario en el Intent, intenta restaurarlo desde el estado de la actividad
        if (nombreUsuario == null) {
            nombreUsuario = obtenerNombreUsuarioDesdePrefs();
        }



        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Bienvenido, " + nombreUsuario);
        ImageButton myImageButton = findViewById(R.id.btnTecno);
        myImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuActivity.this, MainActivity.class);
                // Pasar el nombre de usuario a la siguiente actividad
                intent.putExtra("NOMBRE_USUARIO", nombreUsuario);
                startActivity(intent);
            }
        });

        ImageButton btnViaje = findViewById(R.id.btnViaje);
        btnViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuActivity.this, ViajeActivity.class);
                // Pasar el nombre de usuario a la siguiente actividad
                intent.putExtra("NOMBRE_USUARIO", nombreUsuario);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el nombre de usuario solo si no se ha establecido desde el Intent
        if (nombreUsuario != null) {
            outState.putString("NOMBRE_USUARIO", nombreUsuario);
        }
    }

    private String obtenerNombreUsuarioDesdePrefs() {
        // Obtener el nombre de usuario almacenado desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("NOMBRE_USUARIO", null);
    }

}

