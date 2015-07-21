package com.example.che.cliente.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.che.cliente.Descripcion;
import com.example.che.cliente.R;
import com.example.che.cliente.WebServices.ws_becasGT.IWsdl2CodeEvents;
import com.example.che.cliente.WebServices.ws_becasGT.ws_becasGT;
import com.example.che.cliente.adapters.EntidadAdapter;
import com.example.che.cliente.domain.Beca;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Che on 04/06/2015.
 */
public class EntidadFragment extends Fragment implements IWsdl2CodeEvents {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private ProgressDialog dialog;
    List<String> listDataHeader;
    HashMap<String, List<Beca>> listDataChild;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expandable_list_prueba, container, false);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        Log.i("LOG", "se realiza la llamada a Peticion de becas");


        //Peticion de becaas
        ws_becasGT ws = new ws_becasGT(this);
        try {
            ws.get_info_basica_becas_entidadAsync();
        } catch (Exception e) {
            //Log.i("LOG", e.toString);
            e.printStackTrace();
            Toast.makeText(getActivity(), "No se pudo obtener las becas", Toast.LENGTH_LONG).show();
        }

        return view;
    }


    /*
     * Preparing the list data
     */

    public void processInfoBecas(String json) {
        JSONObject obj = null;
        String oferenteAnterior;
        String oferenteActual;
        json = reemplazar_caracteres_especiales2(json);
        ArrayList<Beca> items = new ArrayList();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        try {
            obj = new JSONObject(json);
            JSONArray arr = obj.getJSONArray("array");
            if (arr.length() != 0) {
                oferenteAnterior = "";

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject temp = arr.getJSONObject(i);
                    oferenteActual = temp.getString("nombre_oferente");

                    if (oferenteActual.equals(oferenteAnterior)) {
                        items.add(new Beca(temp.getInt("id_beca"), temp.getString("nombre_beca"), temp.getString("nombre_pais"), temp.getString("nombre_oferente")));
                        Log.i("LOG", "Oferente de beca " + oferenteActual);
                    } else {
                        if (items.size() >= 1) {
                            Log.i("LOG", "Se agrego una lista con la etiqueta " + oferenteAnterior);
                            listDataChild.put(oferenteAnterior, items);
                        }
                        items = new ArrayList<>();
                        Log.i("LOG", "Oferente de beca " + oferenteActual);
                        items.add(new Beca(temp.getInt("id_beca"), temp.getString("nombre_beca"), temp.getString("nombre_pais"), temp.getString("nombre_oferente")));
                        listDataHeader.add(oferenteActual);
                        oferenteAnterior = oferenteActual;
                    }
                }
                Log.i("LOG", "Se agrego una lista con la etiqueta " + oferenteAnterior);
                listDataChild.put(oferenteAnterior, items);
                showInfo();

            } else {
                Toast.makeText(getActivity(), "No hay Becas", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showInfo(){
        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getActivity(), listDataHeader.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getActivity(), listDataHeader.get(groupPosition) + " Collapsed",  Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                // Toast.makeText(getActivity(), listDataHeader.get(groupPosition) + " : "
                //        + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), Descripcion.class);
                intent.putExtra("idBeca", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getIdBeca());

                getActivity().startActivity(intent);
                return false;
            }
        });

        listAdapter = new EntidadAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getActivity(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
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