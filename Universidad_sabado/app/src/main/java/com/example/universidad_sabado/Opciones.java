package com.example.universidad_sabado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Opciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        //ocultar barra de titulo por defecto
        getSupportActionBar().hide();

    }
    public void Estudiantes(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Materias(View view){
        Intent intent = new Intent(this, Materias.class);
        startActivity(intent);
    }

    public void Salir(View view){
        Intent intent = new Intent(this, Index.class);
        startActivity(intent);
    }

    public void Registro(View view){
        Intent intent = new Intent(this, MatriculaMainActivity2.class);
        startActivity(intent);
    }

}
