package com.example.che.cliente;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.che.cliente.WebServices.ws_becasGT.IWsdl2CodeEvents;
import com.example.che.cliente.WebServices.ws_becasGT.ws_becasGT;
import com.example.che.cliente.adapters.TabsAdapter;
import com.example.che.cliente.domain.Beca;
import com.example.che.cliente.tabs.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Principal extends ActionBarActivity implements IWsdl2CodeEvents{

    //ActionBar actionBar;    //Utilizada para poder controlar la action bar
    private Toolbar toolbar;

    //Tabs
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    //Buscador
    private Menu menu;
    private List<String> items;

    //Mensaje para comprobar conexion
    private ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.logo);

        //Comprobando conexion
        ws_becasGT ws = new ws_becasGT(this);
        try {
            ws.validar_serverAsync();
        } catch (Exception e) {
            //Log.i("LOG", e.toString);
            e.printStackTrace();
            Toast.makeText(this, "No se pudo realizar la conexion con el servidor!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_principal, menu);
        this.menu = menu;


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setQueryHint("Busqueda de Becas..");
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setIconifiedByDefault(false);
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    //mostrarMensaje(s + " submit");
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    intent.putExtra("idBeca", s);
                    startActivity(intent);
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String s) {
                    //mostrarMensaje(s);
                    //loadHistory(s);
                    return false;
                }
            });
        }
        return true;
    }


    private void mostrarMensaje(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.search) {
            //onSearchRequested();
//            return true;
  //      }

        return super.onOptionsItemSelected(item);
    }

    public void continuar(){
        //TABS
        mViewPager = (ViewPager) findViewById(R.id.vp_tabs);
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), this));

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorTabs));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accentColor));
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    private void comprobarConexion(String json){
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
            JSONArray arr = obj.getJSONArray("datos");

            if(arr.length() != 0){
                JSONObject temp = arr.getJSONObject(0);
                if(temp.getInt("Tipo") == 100){
                    continuar();
                }else{
                    mostrarError();
                }
            }else{
                Toast.makeText(this, "No hay conexion", Toast.LENGTH_SHORT).show();
                mostrarError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mostrarError();
        }
    }

    public void mostrarError(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Error de conexión");
        dialogo.setMessage("No se ha podido conectar con el servidor. Comprueba tu conexión a Internet y vuelve a intentarlo.");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Principal.this.finish();
            }
        });
        dialogo.create();
        dialogo.show();
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(this, "Comprobando Conexion", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        comprobarConexion(Data.toString());
        dialog.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        //Toast.makeText(this, "No hay conexion", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }
}


