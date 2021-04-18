package com.app.wifiscanwithbroadcast;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class WifiAdapter extends BaseAdapter {

    Activity contexto;
    List<Wifi> lista;

    public WifiAdapter(Activity context, List<Wifi> lista) {
        contexto = context;
        this.lista = lista;
    }


    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       View myView=view;

       if(myView==null)
           myView=contexto.getLayoutInflater().inflate(R.layout.wifi_item,viewGroup,false);

        TextView ssid=myView.findViewById(R.id.txtssid);
        TextView bssid=myView.findViewById(R.id.txtbssid);

        Wifi mywifi=lista.get(i);

        ssid.setText(mywifi.getSsid());
        bssid.setText(mywifi.getBssid());
       return myView;
    }
}
