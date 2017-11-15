package org.eduideas.helpp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int llamarcontacto1 = 1001;
    private static final int llamarcontacto2 = 1002;
    private static final int llamarcontacto3 = 1003;
    private static final int llamarcontacto4 = 1004;
    private static final int llamarcontacto5 = 1005;
    private CheckBox checkBox1;
    final Context context = this;
    private Button suscribirse;
    private TextView numerodeserie;


    TextView nombrecontacto1, telefonocontacto1;
    TextView nombrecontacto2, telefonocontacto2;
    TextView nombrecontacto3, telefonocontacto3;
    TextView nombrecontacto4, telefonocontacto4;
    TextView nombrecontacto5, telefonocontacto5;

    TextView mensaje1;
    TextView mensaje2;
    Button botonHelpp;
    Button seleccionarcontacto1;
    Button seleccionarcontacto2;
    Button seleccionarcontacto3;
    Button seleccionarcontacto4;
    Button seleccionarcontacto5;
    Button borrarcontacto1;
    Button borrarcontacto2;
    Button borrarcontacto3;
    Button borrarcontacto4;
    Button borrarcontacto5;
    SharedPreferences basededatosPref;
    SharedPreferences.Editor editor;

    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        basededatosPref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = basededatosPref.edit();

        nombrecontacto1 = (TextView) findViewById(R.id.nombrecontacto1);
        telefonocontacto1 = (TextView) findViewById(R.id.telefonocontacto1);
        telefonocontacto1.setText(basededatosPref.getString("dbtelefonocontacto1", null));
        nombrecontacto1.setText(basededatosPref.getString("dbnombrecontacto1", "Vacío"));
        nombrecontacto2 = (TextView) findViewById(R.id.nombrecontacto2);
        telefonocontacto2 = (TextView) findViewById(R.id.telefonocontacto2);
        telefonocontacto2.setText(basededatosPref.getString("dbtelefonocontacto2", null));
        nombrecontacto2.setText(basededatosPref.getString("dbnombrecontacto2", "Vacío"));
        nombrecontacto3 = (TextView) findViewById(R.id.nombrecontacto3);
        telefonocontacto3 = (TextView) findViewById(R.id.telefonocontacto3);
        telefonocontacto3.setText(basededatosPref.getString("dbtelefonocontacto3", null));
        nombrecontacto3.setText(basededatosPref.getString("dbnombrecontacto3", "Vacío"));
        nombrecontacto4 = (TextView) findViewById(R.id.nombrecontacto4);
        telefonocontacto4 = (TextView) findViewById(R.id.telefonocontacto4);
        telefonocontacto4.setText(basededatosPref.getString("dbtelefonocontacto4", null));
        nombrecontacto4.setText(basededatosPref.getString("dbnombrecontacto4", "Vacío"));
        nombrecontacto5 = (TextView) findViewById(R.id.nombrecontacto5);
        telefonocontacto5 = (TextView) findViewById(R.id.telefonocontacto5);
        telefonocontacto5.setText(basededatosPref.getString("dbtelefonocontacto5", null));
        nombrecontacto5.setText(basededatosPref.getString("dbnombrecontacto5", "Vacío"));
        mensaje1 = (TextView) findViewById(R.id.coordenadas);
        mensaje1.setMovementMethod(LinkMovementMethod.getInstance());
        mensaje2 = (TextView) findViewById(R.id.direccion);
        mensaje2.setMovementMethod(LinkMovementMethod.getInstance());
        String mensaje1adb = mensaje1.getText().toString();
        editor.putString("mensaje1", mensaje1adb);
        editor.commit();
        String mensaje2adb = mensaje2.getText().toString();
        editor.putString("mensaje2", mensaje2adb);
        editor.commit();

        botonHelpp = (Button) findViewById(R.id.botonHelpp);
        botonHelpp.setOnClickListener(this);

        seleccionarcontacto1 = (Button) findViewById(R.id.seleccionarcontacto1);
        seleccionarcontacto2 = (Button) findViewById(R.id.seleccionarcontacto2);
        seleccionarcontacto3 = (Button) findViewById(R.id.seleccionarcontacto3);
        seleccionarcontacto4 = (Button) findViewById(R.id.seleccionarcontacto4);
        seleccionarcontacto5 = (Button) findViewById(R.id.seleccionarcontacto5);
        borrarcontacto1 = (Button) findViewById(R.id.borrar1);
        borrarcontacto2 = (Button) findViewById(R.id.borrar2);
        borrarcontacto3 = (Button) findViewById(R.id.borrar3);
        borrarcontacto4 = (Button) findViewById(R.id.borrar4);
        borrarcontacto5 = (Button) findViewById(R.id.borrar5);

        checkBox1=(CheckBox)findViewById(R.id.checkBox1);
        checkBox1.setEnabled(false);
        checkBox1.setChecked(basededatosPref.getBoolean("checked", false));
        checkBox1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    editor.putBoolean("checked", true);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Sus mensajes serán enviados al cuadrante de polica más cercano.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    editor.putBoolean("checked", false);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Sus mensajes solo serán enviados a sus contactos seleccionados.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        suscribirse = (Button) findViewById(R.id.suscribirse);
        numerodeserie = (TextView) findViewById(R.id.numerodeserie);
        numerodeserie.setText(basededatosPref.getString("numerodeserie", null));
        suscribirse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Obtener el archivo prompts.xml
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // Definir el archivo prompts.xml como alertdialog
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.ingresarnumerodeserie);

                // Definir el mensaje del alertdialog para el número de serie
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("INGRESAR",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        numerodeserie.setText(userInput.getText());
                                        String numerodeserieadb = numerodeserie.getText().toString();
                                        editor.putString("numerodeserie", numerodeserieadb);
                                        editor.commit();
                                        recreate();
                                    }
                                })
                        .setNegativeButton("CANCELAR",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // crear alertdialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // mostrar el alertdialog
                alertDialog.show();

            }
        });


        //Almacenamos los posibles números de serie, esto debería ir en otro archivo en el futuro
        String numerodeseriedb = basededatosPref.getString("numerodeserie", "");
        if (numerodeseriedb.equals("4c37dbfae76a9a48544d7248127d2d29") || numerodeseriedb.equals("abcd")){
            checkBox1.setEnabled(true);
         //   suscribirse.setVisibility(View.GONE);

        }
        else {
            checkBox1.setEnabled(false);
            editor.putBoolean("checked", false);
            editor.commit();
        }

        //Comprobamos los permisos de envío de SMS
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                // permission is already granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {

        }

        //Comprobamos los permisos de GPS
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        else {
            locationStart();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

        mensaje1.setText("Actualizando ubicación...");
        mensaje2.setText(" ");

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    //Obtenemos la direccion de la calle a partir de la latitud y la longitud
    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    mensaje2.setText(" "
                            + DirCalle.getAddressLine(0));
                    String direccion = mensaje2.getText().toString();
                    editor.putString("mensaje2", direccion);
                    editor.commit();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String Text = "Estoy en problemas, mi ubicacion actual es: " + "\n http://maps.google.com/maps?q="
                    + loc.getLatitude() + "," + loc.getLongitude();
            mensaje1.setText(Text);
            editor.putString("mensaje1", Text);
            editor.commit();

            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            mensaje1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            mensaje1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.botonHelpp:
                sendSMS();
                break;
            default:
                break;
        }

    }

    private void sendSMS() {
        String mensaje1final=basededatosPref.getString("mensaje1", "DEFAULT");
        String mensaje2final=basededatosPref.getString("mensaje2", "DEFAULT");
        String contacto1sms = telefonocontacto1.getText().toString();
        String contacto2sms = telefonocontacto2.getText().toString();
        String contacto3sms = telefonocontacto3.getText().toString();
        String contacto4sms = telefonocontacto4.getText().toString();
        String contacto5sms = telefonocontacto5.getText().toString();

        String contactossms[] = {contacto1sms, contacto2sms, contacto3sms, contacto4sms, contacto5sms};
        List<String> list = new ArrayList<String>();
        for(String s : contactossms) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        contactossms = list.toArray(new String[list.size()]);

        String mensajeacontactos = mensaje1final + " " + mensaje2final;
        SmsManager smsManager = SmsManager.getDefault();

        if (contacto1sms.isEmpty() && contacto2sms.isEmpty() && contacto3sms.isEmpty() && contacto4sms.isEmpty() &&contacto5sms.isEmpty() && checkBox1.isChecked() == false){
            Toast.makeText(getApplicationContext(), "Debe seleccionar al menos un contacto.", Toast.LENGTH_LONG).show();

        } else {
            if (checkBox1.isChecked()==true) {
                for (String contactosfinal : contactossms) {
                    smsManager.sendTextMessage(contactosfinal, null, (mensajeacontactos), null, null);
                    smsManager.sendTextMessage("+573148894999", null, (mensajeacontactos+ " " +contactosfinal), null, null);
                }
                Toast.makeText(getApplicationContext(), "Mensaje enviado.", Toast.LENGTH_LONG).show();
            }
            else {
                for (String contactosfinal : contactossms) {
                    smsManager.sendTextMessage(contactosfinal, null, (mensajeacontactos), null, null);
                    }
                    Toast.makeText(getApplicationContext(), "Mensaje enviado.", Toast.LENGTH_LONG).show();
            }
        }

    }


    public void seleccionarcontacto1(View v) {
         Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(contactPickerIntent, llamarcontacto1);
    }

    public void seleccionarcontacto2(View v) {

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, llamarcontacto2);
    }

    public void seleccionarcontacto3(View v) {

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, llamarcontacto3);
    }
    public void seleccionarcontacto4(View v) {

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, llamarcontacto4);
    }
    public void seleccionarcontacto5(View v) {

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, llamarcontacto5);
    }
    public void borrarcontacto1(View v) {
        editor.remove("dbtelefonocontacto1");
        editor.remove("dbnombrecontacto1");
        editor.commit();
        nombrecontacto1.setText("Vacío");
        telefonocontacto1.setText("");
    }

    public void borrarcontacto2(View v) {
        editor.remove("dbtelefonocontacto2");
        editor.remove("dbnombrecontacto2");
        editor.commit();
        nombrecontacto2.setText("Vacío");
        telefonocontacto2.setText("");
    }

    public void borrarcontacto3(View v) {
        editor.remove("dbtelefonocontacto3");
        editor.remove("dbnombrecontacto3");
        editor.commit();
        nombrecontacto3.setText("Vacío");
        telefonocontacto3.setText("");
    }

    public void borrarcontacto4(View v) {
        editor.remove("dbtelefonocontacto4");
        editor.remove("dbnombrecontacto4");
        editor.commit();
        nombrecontacto4.setText("Vacío");
        telefonocontacto4.setText("");
    }

    public void borrarcontacto5(View v) {
        editor.remove("dbtelefonocontacto5");
        editor.remove("dbnombrecontacto5");
        editor.commit();
        nombrecontacto5.setText("Vacío");
        telefonocontacto5.setText("");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case llamarcontacto1:
                    Cursor cursor = null;
                    try {
                        String contactNumber = null;
                        String contactName = null;
                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        contactNumber = cursor.getString(phoneIndex);
                        contactNumber = contactNumber.replaceAll("[ )(-]","");
                        contactName = cursor.getString(nameIndex);
                        editor.putString("dbtelefonocontacto1", contactNumber); // Storing string
                        editor.putString("dbnombrecontacto1", contactName); // Storing string
                        editor.commit();
                        telefonocontacto1.setText(basededatosPref.getString("dbtelefonocontacto1", null));
                        nombrecontacto1.setText(basededatosPref.getString("dbnombrecontacto1", null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case llamarcontacto2:
              //  Cursor cursor = null;
                try {
                    String contactNumber = null;
                    String contactName = null;
                    Uri uri = data.getData();
                    cursor = getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    int phoneIndex = cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int nameIndex = cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    contactNumber = cursor.getString(phoneIndex);
                    contactNumber = contactNumber.replaceAll("[ )(-]","");
                    contactName = cursor.getString(nameIndex);
                    editor.putString("dbtelefonocontacto2", contactNumber); // Storing string
                    editor.putString("dbnombrecontacto2", contactName); // Storing string
                    editor.commit();
                    telefonocontacto2.setText(basededatosPref.getString("dbtelefonocontacto2", null));
                    nombrecontacto2.setText(basededatosPref.getString("dbnombrecontacto2", null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                case llamarcontacto3:
                    //  Cursor cursor = null;
                    try {
                        String contactNumber = null;
                        String contactName = null;
                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        contactNumber = cursor.getString(phoneIndex);
                        contactNumber = contactNumber.replaceAll("[ )(-]","");
                        contactName = cursor.getString(nameIndex);
                        editor.putString("dbtelefonocontacto3", contactNumber); // Storing string
                        editor.putString("dbnombrecontacto3", contactName); // Storing string
                        editor.commit();
                        telefonocontacto3.setText(basededatosPref.getString("dbtelefonocontacto3", null));
                        nombrecontacto3.setText(basededatosPref.getString("dbnombrecontacto3", null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case llamarcontacto4:
                    //  Cursor cursor = null;
                    try {
                        String contactNumber = null;
                        String contactName = null;
                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        contactNumber = cursor.getString(phoneIndex);
                        contactNumber = contactNumber.replaceAll("[ )(-]","");
                        contactName = cursor.getString(nameIndex);
                        editor.putString("dbtelefonocontacto4", contactNumber); // Storing string
                        editor.putString("dbnombrecontacto4", contactName); // Storing string
                        editor.commit();
                        telefonocontacto4.setText(basededatosPref.getString("dbtelefonocontacto4", null));
                        nombrecontacto4.setText(basededatosPref.getString("dbnombrecontacto4", null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case llamarcontacto5:
                    //  Cursor cursor = null;
                    try {
                        String contactNumber = null;
                        String contactName = null;
                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        contactNumber = cursor.getString(phoneIndex);
                        contactNumber = contactNumber.replaceAll("[ )(-]","");
                        contactName = cursor.getString(nameIndex);
                        editor.putString("dbtelefonocontacto5", contactNumber); // Storing string
                        editor.putString("dbnombrecontacto5", contactName); // Storing string
                        editor.commit();
                        telefonocontacto5.setText(basededatosPref.getString("dbtelefonocontacto5", null));
                        nombrecontacto5.setText(basededatosPref.getString("dbnombrecontacto5", null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
        }
        }
    }

}

