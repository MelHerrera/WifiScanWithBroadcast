package com.app.wifiscanwithbroadcast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CustomDialog extends AppCompatDialogFragment {
Wifi Ap;
Activity contexto;
    public CustomDialog(Activity Contexto, Wifi Ap)
    {
        this.Ap=Ap;
        this.contexto=Contexto;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder myAlert= new AlertDialog.Builder(contexto);
        myAlert.setTitle(Ap.getSsid());

        View myView =contexto.getLayoutInflater().inflate(R.layout.item_conexion,null,false);
        final EditText edtContra= myView.findViewById(R.id.edtcontra);
        myAlert.setView(myView);

        myAlert.setPositiveButton("Conectar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                conectarAp(edtContra.getText().toString());
            }
        });

        return myAlert.create();
    }

    private void conectarAp(String preSharedKey)
    {
        WifiConfiguration wiConfig = new WifiConfiguration();
        wiConfig.SSID = String.format("\"%s\"", Ap);
        wiConfig.preSharedKey = String.format("\"%s\"", preSharedKey);

        WifiManager wifiManager = (WifiManager)contexto.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        int netId = wifiManager.addNetwork(wiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        if(wifiManager.reconnect())
            Toast.makeText(contexto,"Conectado",Toast.LENGTH_LONG).show();

    }
}
