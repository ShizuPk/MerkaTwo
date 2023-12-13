package com.databit.merkatwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends AppCompatActivity implements CarritoAdapter.OnEliminarProductoClickListener {

    private RecyclerView recyclerViewCarrito;
    private CarritoAdapter carritoAdapter;
    private List<Producto> listaProductosEnCarrito;
    private Button btnVolverAlMenu;
    private Button btnPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // Obtener la lista de productos en el carrito desde SharedPreferences
        recuperarProductosEnCarrito();

        // Inicializar el RecyclerView y el adaptador para el carrito
        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito);
        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(this));
        carritoAdapter = new CarritoAdapter(listaProductosEnCarrito, this);
        recyclerViewCarrito.setAdapter(carritoAdapter);

        // Establecer el escuchador en el adaptador
        carritoAdapter.setOnEliminarProductoClickListener(this);

        // Configurar el clic del botón "Volver al menú"
        btnVolverAlMenu = findViewById(R.id.btnVolverAlMenu);
        btnVolverAlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarritoActivity.this, menuActivity.class);
                startActivity(intent);
            }
        });

        // Configurar el clic del botón "Pagar"
        btnPagar = findViewById(R.id.btnPagar);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Calcular el monto total de la compra
                double montoTotal = calcularMontoTotal();

                // Simular el proceso de pago (puedes integrar el pago real aquí)
                procesarPago(montoTotal);

                // Mostrar notificación
                mostrarNotificacion("Compra realizada", "Monto: $" + montoTotal);

                // Limpiar el carrito después de la compra (puedes implementar esto)
                limpiarCarrito();
                guardarProductosEnCarrito();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Guardar la lista de productos en el carrito en SharedPreferences
        guardarProductosEnCarrito();
    }

    private void recuperarProductosEnCarrito() {
        SharedPreferences preferences = getSharedPreferences("carrito_preferences", Context.MODE_PRIVATE);
        String listaProductosEnCarritoJson = preferences.getString("productos_en_carrito", "");

        // Convertir la cadena JSON de vuelta a una lista de productos
        Gson gson = new Gson();
        Type listaProductosType = new TypeToken<List<Producto>>() {
        }.getType();
        listaProductosEnCarrito = gson.fromJson(listaProductosEnCarritoJson, listaProductosType);

        // Asegurarse de que la lista no sea nula
        if (listaProductosEnCarrito == null) {
            listaProductosEnCarrito = new ArrayList<>();
        }
    }

    private void guardarProductosEnCarrito() {
        // Guardar la lista de productos en el carrito en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("carrito_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convertir la lista de productos a JSON y guardarla
        Gson gson = new Gson();
        String listaProductosEnCarritoJson = gson.toJson(listaProductosEnCarrito);
        editor.putString("productos_en_carrito", listaProductosEnCarritoJson);

        // Aplicar los cambios
        editor.apply();
    }

    @Override
    public void onEliminarProductoClick(int position) {
        // Manejar la lógica para eliminar el producto en la posición dada
        if (listaProductosEnCarrito != null && position < listaProductosEnCarrito.size()) {
            listaProductosEnCarrito.remove(position);
            carritoAdapter.notifyDataSetChanged(); // Notificar al adaptador sobre el cambio en los datos

            // Guardar la lista actualizada en SharedPreferences
            guardarProductosEnCarrito();
        }
    }


    private double calcularMontoTotal() {
        double montoTotal = 0.0;
        for (Producto producto : listaProductosEnCarrito) {
            montoTotal += producto.getPrecio() * producto.getCantidad();
        }
        return montoTotal;
    }

    private void procesarPago(double montoTotal) {
        // Aquí iría la lógica real de procesamiento de pago (p. ej., usando Stripe, PayPal, etc.)
        // En este ejemplo, simplemente mostramos un mensaje en la consola.
        System.out.println("Procesando pago de $" + montoTotal);
    }

    private void mostrarNotificacion(String titulo, String mensaje) {
        Toast.makeText(this, titulo + ": " + mensaje, Toast.LENGTH_SHORT).show();
    }

    private void limpiarCarrito() {
        // Lógica para limpiar el carrito después de la compra
        listaProductosEnCarrito.clear();
        carritoAdapter.notifyDataSetChanged(); // Notificar al adaptador sobre el cambio en los datos
    }
}
