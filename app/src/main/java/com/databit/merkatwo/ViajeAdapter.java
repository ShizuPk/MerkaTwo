// Clase ViajeAdapter
package com.databit.merkatwo;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.List;

public class ViajeAdapter extends RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder> {
    private List<Viaje> listaViajes;
    private Context contexto;
    private OnCompraClickListener compraClickListener;
    private List<Viaje> listaViajesEnCarrito;  // Agregado para manejar el carrito

    public ViajeAdapter(List<Viaje> listaViajes, Context contexto, List<Viaje> listaViajesEnCarrito) {
        this.listaViajes = listaViajes;
        this.contexto = contexto;
        this.listaViajesEnCarrito = listaViajesEnCarrito;
    }

    public interface OnCompraClickListener {
        void onRealizarCompraClick(int position);
    }

    public void setOnCompraClickListener(OnCompraClickListener listener) {
        this.compraClickListener = listener;
    }

    @Override
    public ViajeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_viaje, parent, false);
        return new ViajeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViajeViewHolder holder, int position) {
        // Obtener el viaje en la posición dada
        Viaje viaje = listaViajes.get(position);

        // Rellenar los datos en el ViewHolder
        holder.nombreViaje.setText(viaje.getNombre());
        holder.descripcionViaje.setText(viaje.getDescripcion());
        holder.precioViaje.setText(viaje.getPrecio());
        holder.cantidadViaje.setText("Cantidad: " + viaje.getCantidad()); // Actualizar la cantidad

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions
                .transforms(new CenterCrop(), new RoundedCorners(16))
                .override(350, 250); // Ajusta el tamaño de la imagen según tus preferencias

        // Cargar la imagen usando Glide
        Glide.with(contexto)
                .load(viaje.getImagenResId())
                .apply(requestOptions)
                .into(holder.imagenViaje);

        // Manejar clic en el botón de compra
        holder.btnComprarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && compraClickListener != null) {
                    compraClickListener.onRealizarCompraClick(adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaViajes.size();
    }

    public class ViajeViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreViaje, descripcionViaje, precioViaje, cantidadViaje;
        public Button btnComprarViaje;
        public ImageView imagenViaje;

        public ViajeViewHolder(View view) {
            super(view);
            nombreViaje = view.findViewById(R.id.textNombreViaje);
            descripcionViaje = view.findViewById(R.id.textDescripcionViaje);
            precioViaje = view.findViewById(R.id.textPrecioViaje);
            cantidadViaje = view.findViewById(R.id.textCantidadViaje);
            btnComprarViaje = view.findViewById(R.id.btnViaje);
            imagenViaje = view.findViewById(R.id.imageViaje);
        }
    }

    private void agregarViajeAlCarrito(Viaje viaje) {
        // Incrementar la cantidad del viaje si ya está en el carrito
        if (listaViajesEnCarrito.contains(viaje)) {
            Viaje viajeEnCarrito = listaViajesEnCarrito.get(listaViajesEnCarrito.indexOf(viaje));
            viajeEnCarrito.incrementarCantidad();
        } else {
            // Agregar el viaje al carrito si no está en la lista
            Viaje viajeEnCarrito = new Viaje(
                    viaje.getNombre(),
                    viaje.getDescripcion(),
                    viaje.getPrecio(),
                    viaje.getImagenResId(),
                    1
            );
            listaViajesEnCarrito.add(viajeEnCarrito);
        }

        // Guardar la lista de viajes en el carrito en SharedPreferences
        guardarViajesEnCarrito();

        // Notificar al adaptador que los datos han cambiado
        notifyDataSetChanged();
    }
    private void guardarViajesEnCarrito() {
        Gson gson = new Gson();
        String listaViajesEnCarritoJson = gson.toJson(listaViajesEnCarrito);

        SharedPreferences preferences = contexto.getSharedPreferences("carrito_viajes_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("viajes_en_carrito", listaViajesEnCarritoJson);
        editor.apply();
    }


}
