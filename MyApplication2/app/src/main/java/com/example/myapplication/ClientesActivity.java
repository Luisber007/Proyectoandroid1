package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

public class ClientesActivity extends AppCompatActivity {

    EditText jetidentificacion, jetnombre, jetdireccion, jettelefono;
    CheckBox jcbactivo;
    Button jbtguardar, jbtanular;
    //establece la conexion con las clases
    ClsOpenHelper admin = new ClsOpenHelper(this, "Concesionario.db", null, 1);

    //creacion de variables solo en esa clase pero las puedo usar donde yo quiera en esta clase
    String identificacion, nombre, direccion, telefono;

    long respuesta;
    boolean sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        getSupportActionBar().hide();
        //Asociar objetos Java a objetos Xml
        jetidentificacion = findViewById(R.id.etidentificacion);
        jetnombre = findViewById(R.id.etnombre);
        jetdireccion = findViewById(R.id.etdireccion);
        jettelefono = findViewById(R.id.etteléfono);
        jcbactivo = findViewById(R.id.cbactivo);
        jbtanular = findViewById(R.id.btanular);
        jbtguardar = findViewById(R.id.btguardar);
        sw=false;
        jetidentificacion.requestFocus();
        Toast.makeText(this, "Digite identificación y luego Click en Buscar", Toast.LENGTH_LONG).show();

    }//fin del metodo oncreate

    public void Consultar(View view) {
        identificacion = jetidentificacion.getText().toString();
        //Validar que se digitó una identificación
        if (!identificacion.isEmpty()) {
            SQLiteDatabase db = admin.getReadableDatabase();//estoy consultando la base de datos llamada db
            //en esta linea estoy realizando una concatenación de datos, el selext * indica que va a traer todos los datos de la tabla,
            // null indica que esta apuntando antes del primer registro
            Cursor registro = db.rawQuery("select * from TblCliente where Ident_cliente='" + identificacion + "'", null);
            if (registro.moveToNext()) {
                sw=true;
                Toast.makeText(this, "cliente Registrado", Toast.LENGTH_SHORT).show();
                jbtanular.setEnabled(true);
                jetnombre.setText(registro.getString(1));
                jetdireccion.setText(registro.getString(2));
                jettelefono.setText(registro.getString(3));
                if (registro.getString(4).equals("Si"))
                    jcbactivo.setChecked(true);
                    else
                        jcbactivo.setChecked(false);
            } else {
                jcbactivo.setChecked(true);
                Toast.makeText(this, "Cliente no Registrado", Toast.LENGTH_LONG).show();
            }
            jetnombre.setEnabled(true);
            jetdireccion.setEnabled(true);
            jettelefono.setEnabled(true);
            jetnombre.requestFocus();
            jbtguardar.setEnabled(true);
            jetidentificacion.setEnabled(true);
            db.close();//aqui le estoy diciendo que voy a cerrar la base de datos


        } else {
            Toast.makeText(this, "Identificacion es requerida", Toast.LENGTH_LONG).show();
            jetidentificacion.requestFocus();
        }
    }//fin metodo consultar
    public void Guardar(View view){
        identificacion=jetidentificacion.getText().toString();
        nombre=jetnombre.getText().toString();
        direccion=jetdireccion.getText().toString();
        telefono=jettelefono.getText().toString();
        //Validar que la informacion este completa
        if (!nombre.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty()){
            SQLiteDatabase db=admin.getWritableDatabase();//operacion de escritura
            //este es un nuevo contenedor
            //se esta llevando la informacion a la memoria ram
            ContentValues fila= new ContentValues();
            //en esta linea se define quien va con quien "se lleno el contenedor"
            fila.put("Ident_cliente", identificacion);
            fila.put("Nom_cliente", nombre);
            fila.put("Dir_cliente", direccion);
            fila.put("Tel_cliente", telefono);
            //Guardar el registro en la base de datos
            if (sw == false)
                respuesta=db.insert("TblCliente", null, fila);
            else{
                sw=false;
                respuesta=db.update("TblCliente", fila, "Ident_cliente='"+identificacion+"'", null);
            }
            if (respuesta > 0){
                Toast.makeText(this, "Registro Guardado", Toast.LENGTH_SHORT).show();
                Cancelar();
            }else
                Toast.makeText(this, "cliente no registrado", Toast.LENGTH_SHORT).show();
            db.close();

        }else{
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetnombre.requestFocus();
        }
    }//Fin metodo guardar

    public void Anular(View view){
        SQLiteDatabase db=admin.getReadableDatabase();
        ContentValues fila=new ContentValues();
        if (jcbactivo.isChecked())
            fila.put("activo", "No");
        else
            fila.put("activo", "Si");
        respuesta=db.update("Tblcliente", fila, "Ident_cliente='"+identificacion+"'", null);
        if (respuesta > 0){
            Toast.makeText(this, "Registro Anulado", Toast.LENGTH_LONG).show();
            Cancelar();
        }else{
            sw=false;
            Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
        }

        db.close();

    }// Fin de método Anular
    public void Listar(View view){
        Intent intlistar=new Intent(this, listarClientesActivity.class);
        startActivity(intlistar);

    }//fin metodo listar
    public void Regresar(View View) {
        Intent intmenu = new Intent(this, MainActivity.class);
        startActivity(intmenu);
    }//Fin metodo regrsar

    private void Cancelar(){
        jetidentificacion.setText("");
        jettelefono.setText("");
        jetnombre.setText("");
        jetdireccion.setText("");
        jetidentificacion.setEnabled(true);
        jetidentificacion.requestFocus();
        jcbactivo.setChecked(false);
        jetdireccion.setEnabled(false);
        jetnombre.setEnabled(false);
        jettelefono.setEnabled(false);
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        sw=false;
    }//Fin metodo Cancelar

    public void Limpiar(View view){
        Cancelar();
    }//fin del método limpiar
}