package com.databit.merkatwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<Producto> listaCarrito;
    private Context contexto;
    private OnEliminarProductoClickListener eliminarProductoClickListener;

    private List<Viaje> listaViajesCarrito;


    public CarritoAdapter(List<Producto> listaCarrito, Context contexto) {
        this.listaCarrito = listaCarrito;
        this.contexto = contexto;
    }

    public interface OnEliminarProductoClickListener {
        void onEliminarProductoClick(int position);
    }

    public void setOnEliminarProductoClickListener(OnEliminarProductoClickListener listener) {
        this.eliminarProductoClickListener = listener;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Producto producto = listaCarrito.get(position);
        holder.nombreProductoCarrito.setText(producto.getNombre());
        holder.cantidadProductoCarrito.setText("Cantidad: " + producto.getCantidad());
        holder.precioTotalCarrito.setText("Total: $" + (producto.getPrecio() * producto.getCantidad()));
        Glide.with(contexto).load(producto.getImageUrl()).into(holder.imagenProductoCarrito);

        // Configurar clic del botón "Eliminar"
        holder.btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eliminarProductoClickListener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        eliminarProductoClickListener.onEliminarProductoClick(adapterPosition);
                    }
                }
            }
        });

        // Configurar clic del botón "Aumentar Cantidad"
        holder.btnAumentarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Incrementar la cantidad al hacer clic en el botón
                producto.setCantidad(producto.getCantidad() + 1);
                notifyDataSetChanged(); // Notificar al adaptador sobre el cambio
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCarrito != null ? listaCarrito.size() : 0;
    }

    public class CarritoViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreProductoCarrito, cantidadProductoCarrito, precioTotalCarrito;
        public ImageView imagenProductoCarrito;
        public Button btnAumentarCantidad, btnEliminarProducto;

        public CarritoViewHolder(View view) {
            super(view);
            nombreProductoCarrito = view.findViewById(R.id.textNombreProductoCarrito);
            cantidadProductoCarrito = view.findViewById(R.id.textCantidadProductoCarrito);
            precioTotalCarrito = view.findViewById(R.id.textPrecioTotalCarrito);
            imagenProductoCarrito = view.findViewById(R.id.imageProductoCarrito);
            btnAumentarCantidad = view.findViewById(R.id.btnAumentarCantidad);
            btnEliminarProducto = view.findViewById(R.id.btnEliminarProducto);
        }
    }
}
