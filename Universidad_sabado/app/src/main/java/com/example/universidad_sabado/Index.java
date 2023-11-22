package com.example.universidad_sabado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Index extends AppCompatActivity {

    EditText usuario,clave;

    String Usuario, Clave;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        //ocultar barra de titulo por defecto
        getSupportActionBar().hide();

        usuario = findViewById(R.id.etusuario);
        clave = findViewById(R.id.etclave);
    }

    public void login(View view){

        Usuario = usuario.getText().toString();
        Clave = clave.getText().toString();

        if (Usuario.isEmpty() || Clave.isEmpty()){
            Toast.makeText(this, "Usuario y Contraseña Obligatorios", Toast.LENGTH_LONG).show();
        }else{
            db.collection("usuarios").whereEqualTo("username",Usuario).whereEqualTo("password",Clave).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            Intent intent = new Intent(getApplicationContext(),Opciones.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Index.this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Index.this, "Error de servidor", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

