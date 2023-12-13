package Vista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.databit.merkatwo.R;

public class Bienvenida extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        boolean bienvenidaMostrada = prefs.getBoolean("bienvenida_mostrada", false);

        if (bienvenidaMostrada) {
            setContentView(R.layout.activity_bienvenida);
            Button ingresarButton = findViewById(R.id.button3);

            ingresarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Bienvenida.this, login.class);
                    startActivity(intent);
                    finish(); // Finaliza la actividad actual para que no pueda volver atrás.
                }
            });
        } else {
            // Si la bienvenida ya se ha mostrado, inicia directamente la actividad login.
            Intent intent = new Intent(Bienvenida.this, login.class);
            startActivity(intent);
            finish(); // Finaliza la actividad actual para que no pueda volver atrás.
        }
    }

}

