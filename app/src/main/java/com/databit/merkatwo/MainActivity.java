package com.databit.merkatwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductoAdapter productoAdapter;
    private List<Producto> listaProductos;
    private List<Producto> listaProductosEnCarrito;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recuperar productos en el carrito desde SharedPreferences
        recuperarProductosEnCarrito();

        // Inicializar la lista de productos y el RecyclerView
        listaProductos = crearListaProductos();
        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productoAdapter = new ProductoAdapter(listaProductos, this);
        recyclerView.setAdapter(productoAdapter);
        // Crear el listener para manejar clics en "Comprar"
        ProductoAdapter.OnCarritoClickListener carritoClickListener = new ProductoAdapter.OnCarritoClickListener() {
            @Override
            public void onAñadirAlCarritoClick(int position) {
                // Obtener el producto seleccionado y agregarlo al carrito
                Producto productoSeleccionado = listaProductos.get(position);
                agregarProductoAlCarrito(productoSeleccionado);

                // Mostrar mensaje de compra realizada
                mostrarMensajeCompraRealizada();
            }
        };
        productoAdapter = new ProductoAdapter(listaProductos, this);
        productoAdapter.setOnCarritoClickListener(carritoClickListener);
        recyclerView.setAdapter(productoAdapter);
    }

    // Método para crear una lista de productos de ejemplo
    private List<Producto> crearListaProductos() {
        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto("Nintendo Switch Oled", "Consola portátil", 299.99,"https://images.unsplash.com/photo-1612036781124-847f8939b154?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",1));
        lista.add(new Producto("Audífono HP", "Audífonos de alta calidad", 79.99,"https://falabella.scene7.com/is/image/Falabella/gsc_120991659_2861326_2?wid=1500&hei=1500&qlt=70",2));
        lista.add(new Producto("HP Victus 16", "Computadora portátil gaming", 1299.99,"https://falabella.scene7.com/is/image/Falabella/16584624_1?wid=1500&hei=1500&qlt=70",3));
        return lista;
    }
    private void agregarProductoAlCarrito(Producto producto) {
        // Incrementar la cantidad del producto si ya está en el carrito
        if (listaProductosEnCarrito.contains(producto)) {
            Producto productoEnCarrito = listaProductosEnCarrito.get(listaProductosEnCarrito.indexOf(producto));
            productoEnCarrito.incrementarCantidad();
        } else {
            // Agregar el producto al carrito si no está en la lista
            Producto productoEnCarrito = new Producto(
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getImageUrl(),
                    1
            );
            listaProductosEnCarrito.add(productoEnCarrito);
        }

        // Guardar la lista de productos en el carrito en SharedPreferences
        guardarProductosEnCarrito();

        // Notificar al adaptador que los datos han cambiado
        productoAdapter.notifyDataSetChanged();
    }




    // Método para mostrar un mensaje de compra realizada
    private void mostrarMensajeCompraRealizada() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Compra añadida");
        builder.setMessage("¡Gracias por tu compra!");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            dialog.dismiss();
            // Crear el intent para abrir la CarritoActivity
            Intent carritoIntent = new Intent(MainActivity.this, CarritoActivity.class);
            // Pasar la lista de productos en el carrito al intent
            carritoIntent.putParcelableArrayListExtra("listaProductosEnCarrito", new ArrayList<>(listaProductosEnCarrito));
            // Iniciar la CarritoActivity
            startActivity(carritoIntent);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {

        listaProductosEnCarrito.clear();
        super.onBackPressed();
    }
    private void guardarProductosEnCarrito() {
        // Convertir la lista de productos en el carrito a un formato que se pueda almacenar en SharedPreferences
        // Puedes usar Gson para convertir la lista a una cadena JSON
        Gson gson = new Gson();
        String listaProductosEnCarritoJson = gson.toJson(listaProductosEnCarrito);

        // Guardar la cadena JSON en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("carrito_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("productos_en_carrito", listaProductosEnCarritoJson);
        editor.apply();
    }
    private void recuperarProductosEnCarrito() {
        SharedPreferences preferences = getSharedPreferences("carrito_preferences", Context.MODE_PRIVATE);
        String listaProductosEnCarritoJson = preferences.getString("productos_en_carrito", "");

        // Convertir la cadena JSON de vuelta a una lista de productos
        Gson gson = new Gson();
        Type listaProductosType = new TypeToken<List<Producto>>() {}.getType();
        listaProductosEnCarrito = gson.fromJson(listaProductosEnCarritoJson, listaProductosType);

        // Asegurarse de que la lista no sea nula
        if (listaProductosEnCarrito == null) {
            listaProductosEnCarrito = new ArrayList<>();
        }
    }



}
