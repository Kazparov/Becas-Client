package com.example.che.cliente.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.che.cliente.R;
import com.example.che.cliente.domain.Beca;
import com.example.che.cliente.interfaces.RecyclerViewOnClickListenerHack;

import java.util.List;

/**
 * Created by Che on 05/06/2015.
 */
public class BecaAdapter extends RecyclerView.Adapter<BecaAdapter.MyViweHolder>{

    private List<Beca> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;


    public BecaAdapter(Context c, List<Beca> l){
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViweHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //Cuando se crea un view
        Log.i("LOG", "oncreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_beca, viewGroup,false);
        MyViweHolder mvh = new MyViweHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViweHolder myViweHolder, int position) {
        //Insertar los valores
        Log.i("LOG", "onBindViewHolder()");
         //.view.setImageResource(mList.get(position).getPhoto());
        myViweHolder.tvNombreBeca.setText(mList.get(position).getNombre());
        myViweHolder.tvPaisBeca.setText(mList.get(position).getPais());
        myViweHolder.tvEntidadBeca.setText(mList.get(position).getEntidad());

        try{
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(myViweHolder.itemView);
        }catch(Exception e){
            Log.i("LOG","Error con la animacion");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setmRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }

    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }


    public class MyViweHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //public ImageView ivB;
        public TextView tvNombreBeca;
        public TextView tvPaisBeca;
        public TextView tvEntidadBeca;


        public MyViweHolder(View itemView) {
            super(itemView);
            tvNombreBeca = (TextView) itemView.findViewById(R.id.tv_nombre_beca);
            tvPaisBeca = (TextView) itemView.findViewById(R.id.tv_pais_beca);
            tvEntidadBeca = (TextView) itemView.findViewById(R.id.tv_entidad_beca);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }

    }
}
