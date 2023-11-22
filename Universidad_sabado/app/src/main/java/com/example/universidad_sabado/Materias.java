package com.example.universidad_sabado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Materias extends AppCompatActivity {

    EditText etcodmateria, etnombre, etprofesor, etcreditos;
    CheckBox cbactivo;
    Button btguardar, btcancelar, btlimpiar, btregresar;
    String materia, nombre, profesor, creditos, materias="asignaturas", clave;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);
        //ocultar barra de titulo por defecto
        getSupportActionBar().hide();

        etcodmateria=findViewById(R.id.etcodmateria);
        etnombre=findViewById(R.id.etnombre);
        etprofesor=findViewById(R.id.etprofesor);
        etcreditos=findViewById(R.id.etcreditos);
        cbactivo=findViewById(R.id.cbactivo);
        btguardar=findViewById(R.id.btguardar);
        btcancelar=findViewById(R.id.btcancelar);
        btlimpiar=findViewById(R.id.btlimpiar);
        btregresar=findViewById(R.id.btregresar);

    }//fin metodo oncreate

    public void Guardar(View view){
        materia = etcodmateria.getText().toString();
        nombre=etnombre.getText().toString();
        profesor=etprofesor.getText().toString();
        creditos=etcreditos.getText().toString();
        if(!materia.isEmpty() && !nombre.isEmpty() && !profesor.isEmpty() && !creditos.isEmpty()){
            // Create a new student with a first and last name
            // Hash significa que me va a crear una llave primeria aleatoria
            Map<String, Object> carrera = new HashMap<>();
            carrera.put("Materia",materia);
            carrera.put("NombreM", nombre);
            carrera.put("Profesor", profesor);
            carrera.put("Creditos", creditos);
            carrera.put("Activo", "Si");
            if (clave.equals("")){
                // Add a new document with a generated ID
                //la coleccion en base de datos relacionales es equivalente a tablas
                db.collection(materias)
                        //lanza el programa
                        .add(carrera)
                        //escucha la respuesta
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(Materias.this, "Documento guardado", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w(TAG, "Error adding document", e);
                                Toast.makeText(Materias.this, "Error al guardar documento", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{
                //Modificar
                db.collection(materias).document(clave)
                        .set(carrera)//Esto lo copio y pego del doc word pag 18
                        .addOnSuccessListener(new OnSuccessListener<Void>() {//Esto lo copio pag 18*
                            @Override
                            public void onSuccess(Void Void) {
                                Toast.makeText(Materias.this,"Documento actualizado",Toast.LENGTH_SHORT).show();
                                Limpiar_campos();//Esto lo modifico
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Materias.this,"Error actualizando documento",Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        }else{
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcodmateria.requestFocus();
        }
    }//fin metodo Guardar
    public void Consultar(View view){
        Consultar_documento();
    }//Fin metodo consultar

    private void Consultar_documento(){
        materia = etcodmateria.getText().toString();
        if (!materia.isEmpty())
        {
            db.collection(materias)
                    //con esta instruccion le digo que quiero que me consulte
                    .whereEqualTo("Materia", materia)
                    .get()
                    //en esta linea esta escuchando la respuesta
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {//cuando encuentra entra al for
                                if (task.getResult().size() != 0) {//esto que hago es para saber si lo encontro o no lo encotro, si es 0 no encontro nada *
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave=document.getId();
                                        etnombre.setText(document.getString("NombreM"));
                                        etprofesor.setText(document.getString("Profesor"));
                                        etcreditos.setText(document.getString("Creditos"));
                                        if (document.getString("Activo").equals("Si"))
                                            cbactivo.setChecked(true);
                                        else
                                            cbactivo.setChecked(false);
                                    }
                                    //* estos los habilitamos cuando el dif de 0 **
                                    cbactivo.setEnabled(true);
                                    btguardar.setEnabled(true);//* este boton lo habilitamos cuando da 0
                                } else {
                                    clave="";
                                    cbactivo.setEnabled(false);
                                    btguardar.setEnabled(true);//* este boton lo habilitamos cuando da 0
                                    //Log.w(TAG, "Error getting documents.", task.getException());
                                    Toast.makeText(Materias.this, "documento no hallado", Toast.LENGTH_SHORT).show();
                                }

                                //Aca estoy habilitando los campos para poder modificar despues de que haya consultado*
                                etcreditos.setEnabled(true);
                                etnombre.setEnabled(true);
                                etprofesor.setEnabled(true);
                                etnombre.requestFocus();
                                etcodmateria.setEnabled(false);
                            }else {//Cuando no encuentra nada entra a este else
                                //Log.w(TAG, "Error getting documents.", task.getException());
                                //Toast.makeText(MainActivity.this, "Documento no hallado", Toast.LENGTH_SHORT).show(); este lo puse como comentario*
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Carnet Requerido", Toast.LENGTH_SHORT).show();
            etcodmateria.requestFocus();
        }

    }//fin metodo consultar documento

    public void Salir(View view) {
        Intent intent = new Intent(this, Opciones.class);
        startActivity(intent);
    }

    public void Limpiar (View view){
        Limpiar_campos();
    }//Fin metodo Limpiar

    private void Limpiar_campos(){
        etcodmateria.setText("");
        etnombre.setText("");
        etprofesor.setText("");
        etcreditos.setText("");
        cbactivo.setChecked(false);
        etcodmateria.setEnabled(true);
        etnombre.setEnabled(false);
        etprofesor.setEnabled(false);
        etcreditos.setEnabled(false);
        btguardar.setEnabled(false);
        btcancelar.setEnabled(false);
        btlimpiar.setEnabled(false);
        btregresar.setEnabled(true);
        etcodmateria.requestFocus();

    }//Fin metodo limpiar
}