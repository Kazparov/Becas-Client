package com.example.che.cliente;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.che.cliente.WebServices.ws_becasGT.IWsdl2CodeEvents;
import com.example.che.cliente.WebServices.ws_becasGT.ws_becasGT;
import com.example.che.cliente.adapters.BecaAdapter;
import com.example.che.cliente.domain.Beca;
import com.example.che.cliente.interfaces.RecyclerViewOnClickListenerHack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends ActionBarActivity implements RecyclerViewOnClickListenerHack, IWsdl2CodeEvents {

    private String searchWord;
    ProgressDialog dialog;
    SearchView sv;

    protected List<Beca> mList; //Lista de Becas desplegadas
    private RecyclerView mReciclerView; //Para mostrar las becas.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Verificando si existia ya la frase!.
        if(savedInstanceState != null){
            searchWord = savedInstanceState.getParcelable("idBeca");
        }else{
            Bundle bundle = getIntent().getExtras();
            searchWord = bundle.getString("idBeca");
        }


        //Creado el recicler!
        mReciclerView = (RecyclerView) findViewById(R.id.rv_list);
        mReciclerView.setHasFixedSize(true);
        mReciclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mReciclerView.getLayoutManager();
                BecaAdapter adapter = (BecaAdapter) mReciclerView.getAdapter();
            }
        });


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mReciclerView.setLayoutManager(llm);


        //Configurando SearchView
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv = (SearchView) findViewById(R.id.sv_busqueda);
        sv.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        sv.setIconifiedByDefault(false);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //mostrarMensaje(s + " submit");
                searchWord(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //Cambiando Color de text en el hint
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setTextColor(Color.BLACK);
        //Cambiando el Hint
        sv.setQueryHint("Busqueda de Becas");

        //Obteniendo la frase a buscar inicial.
        searchWord(searchWord);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){


        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showInfo(ArrayList<Beca> l){
        mList = l;
        BecaAdapter adapter = new BecaAdapter(this, mList);
        adapter.setmRecyclerViewOnClickListenerHack(this);
        mReciclerView.setAdapter(adapter);
    }

    public void processInfoBecas(String json){
        JSONObject obj = null;
        ArrayList<Beca> items = new ArrayList();
        //Toast.makeText(this, json, Toast.LENGTH_LONG).show();
        json = reemplazar_caracteres_especiales2(json);
        try {
            obj = new JSONObject(json);
            JSONArray arr = obj.getJSONArray("array");

            if(arr.length() != 0){
                for (int i = 0; i < arr.length(); i++){
                    JSONObject temp = arr.getJSONObject(i);
                    items.add(new Beca(temp.getInt("id_beca"), temp.getString("nombre_beca"), temp.getString("nombre_pais"), temp.getString("nombre_oferente")));
                }
                showInfo(items);
            }else{
                Toast.makeText(this, "No hay Becas con que coincidan con la busqueda!", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
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

    public String reemplazar_caracteres_especiales(String cadena){

        cadena = cadena.replace("á","-1001-");
        cadena = cadena.replace("é","-1002-");
        cadena = cadena.replace("í","-1003-");
        cadena = cadena.replace("ó","-1004-");
        cadena = cadena.replace("ú","-1005-");
        cadena = cadena.replace("ñ","-1006-");
        cadena = cadena.replace("ü","-1007-");
        cadena = cadena.replace("\"","-1008-");
        cadena = cadena.replace(",","-1009-");
        cadena = cadena.replace("Á","-1010-");
        cadena = cadena.replace("É","-1011-");
        cadena = cadena.replace("Í","-1012-");
        cadena = cadena.replace("Ó","-1013-");
        cadena = cadena.replace("Ú","-1014-");
        cadena = cadena.replace("Ñ","-1015-");
        cadena = cadena.replace("Ü","-1016-");
        return cadena;
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        //dialog = ProgressDialog.show(this, "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        processInfoBecas(Data.toString());
        //if(dialog != null && dialog.isShowing()){
          //  dialog.dismiss();
        //}
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {

    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        //dialog.dismiss();
    }

    @Override
    public void onClickListener(View view, int position) {
        //Toast.makeText(this, "Posicion :" + position, Toast.LENGTH_SHORT).show();
        //Accion que pasa al darle CLic a un item
        Intent intent = new Intent(this, Descripcion.class);
        intent.putExtra("idBeca", mList.get(position).getIdBeca());
        startActivity(intent);
    }

    public void searchWord(String s){
        try {
            new ws_becasGT(this).get_info_search_becasAsync("{\"word\":\"" + reemplazar_caracteres_especiales(s) + "\"}");
        } catch (Exception e) {
            //Log.i("LOG", e.toString);
            e.printStackTrace();
            Toast.makeText(this, "No se pudo obtener la iformacion", Toast.LENGTH_LONG).show();
        }
    }
}
