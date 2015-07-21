package com.example.che.cliente.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.che.cliente.Descripcion;
import com.example.che.cliente.R;
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

/**
 * Created by Che on 04/06/2015.
 */
public class BecaFragment extends Fragment implements RecyclerViewOnClickListenerHack, IWsdl2CodeEvents{
    private RecyclerView mReciclerView;
    protected List<Beca> mList;
    private ProgressDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pais, container, false);

        mReciclerView = (RecyclerView) view.findViewById(R.id.rv_list);
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


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mReciclerView.setLayoutManager(llm);


        Log.i("LOG", "se realiza la llamada a Peticion de becas");

        //Peticion de becas
        ws_becasGT ws = new ws_becasGT(this);
        try {
            ws.get_info_basica_becasAsync();
        } catch (Exception e) {
            //Log.i("LOG", e.toString);
            e.printStackTrace();
            Toast.makeText(getActivity(), "No se pudo obtener las becas", Toast.LENGTH_LONG).show();
        }

        return view;
    }


    public void processInfoBecas(String json){
        JSONObject obj = null;
        ArrayList<Beca> items = new ArrayList();
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
                Toast.makeText(getActivity(), "No hay Becas 1", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showInfo(ArrayList<Beca> l){
        mList = l;
        BecaAdapter adapter = new BecaAdapter(getActivity(), mList);
        adapter.setmRecyclerViewOnClickListenerHack(this);
        mReciclerView.setAdapter(adapter);
    }


    @Override
    public void onClickListener(View view, int position) {
        Toast.makeText(getActivity(), "Posicion :" + position, Toast.LENGTH_SHORT).show();
        //Accion que pasa al darle CLic a un item
        Intent intent = new Intent(getActivity(), Descripcion.class);
        intent.putExtra("idBeca", mList.get(position).getIdBeca());
        getActivity().startActivity(intent);
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getActivity(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        //Acciones que realizamos al recibir la data
        processInfoBecas(Data.toString());
        dialog.dismiss();

    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {

    }

    @Override
    public void Wsdl2CodeEndedRequest() {

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
