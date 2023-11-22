package com.example.universidad_sabado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListarEstudiantesMainActivity extends AppCompatActivity {

    TextView tvaplicacion;
    RecyclerView rvestudiantes;
    ArrayList<ClsEstudiantes> alestudiantes;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String coleccion="Estudiante";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_estudiantes_main);
        getSupportActionBar().hide();
        //Asociar los objetos java con los XML
        tvaplicacion=findViewById(R.id.tvaplicacion);
        rvestudiantes=findViewById(R.id.rvestudiantes);
        //Inicializar el array list
        alestudiantes=new ArrayList<>();
        rvestudiantes.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rvestudiantes.setHasFixedSize(true);


        //Consultar y llevar al array list
        cargar_datos();

    }// Fin metodo oncreate

    private void cargar_datos(){
        db.collection(coleccion)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //instanciar la clase ClsEstudiantes
                                ClsEstudiantes objestudiantes=new ClsEstudiantes();
                                objestudiantes.setCarnet(document.getString("Carnet"));
                                objestudiantes.setCarrera(document.getString("Carrera"));
                                objestudiantes.setNombre(document.getString("Nombre"));
                                objestudiantes.setSemestre(document.getString("Semestre"));
                                objestudiantes.setActivo(document.getString("Activo"));
                                alestudiantes.add(objestudiantes);

                            }
                            //Adapte la informacion del ArrayList
                            ClsEstudiantesAdapter aestudiantes=new ClsEstudiantesAdapter(alestudiantes);
                            rvestudiantes.setAdapter(aestudiantes);

                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        //fin metodo Cargar_datos
    }
    public void Regresar (View view){
        Intent intregresar=new Intent(this, MainActivity.class);
        startActivity(intregresar);
    }
}