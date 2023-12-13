package Vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.databit.merkatwo.AppDatabase;
import com.databit.merkatwo.R;
import com.databit.merkatwo.Usuario;
import com.databit.merkatwo.menuActivity;

public class login extends AppCompatActivity {

    private EditText firstNameEditText, passwordEditText;

    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerButton = findViewById(R.id.registerButton);
        Button LoginButton = findViewById(R.id.loginButton);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        // Cargar el nombre de usuario almacenado, si existe
        String storedUsername = getStoredUsername();
        if (!TextUtils.isEmpty(storedUsername)) {
            // Si hay un nombre de usuario almacenado, puedes realizar alguna acción,
            // por ejemplo, llenar automáticamente el campo de nombre de usuario.
            firstNameEditText.setText(storedUsername);
        }

        // Configurar el OnClickListener para el botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    private void registrarUsuario() {
        String nombre = firstNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(password)) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos.");
        } else {
            // Verificar si el usuario ya está registrado
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

            // Ejecutar la consulta en un hilo aparte para evitar bloquear el hilo principal
            new Thread(() -> {
                Usuario usuarioExistente = db.usuarioDao().buscarUsuario(nombre,password);

                // Acciones posteriores en el hilo principal
                runOnUiThread(() -> {
                    if (usuarioExistente != null) {
                        // El usuario ya está registrado, mostrar mensaje o realizar alguna acción
                        mostrarAlerta("Usuario Existente", "Este usuario ya está registrado.");
                    } else {
                        // El usuario no está registrado, proceder con el registro
                        Usuario nuevoUsuario = new Usuario(nombre, password);
                        insertarUsuarioEnBD(nuevoUsuario);

                        // Guardar el nombre de usuario en SharedPreferences
                        guardarNombreUsuarioEnPrefs(nombre);

                        // Pasar el nombre de usuario a la actividad menuActivity
                        Intent intent = new Intent(login.this, menuActivity.class);
                        intent.putExtra("NOMBRE_USUARIO", nombre);
                        startActivity(intent);
                    }
                });
            }).start();
        }
    }

    private void iniciarSesion() {
        String nombre = firstNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(password)) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos.");
        } else {
            // Acceder a la base de datos y realizar la consulta
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

            // Ejecutar la consulta en un hilo aparte para evitar bloquear el hilo principal
            new Thread(() -> {
                // Verificar si el usuario existe en la base de datos
                Usuario usuario = db.usuarioDao().buscarUsuario(nombre, password);

                // Acciones posteriores en el hilo principal
                runOnUiThread(() -> {
                    if (usuario != null) {
                        // Usuario encontrado, iniciar la actividad menuActivity
                        Intent intent = new Intent(login.this, menuActivity.class);
                        intent.putExtra("NOMBRE_USUARIO", nombre);
                        startActivity(intent);
                    } else {
                        // Usuario no encontrado, mostrar mensaje de error en el hilo principal
                        mostrarAlerta("Error de inicio de sesión", "Usuario o contraseña incorrectos.");
                    }
                });
            }).start();
        }
    }

    private void guardarNombreUsuarioEnPrefs(String nombre) {
        // Guardar el nombre de usuario en SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("NOMBRE_USUARIO", nombre);
        editor.apply();
    }

    private String getStoredUsername() {
        // Obtener el nombre de usuario almacenado desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("NOMBRE_USUARIO", "");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void insertarUsuarioEnBD(Usuario usuario) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        new Thread(() -> {
            db.usuarioDao().insertar(usuario);
        }).start();
    }

}
