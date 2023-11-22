package com.example.universidad_sabado;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClsEstudiantesAdapter extends RecyclerView.Adapter <ClsEstudiantesAdapter.clsestudiantesViewHolder> {
    private ArrayList<ClsEstudiantes> listarestudiantes;

    public ClsEstudiantesAdapter(ArrayList<ClsEstudiantes> listarestudiantes) {
        this.listarestudiantes = listarestudiantes;
    }

    @NonNull
    @Override
    public ClsEstudiantesAdapter.clsestudiantesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estudiantesresources,null,false);
        return new ClsEstudiantesAdapter.clsestudiantesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClsEstudiantesAdapter.clsestudiantesViewHolder holder, int position) {
        holder.tvcarnet.setText(listarestudiantes.get(position).getCarnet());
        holder.tvnombre.setText(listarestudiantes.get(position).getNombre());
        holder.tvcarrera.setText(listarestudiantes.get(position).getCarrera());
        holder.tvsemestre.setText(listarestudiantes.get(position).getSemestre());
        if (listarestudiantes.get(position).getActivo().equals("Si"))
            holder.cbactivo.setChecked(true);
        else
            holder.cbactivo.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return listarestudiantes.size();
    }
//static en la clase da mayor alcance
    public static class clsestudiantesViewHolder extends RecyclerView.ViewHolder {
        TextView tvcarnet, tvnombre, tvcarrera, tvsemestre;
        CheckBox cbactivo;
        public clsestudiantesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcarnet=itemView.findViewById(R.id.tvcarnet);
            tvnombre=itemView.findViewById(R.id.tvnombre);
            tvcarrera=itemView.findViewById(R.id.tvcarrera);
            tvsemestre=itemView.findViewById(R.id.tvsemestre);
            cbactivo=itemView.findViewById(R.id.cbactivo);

        }
    }
}
