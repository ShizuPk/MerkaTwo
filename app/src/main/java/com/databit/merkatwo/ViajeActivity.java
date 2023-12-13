package com.databit.merkatwo;

import android.content.Context;
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

public class ViajeActivity extends AppCompatActivity implements ViajeAdapter.OnCompraClickListener {

    private RecyclerView recyclerViewViajes;
    private ViajeAdapter viajeAdapter;
    private List<Viaje> listaViajes;
    private List<Viaje> listaViajesEnCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viaje);

        recuperarViajesEnCarrito();

        recyclerViewViajes = findViewById(R.id.recyclerViewViajes);
        recyclerViewViajes.setLayoutManager(new LinearLayoutManager(this));

        listaViajes = obtenerListaDeViajes();

        viajeAdapter = new ViajeAdapter(listaViajes, this, listaViajesEnCarrito);
        recyclerViewViajes.setAdapter(viajeAdapter);

        // Modifica el código para manejar el clic en el botón "Comprar" en el adaptador
        viajeAdapter.setOnCompraClickListener(new ViajeAdapter.OnCompraClickListener() {
            @Override
            public void onRealizarCompraClick(int position) {
                Viaje viaje = listaViajes.get(position);
                agregarViajeAlCarrito(viaje);
                mostrarMensajeCompraRealizada(viaje);
            }
        });
    }

    @Override
    public void onRealizarCompraClick(int position) {
        Viaje viaje = listaViajes.get(position);
        agregarViajeAlCarrito(viaje);
        mostrarMensajeCompraRealizada(viaje);
    }
    private void agregarViajeAlCarrito(Viaje viaje) {
        if (listaViajesEnCarrito.contains(viaje)) {
            Viaje viajeEnCarrito = listaViajesEnCarrito.get(listaViajesEnCarrito.indexOf(viaje));
            viajeEnCarrito.incrementarCantidad();
        } else {
            Viaje nuevoViajeEnCarrito = new Viaje(
                    viaje.getNombre(),
                    viaje.getDescripcion(),
                    viaje.getPrecio(),
                    viaje.getImagenResId(),
                    1
            );
            listaViajesEnCarrito.add(nuevoViajeEnCarrito);
        }

        guardarViajesEnCarrito();
        viajeAdapter.notifyDataSetChanged();
    }

    private void mostrarMensajeCompraRealizada(Viaje viaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Compra Añadida al carrito");
        builder.setMessage("¡Gracias por tu compra de " + viaje.getNombre() + "!");
        builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<Viaje> obtenerListaDeViajes() {
        List<Viaje> listaViajes = new ArrayList<>();
        int idImagenSantiago = R.drawable.santiago;
        int idImagenArgentina = R.drawable.argentina;
        int idImagenBrasil = R.drawable.brazil;

        listaViajes.add(new Viaje("Santiago", "La capital del hermoso país llamado Chile", "1000", idImagenSantiago, 0));
        listaViajes.add(new Viaje("Argentina", "El país con el dólar caído al suelo", "1500", idImagenArgentina, 0));
        listaViajes.add(new Viaje("Brasil", "El país con la cultura más extraordinaria", "1200", idImagenBrasil, 0));

        return listaViajes;
    }

    private void guardarViajesEnCarrito() {
        Gson gson = new Gson();
        String listaViajesEnCarritoJson = gson.toJson(listaViajesEnCarrito);

        SharedPreferences preferences = getSharedPreferences("carrito_viajes_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("viajes_en_carrito", listaViajesEnCarritoJson);
        editor.apply();
    }

    private void recuperarViajesEnCarrito() {
        SharedPreferences preferences = getSharedPreferences("carrito_viajes_preferences", Context.MODE_PRIVATE);
        String listaViajesEnCarritoJson = preferences.getString("viajes_en_carrito", "");

        Gson gson = new Gson();
        Type listaViajesType = new TypeToken<List<Viaje>>() {}.getType();
        listaViajesEnCarrito = gson.fromJson(listaViajesEnCarritoJson, listaViajesType);

        if (listaViajesEnCarrito == null) {
            listaViajesEnCarrito = new ArrayList<>();
        }
    }
}
