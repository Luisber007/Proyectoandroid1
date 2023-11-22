package com.example.universidad_sabado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.annotations.concurrent.Background;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MatriculaMainActivity2 extends AppCompatActivity {

    Button btconsultarmat, btconsulcarnet, btconsulmateria, btanular, btadicionar;
    TextView tvmostrarnombrec, tvmostrarnombrem, tvnombre, tvcreditos;

    EditText etcodigo, etcarnet, etcodmateria, etfecha;
    CheckBox cbactivo;

    String codigo, coleccion = "Matricula", clave, fecha, nombre_estudiante, nombre_materia, nombre_carrera, nombree, creditos;

    String coleccion1 = "Estudiante", carnet;
    String coleccion2 = "asignaturas", codigo_materia;

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //Inicializar el firestore

    //variables globales
    private int year, mes, dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matricula_main2);
        getSupportActionBar().hide();
        //Asociar objetos

        etfecha = findViewById(R.id.etfecha);///**********///
        btconsultarmat = findViewById(R.id.btbuscarmat);
        btconsulcarnet = findViewById(R.id.btbuscarcar);///**********///
        btconsulmateria = findViewById(R.id.btbuscarmateria);///**********///
        btadicionar = findViewById(R.id.btadicionar);
        btanular = findViewById(R.id.btanular);
        tvmostrarnombrec = findViewById(R.id.tvcarrera);///**********///
        tvnombre = findViewById(R.id.tvnombre);///**********///
        tvmostrarnombrem = findViewById(R.id.tvnommateria);
        tvcreditos = findViewById(R.id.tvcreditos);
        etcodigo = findViewById(R.id.etcodigo);///**********///
        etcarnet = findViewById(R.id.etcarnet);///**********///
        etcodmateria = findViewById(R.id.etcodmateria);///**********///
        cbactivo = findViewById(R.id.cbactivo);


        //colocamos el boton ponerFecha a la escucha de un clic
        etfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se obtiene la fecha actual del sistema
                final Calendar c = Calendar.getInstance();
                // se captura el año el mes y el dia de la fecha actual en tres variables tipo int
                year = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                //creamos el dialogDatePicker
                DatePickerDialog datePicker = new DatePickerDialog(MatriculaMainActivity2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etfecha.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, year, mes, dia);

                // se muestra el cuadro de dialogo
                datePicker.show();
            }
        });
        btconsultarmat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Consultar_documento();
            }
        });
    }//Fin metodo oncreate

    private void Consultar_documento() {
        codigo = etcodigo.getText().toString();
        if (!codigo.isEmpty()) {
            db.collection(coleccion)
                    .whereEqualTo("Codigo_Matricula", codigo)//se añade linea para ir a  realizar  busqueda individual
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override // Override quiere decir que viene de otra clase
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0) {
                                    //Si encontró al menos un registro
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        //Guardando el HashMap del documento
                                        clave = document.getId();
                                        etfecha.setText(document.getString("Fecha_Matricula"));
                                        etcarnet.setText(document.getString("Carnet"));
                                        codigo_materia = document.getString("Materia");
                                        etcodmateria.setText(document.getString("Materia"));
                                        tvnombre.setText(document.getString("Nombre"));
                                        tvmostrarnombrec.setText(document.getString("Carrera"));
                                        //tvmostrarnombrec.setText(document.getString("Nombre_Estudiante"));
                                        tvmostrarnombrem.setText(document.getString("NombreM"));
                                        tvcreditos.setText(document.getString("Creditos"));
                                        btanular.setEnabled(true);
                                        cbactivo.setEnabled(false);
                                        if (document.getString("Activo").equals("SI"))
                                            cbactivo.setChecked(true);
                                        else
                                            cbactivo.setChecked(false);
                                    }
                                } else {
                                    clave = "";
                                    Toast.makeText(MatriculaMainActivity2.this, "Código de matricula no hallado", Toast.LENGTH_SHORT).show();
                                    //btfecha.setEnabled(true);
                                    etfecha.setEnabled(true);
                                    etfecha.requestFocus();
                                    etcarnet.setEnabled(true);
                                    btconsulcarnet.setEnabled(true);
                                    btconsulmateria.setEnabled(true);
                                    btadicionar.setEnabled(true);
                                }
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Codigo de matricula es requerido", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
    } // Fin Consultar_documento

    public void Consultar_carnet(View view) {
        carnet = etcarnet.getText().toString();
        if (!carnet.isEmpty()) {
            db.collection(coleccion1)
                    .whereEqualTo("Carnet", carnet)//se añade linea para ir a  realizar  busqueda individual
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override // Override quiere decir que viene de otra clase
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {
                                    //Si encontró al menos un registro
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        //Guardando el HashMap del documento
                                        clave = document.getId();
                                        etcarnet.setText(document.getString("Carnet"));
                                        tvnombre.setText(document.getString("Nombre"));
                                        tvmostrarnombrec.setText(document.getString("Carrera"));
                                        etcodmateria.setEnabled(true);
                                        btconsulmateria.setEnabled(true);
                                        etcodmateria.requestFocus();
                                    }
                                } else {
                                    clave = "";
                                    Toast.makeText(MatriculaMainActivity2.this, "Código de carnet no hallado", Toast.LENGTH_SHORT).show();
                                    Limpiar_Campos(); // Limpia los campos en caso de que no se encuentre el carnet
                                }
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                                Toast.makeText(MatriculaMainActivity2.this, "Error al obtener documentos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(this, "Codigo de carnet es requerido", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
    } // Fin Consultar_carnet

    public void Consultar_materia(View view) {
        codigo_materia = etcodmateria.getText().toString();
        if (!codigo_materia.isEmpty()) {
            db.collection(coleccion2)
                    .whereEqualTo("Materia", codigo_materia)//se añade linea para ir a  realizar  busqueda individual
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override // Override quiere decir que viene de otra clase
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0) {
                                    //Si encontró al menos un registro
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        //Guardando el HashMap del documento
                                        clave = document.getId();
                                        etcodmateria.setText(document.getString("Materia"));
                                        tvmostrarnombrem.setText(document.getString("NombreM"));
                                        tvcreditos.setText(document.getString("Creditos"));
                                        btadicionar.setEnabled(true);
                                        if (document.getString("Activo").equals("SI"))
                                            cbactivo.setChecked(true);
                                        else
                                            cbactivo.setChecked(false);
                                    }
                                } else {
                                    clave = "";
                                    Toast.makeText(MatriculaMainActivity2.this, "Código de materia no hallado", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Codigo de materia es requerido", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
    }// Fin Consultar_materia

    public void Adicionar(View view) {
        nombre_carrera = tvmostrarnombrec.getText().toString();
        nombre_estudiante = tvnombre.getText().toString();
        nombre_materia = tvmostrarnombrem.getText().toString();
        codigo = etcodigo.getText().toString();
        carnet = etcarnet.getText().toString();
        codigo_materia = etcodmateria.getText().toString();
        fecha = etfecha.getText().toString();
        nombree= tvnombre.getText().toString();
        creditos= tvcreditos.getText().toString();

        if (!codigo.isEmpty() && !carnet.isEmpty() && !codigo_materia.isEmpty() && !fecha.isEmpty()) {
            Map<String, Object> matricula = new HashMap<>();
            matricula.put("Fecha_Matricula", fecha);
            matricula.put("Materia", codigo_materia);
            matricula.put("NombreM", nombre_materia);
            matricula.put("Codigo_Matricula", codigo);
            matricula.put("Carnet", carnet);
            matricula.put("Carrera", nombre_carrera);
            matricula.put("Nombre", nombree);
            matricula.put("Creditos", creditos);
            matricula.put("Activo", "SI");

            db.collection(coleccion)
                    .add(matricula)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MatriculaMainActivity2.this, "Documento guardado", Toast.LENGTH_SHORT).show();
                                    Limpiar_Campos();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Manejar errores aquí si es necesario
                        }
                    });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MatriculaMainActivity2.this, "Los datos son requeridos", Toast.LENGTH_SHORT).show();
                    etcarnet.requestFocus();
                }
            });
        }


    // Otros métodos y parte del código no modificados...
}//Fin método adicionar
    public  void Anular(View view){
        Map<String, Object> matricula = new HashMap<>();
        matricula.put("Fecha_Matricula", fecha);
        matricula.put("Materia", codigo_materia);
        matricula.put("NombreM", nombre_materia);
        matricula.put("Codigo_Matricula", codigo);
        matricula.put("Carnet", carnet);
        matricula.put("Carrera", nombre_carrera);
        matricula.put("Nombre", nombree);
        matricula.put("Creditos", creditos);
        matricula.put("Activo", "No");
        db.collection(coleccion).document(clave)                   //Cambien el nombre de user por un string que cree coleccion
                .set(matricula)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MatriculaMainActivity2.this,"Documento Anulado...",Toast.LENGTH_SHORT).show();
                        Limpiar_Campos();
                        cbactivo.setEnabled(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MatriculaMainActivity2.this,"Error anulando documento..",Toast.LENGTH_SHORT).show();
                    }
                });

    }//Fin método Anular


    public void Regresar(View view){
    Intent intMatricula =new Intent(this, Opciones.class);
    startActivity(intMatricula);
    }
    //Fin metodo regresar



    public void Limpiar(View view){
        Limpiar_Campos();
    }//Fin metodo Limpiar

    private void Limpiar_Campos() {
        etcodigo.setEnabled(true);
        btanular.setEnabled(false);
        etfecha.setText("fecha");
        etfecha.setEnabled(false);
        //btfecha.setEnabled(false);
        etcodmateria.setText("");
        etcodigo.setText("");
        etcarnet.setText("");
        etcodmateria.setEnabled(false);
        btconsulmateria.setEnabled(false);
        etcarnet.setEnabled(false);
        btconsulcarnet.setEnabled(false);
        tvmostrarnombrem.setText("");
        tvmostrarnombrec.setText("");
        tvcreditos.setText("");
        tvnombre.setText("");
        etcodigo.setText("");
        etcodigo.requestFocus();
        cbactivo.setChecked(false);
        btadicionar.setEnabled(false);
    }// Fin metodo limpiar_Campos
}