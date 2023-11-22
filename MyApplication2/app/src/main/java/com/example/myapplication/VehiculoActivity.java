package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class VehiculoActivity extends AppCompatActivity {

    EditText jetplaca, jetmarca, jetmodelo, jetvalor;
    CheckBox jcbactivo;
    Button jbtguardar,jbtanular;

    //esto me permite vincularme con la BD Concesionario.db establecer conexion
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);
    String placa,marca,modelo,valor;

    long respuesta;
    boolean sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);
        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar objetos Java a objetos Xml
        jetplaca=findViewById(R.id.etplaca);
        jetmarca=findViewById(R.id.etmarca);
        jetmodelo=findViewById(R.id.etmodelo);
        jetvalor=findViewById(R.id.etvalor);
        jcbactivo=findViewById(R.id.cbactivo);
        jbtguardar=findViewById(R.id.btguardar);
        jbtanular=findViewById(R.id.btanular);
        sw=false;
        jetplaca.requestFocus();
        Toast.makeText(this, "Digite Placa y luego click en Buscar", Toast.LENGTH_LONG).show();
    } //fin metodo onCreate
    public void Consultar(View view){
        placa=jetplaca.getText().toString();
        //Validar que se digito una placa
        if(!placa.isEmpty()){
            //ingresa a leer la base de datos
            SQLiteDatabase db=admin.getReadableDatabase();
            //Aqui va a buscar los registro en la tabla por medio de la primary key
            Cursor registro=db.rawQuery("select * from TblVehiculo where Placa='"+placa+"'", null);
            //ingresa y pregunta si los datos estan, entonces muestre la información consultada, en caso contratrio indique que el cliente no esta registrado
            if (registro.moveToNext()){
                sw=true;
                Toast.makeText(this, "cliente Registrado", Toast.LENGTH_LONG).show();
                jbtanular.setEnabled(true);
                jetmarca.setText(registro.getString(1));
                jetmodelo.setText(registro.getString(2));
                jetvalor.setText(registro.getString(3));
                if (registro.getString(4).equals("Si"))
                    jcbactivo.setChecked(true);
                else
                    jcbactivo.setChecked(false);
            }else{
                jcbactivo.setChecked(true);
                Toast.makeText(this, "Cliente no esta Registrado", Toast.LENGTH_LONG).show();
            }
            //permitiendo que los usuarios interactúen con él y realicen acciones según el comportamiento normal de ese elemento en la interfaz de usuario.
            jetmarca.setEnabled(true);
            jetmodelo.setEnabled(true);
            jetvalor.setEnabled(true);
            jetmarca.requestFocus();
            jbtguardar.setEnabled(true);
            jetmarca.setEnabled(true);
            db.close();
        }else{
            Toast.makeText(this, "La identificación es requerida", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
    }//fin metodo Consultar
    public void Guardar(View view){
        //Validar que los campos no esten vacios
        placa=jetplaca.getText().toString();
        modelo=jetmodelo.getText().toString();
        marca=jetmarca.getText().toString();
        valor=jetvalor.getText().toString();
        if (placa.isEmpty() || modelo.isEmpty() || marca.isEmpty() || valor.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }else{
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            //Llenar el contenedor
            registro.put("Placa",placa);
            registro.put("Modelo",modelo);
            registro.put("Marca",marca);
            registro.put("Valor",valor);
            //Guardar el registro en la tabla de la base de datos
            if (sw == false)
                respuesta=db.insert("TblVehiculo",null,registro);
            else {
                respuesta = db.update("TblVehiculo", registro, "Placa='" + placa + "'", null);
                sw=false;
            }
            if (respuesta > 0 ){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else{
                Toast.makeText(this, "Registro no guardado", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    } //fin del metodo Guardar

    public void Anular(View view){

        SQLiteDatabase db=admin.getWritableDatabase();
        ContentValues fila=new ContentValues();
        if (jcbactivo.isChecked())
            fila.put("activo","No");
        else
            fila.put("activo","Si");
        respuesta=db.update("TblVehiculo",fila,"Placa='"+placa+"'",null);
        if (respuesta > 0){
            Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
            Limpiar_campos();
        }else {
            sw = false;
            Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }//fin metodo anular

    public void Limpiar(View view){
        Limpiar_campos();
    }//fin metodo Limpiar

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }//fin metodo Regresar

    private void Limpiar_campos(){
        jetplaca.setText("");
        jetmodelo.setText("");
        jetmarca.setText("");
        jetvalor.setText("");
        jcbactivo.setChecked(false);
        jetplaca.setEnabled(true);
        jetplaca.requestFocus();
        jetmodelo.setEnabled(false);
        jetmarca.setEnabled(false);
        jetvalor.setEnabled(false);
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        sw=false;
    }//fin del metodo Limpiar_campos
}