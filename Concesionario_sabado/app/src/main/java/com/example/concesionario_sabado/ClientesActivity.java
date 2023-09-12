package com.example.concesionario_sabado;

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

    public class ClientesActivity extends AppCompatActivity {

    EditText jetidentificacion,jetnombre,jetdireccion,jettelefono;
    CheckBox jcbactivo;
    Button jbtguardar,jbtanular;
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);
    String identificacion,nombre,direccion,telefono;
    long respuesta;
    boolean sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar objetos Java a objetos Xml
        jetidentificacion=findViewById(R.id.etidentificacion);
        jetnombre=findViewById(R.id.etnombre);
        jetdireccion=findViewById(R.id.etdireccion);
        jettelefono=findViewById(R.id.ettelefono);
        jcbactivo=findViewById(R.id.cbactivo);
        jbtguardar=findViewById(R.id.btguardar);
        jbtanular=findViewById(R.id.btanular);
        sw=false;
        jetidentificacion.requestFocus();
        Toast.makeText(this, "Digite identificacion y luego Click a Buscar", Toast.LENGTH_LONG).show();
    } //fin metodo onCreate

    public void Consultar(View view){
        identificacion=jetidentificacion.getText().toString();
        //Validar que se digito una identificacion
        if (!identificacion.isEmpty()){
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor registro=db.rawQuery("select * from TblCliente where Ident_cliente='"+identificacion+"'",null);
            if (registro.moveToNext()){
                sw=true;
                Toast.makeText(this, "Cliente registrado", Toast.LENGTH_SHORT).show();
                jbtanular.setEnabled(true);
                jetnombre.setText(registro.getString(1));
                jetdireccion.setText(registro.getString(2));
                jettelefono.setText(registro.getString(3));
                if (registro.getString(4).equals("Si"))
                    jcbactivo.setChecked(true);
                else
                    jcbactivo.setChecked(false);
            }else{
                jcbactivo.setChecked(true);
                Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show();
            }
            jetnombre.setEnabled(true);
            jetdireccion.setEnabled(true);
            jettelefono.setEnabled(true);
            jetnombre.requestFocus();
            jbtguardar.setEnabled(true);
            jetidentificacion.setEnabled(false);
            db.close();
        }else{
            Toast.makeText(this, "La identificacion es requerida", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
    }//fin metodo Consultar

    public void Guardar(View view){
        identificacion=jetidentificacion.getText().toString();
        nombre=jetnombre.getText().toString();
        direccion=jetdireccion.getText().toString();
        telefono=jettelefono.getText().toString();
        //Validar que la informacion este completa
        if (!nombre.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty()){
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues fila=new ContentValues();
            //Llenar el contenedor
            fila.put("Ident_cliente",identificacion);
            fila.put("Nom_cliente",nombre);
            fila.put("Dir_cliente",direccion);
            fila.put("Tel_cliente",telefono);
            //Guardar el registro en la base de datos
            if (sw == false )
                respuesta=db.insert("TblCliente",null,fila);
            else{
                sw=false;
                respuesta=db.update("TblCliente",fila,"Ident_cliente='"+identificacion+"'",null);
            }
            if (respuesta > 0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Cancelar();
            }else
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            db.close();
        }else{
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetnombre.requestFocus();
        }
    }//fin metodo Guardar

    public void Anular(View view){
        SQLiteDatabase db=admin.getWritableDatabase();
        ContentValues fila=new ContentValues();
        if (jcbactivo.isChecked())
            fila.put("activo","No");
        else
            fila.put("activo","Si");
        respuesta=db.update("TblCliente",fila,"Ident_cliente='"+identificacion+"'",null);
        if (respuesta > 0){
            Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
            Cancelar();
        }else{
            sw=false;
            Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
        }
        db.close();
    } // Fin metodo Anular

    private void Cancelar()   {
        jetidentificacion.setText("");
        jettelefono.setText("");
        jetnombre.setText("");
        jetdireccion.setText("");
        jetidentificacion.setEnabled(true);
        jetidentificacion.requestFocus();
        jetdireccion.setEnabled(false);
        jetnombre.setEnabled(false);
        jettelefono.setEnabled(false);
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        jcbactivo.setChecked(false);
        sw=false;
    } //fin metodo Cancelar

    public void Limpiar(View view){
        Cancelar();
    }//fin metodo Limpiar

    public void Listar(View view){
        Intent intlistar=new Intent(this,ListarClientesActivity.class);
        startActivity(intlistar);
    }//fin metodo Listar
    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }//fin metodo Regresar
}