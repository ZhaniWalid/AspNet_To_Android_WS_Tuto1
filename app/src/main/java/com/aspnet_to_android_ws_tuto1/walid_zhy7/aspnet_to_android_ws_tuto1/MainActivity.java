package com.aspnet_to_android_ws_tuto1.walid_zhy7.aspnet_to_android_ws_tuto1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

     EditText editTextId;
     public TextView textViewName,textViewId;
     Button   btn_getWS ;
    // iniating handler is important
     Handler mHandler = new Handler();
     //
     public  String SOAP_ACTION = "http://AspNet_To_Android_WebService_Tuto.org/FindUserNameById";
     //public  String SOAP_ACTION_2 = "http://AspNet_To_Android_WebService_Tuto.org/ReturnAllNames";
     public  String WSDL_TARGET_NAMESPACE = "http://AspNet_To_Android_WebService_Tuto.org/";
     public  String OPERATION_NAME = "FindUserNameById";
     //public  String OPERATION_NAME_2 = "ReturnAllNames";
     //public  String SOAP_ADDRESS_Local = "http://10.0.2.2:82/WebServices_AspNet_To_Android_Tuto/MyAspNetAndroidWS.asmx";//Local Soap Web Service(.asmx : dotNet) Consumption on Android Emulator
     public  String SOAP_ADDRESS_Remote_Smartphone = "http://192.168.1.90:82/WebServices_AspNet_To_Android_Tuto/MyAspNetAndroidWS.asmx";//http://ipv4_adr:portAccessibleFromIISLocal   //Remote Soap Web Service(.asmx : dotNet) Consumption on Real Android Device
     private SoapObject request;
     private HttpTransportSE httpTransport;
     private SoapSerializationEnvelope envelope;
     Object response=null;

     String IdValue;
     String NameValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId   = (EditText)findViewById(R.id.EdTxtIdWs);
        textViewName = (TextView)findViewById(R.id.TxtVwNameWS);
        //textViewId = (TextView)findViewById(R.id.TxtVwIdWS);
        btn_getWS    = (Button)findViewById(R.id.btnNameWs);

        btn_getWS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getName(view);

                //MyAsyncTask myAsyncTask = new MyAsyncTask();
                //myAsyncTask.execute();
            }
        });

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */
    }

    public void getName(View view){
        //call AsyncTask from here

        String inputId = editTextId.getText().toString();
        String[] params = new String[]{inputId};

        //Enlever le commentaire de la 4eme ligne au dessus au cas ou
        //On va utiliser un Smartphone Android au lieu de l'émulateur
        //ipv4 : 192.168.1.97 -> maybe change every time when we open wifi -> check in cmd with command : "ipconfig"
        //String[] params = new String[]{"10.0.2.2:82"};//localhost:8603

        new MyAsyncTask().execute(params);
        //new MyAsyncTask().execute();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    */

    class MyAsyncTask extends AsyncTask<String,Void,String> {

        public MyAsyncTask() {
        }

        // this method will fetch output from Web Service
        @Override
        protected String doInBackground(String... params) {

            //SOAP_ADDRESS = "http://"+params[0]+"/MyAspNetAndroidWS.asmx";
            request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
            String resultIsSucceeded = "";
            //String inputId = editTextId.getText().toString();
            //IdValue = params[0];
            //NameValue = params[1];

            //params = new String[]{inputId};

           PropertyInfo pi = new PropertyInfo();
            pi.setName("id");
            pi.setValue(Integer.parseInt(params[0]));
            pi.setType(Integer.class);
            request.addProperty(pi);

            //textViewId.setText(pi.getValue().toString());

            /*PropertyInfo pi_1 = new PropertyInfo();
            pi_1.setName("name");
            pi_1.setValue(params[1]);
            pi_1.setType(String.class);
            request.addProperty(pi_1);*/
            //textViewName.setText(pi_1.getValue().toString());

            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            httpTransport = new HttpTransportSE(SOAP_ADDRESS_Remote_Smartphone);
            try{
                // this part will run the Service call
                httpTransport.call(SOAP_ACTION,envelope);
                response = envelope.getResponse();
                resultIsSucceeded = response.toString();
            }catch (Exception exp){
                //response = exp.getMessage();
                resultIsSucceeded=exp.getMessage();
                exp.printStackTrace();
                //isSucceeded="Failed";
            }
            return resultIsSucceeded;
            //String resultValue =  response.toString();
            //return resultValue;
            //return  response.toString();
        }

       /* @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    textViewId.setText(request.getProperty("Id").toString());
                    textViewName.setText(request.getProperty("name").toString());
                }
            });
        }
      */

        // this method will show output on TextView
        @Override
        protected void onPostExecute(final String resultIsSucceeded) {
            // result parameter should be "final" , so that it can be used in cross thread operation
            super.onPostExecute(resultIsSucceeded);
            // we have to use "Async" to make changes on the TextView
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //textViewId.setText(resultIsSucceeded);
                    textViewName.setText(resultIsSucceeded);
                }
            });
        }
    }
}
