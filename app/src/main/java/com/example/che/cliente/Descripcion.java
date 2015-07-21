package com.example.che.cliente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.che.cliente.WebServices.ws_becasGT.IWsdl2CodeEvents;
import com.example.che.cliente.WebServices.ws_becasGT.ws_becasGT;
import com.example.che.cliente.domain.Beca;

import org.json.JSONArray;
import org.json.JSONObject;


public class Descripcion extends ActionBarActivity implements IWsdl2CodeEvents {
    private Beca beca;
    private int idBeca;
    private ProgressDialog dialog;
    private TextView tv_nombre_beca, tv_pais_beca, tv_entidad_beca, tv_tipo_beca;
    private TextView tv_descripcion_beca;
    private TextView tv_requisitos_beca;
    private TextView tv_solicitud_beca, tv_fecha_inicio_beca, tv_fecha_fin_beca, tv_direccion, tv_telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Enlace de TextView's
        tv_nombre_beca = (TextView) findViewById(R.id.tv_nombre_beca);
        tv_pais_beca = (TextView) findViewById(R.id.tv_pais_beca);
        tv_entidad_beca = (TextView) findViewById(R.id.tv_entidad_beca);
        tv_tipo_beca = (TextView) findViewById(R.id.tv_tipo_beca);
        tv_descripcion_beca = (TextView) findViewById(R.id.tv_descripcion_beca);
        tv_requisitos_beca = (TextView) findViewById(R.id.tv_requisitos_beca);
        tv_solicitud_beca = (TextView) findViewById(R.id.tv_solicitud_beca);
        tv_fecha_inicio_beca = (TextView) findViewById(R.id.tv_fecha_inicio_beca);
        tv_fecha_fin_beca = (TextView) findViewById(R.id.tv_fecha_fin_beca);
        tv_direccion = (TextView) findViewById(R.id.tv_direccion_beca);
        tv_telefono = (TextView) findViewById(R.id.tv_telefono_beca);

        //Verificando si existia ya el ID.
        if(savedInstanceState != null){
            idBeca = savedInstanceState.getParcelable("idBeca");
        }else{
            Bundle bundle = getIntent().getExtras();
            idBeca = bundle.getInt("idBeca");
            //Toast.makeText(this, ">"+idBeca + "<", Toast.LENGTH_LONG).show();
        }

        //Obteniendo datos de la beca
        try {
            new ws_becasGT(this).get_info_becaAsync("{\"id_beca\":\"" + idBeca + "\"}");
        } catch (Exception e) {
            //Log.i("LOG", e.toString);
            e.printStackTrace();
            Toast.makeText(this, "No se pudo obtener la iformacion", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_descripcion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        /*
        else if(id == R.id.facebook){
            Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
            if(intent != null){
                Toast.makeText(this, "entra al if", Toast.LENGTH_LONG).show();
                Intent shareIntent = new Intent();
                shareIntent.setType("text/plain");
                shareIntent.setPackage("com.facebook.katana");

                shareIntent.putExtra(Intent.EXTRA_TITLE, "Compartir");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "HEY! revisa esta beca: " + tv_nombre_beca.getText() + " en becasGT!");

                startActivity(shareIntent);
            }else{
                Toast.makeText(this, "Lo siento!!\nNo cuentas con esta aplicacion.", Toast.LENGTH_LONG).show();
            }

        }*/
        else if(id == R.id.twitter){
            Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.twitter.android");
            if(intent != null){
                //Toast.makeText(this, "entra al if", Toast.LENGTH_LONG).show();
                Intent shareIntent = new Intent();
                shareIntent.setType("text/plain");
                shareIntent.setPackage("com.twitter.android");

                shareIntent.putExtra(Intent.EXTRA_TITLE, "Compartir");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "HEY! revisa esta beca: " + tv_nombre_beca.getText() + " en becasGT!");

                startActivity(shareIntent);
            }else{
                Toast.makeText(this, "Lo siento!\nNo cuentas con esta aplicacion.", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(this, "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        processInfo(Data.toString());
        dialog.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {

    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }

    //Obtiene los datos del json.
    public void processInfo(String data){
        try{
            //Toast.makeText(this, ">"+ data + "<", Toast.LENGTH_LONG).show();
            data = reemplazar_caracteres_especiales2(data);
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("100")){
                JSONArray temp =  jso.getJSONArray("array");
                JSONObject temporal = temp.getJSONObject(0);

                //Obtencion de datos
                String nombre_beca = temporal.getString("nombre_beca");
                String pais_beca = temporal.getString("nombre_pais");
                String entidad_beca = temporal.getString("nombre_entidad");
                String tipo_beca = temporal.getString("tipo_beca");
                String descripcion = temporal.getString("descripcion_caracteristicas");
                String requisitos = temporal.getString("descripcion_requisitos");
                String solicitud = temporal.getString("descripcion_solicitud");
                String fecha_inicio = temporal.getString("fecha_inicio");
                String fecha_fin = temporal.getString("fecha_fin");
                String telefono = temporal.getString("telefono");
                String direccion = temporal.getString("url");
                String telefono_extension = temporal.getString("telefono_extension");

                //Verificacio de valores
                if(!nombre_beca.equals("NULL")){
                    tv_nombre_beca.setText(nombre_beca);
                }if(!pais_beca.equals("NULL")){
                    tv_pais_beca.setText(pais_beca);
                }if(!entidad_beca.equals("NULL")){
                    tv_entidad_beca.setText(entidad_beca);
                }if(!tipo_beca.equals("NULL")){
                    tv_tipo_beca.setText(tipo_beca);
                }if(!descripcion.equals("NULL")){
                    tv_descripcion_beca.setText(descripcion);
                }if(!requisitos.equals("NULL")){
                    tv_requisitos_beca.setText(requisitos);
                }if(!solicitud.equals("NULL")){
                    tv_solicitud_beca.setText(solicitud);
                }if(!fecha_inicio.equals("NULL")){
                    tv_fecha_inicio_beca.setText(fecha_inicio);
                }if(!fecha_fin.equals("NULL")){
                    tv_fecha_fin_beca.setText(fecha_inicio);
                }if(!telefono.equals("NULL")){
                    if(!telefono_extension.equals("NULL")){
                        tv_telefono.setText(telefono + " ext (" + telefono_extension + ")");
                    }else{
                        tv_telefono.setText(telefono);
                    }
                }if(!direccion.equals("NULL")){
                    tv_direccion.setText(direccion);
                }
            }else{
                Toast.makeText(this, desc, Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            Log.e("LOG", "get_info_beca: " + e.getMessage());
            Toast.makeText(this, "No se pudieron recuperar los datos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public String reemplazar_caracteres_especiales2(String cadena){

        cadena = cadena.replace("-1001-","á");
        cadena = cadena.replace("-1002-","é");
        cadena = cadena.replace("-1003-","í");
        cadena = cadena.replace("-1004-","ó");
        cadena = cadena.replace("-1005-","ú");
        cadena = cadena.replace("-1006-","ñ");
        cadena = cadena.replace("-1007-","ü");
        cadena = cadena.replace("-1008-","\"");
        cadena = cadena.replace("-1009-",",");
        cadena = cadena.replace("-1010-","Á");
        cadena = cadena.replace("-1011-","É");
        cadena = cadena.replace("-1012-","Í");
        cadena = cadena.replace("-1013-","Ó");
        cadena = cadena.replace("-1014-","Ú");
        cadena = cadena.replace("-1015-","Ñ");
        cadena = cadena.replace("-1016-","Ü");

        return cadena;
    }
}
