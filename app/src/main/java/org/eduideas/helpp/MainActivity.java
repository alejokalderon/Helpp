package org.eduideas.helpp;

import android.Manifest;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int llamarcontacto1 = 1001;
    private static final int llamarcontacto2 = 1002;
    private static final int llamarcontacto3 = 1003;
    private static final int llamarcontacto4 = 1004;
    private static final int llamarcontacto5 = 1005;


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
    Button borrarcontacto5
            ;
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
        nombrecontacto1.setText(basededatosPref.getString("dbnombrecontacto1", null));
        nombrecontacto2 = (TextView) findViewById(R.id.nombrecontacto2);
        telefonocontacto2 = (TextView) findViewById(R.id.telefonocontacto2);
        telefonocontacto2.setText(basededatosPref.getString("dbtelefonocontacto2", null));
        nombrecontacto2.setText(basededatosPref.getString("dbnombrecontacto2", null));
        nombrecontacto3 = (TextView) findViewById(R.id.nombrecontacto3);
        telefonocontacto3 = (TextView) findViewById(R.id.telefonocontacto3);
        telefonocontacto3.setText(basededatosPref.getString("dbtelefonocontacto3", null));
        nombrecontacto3.setText(basededatosPref.getString("dbnombrecontacto3", null));
        nombrecontacto4 = (TextView) findViewById(R.id.nombrecontacto4);
        telefonocontacto4 = (TextView) findViewById(R.id.telefonocontacto4);
        telefonocontacto4.setText(basededatosPref.getString("dbtelefonocontacto4", null));
        nombrecontacto4.setText(basededatosPref.getString("dbnombrecontacto4", null));
        nombrecontacto5 = (TextView) findViewById(R.id.nombrecontacto5);
        telefonocontacto5 = (TextView) findViewById(R.id.telefonocontacto5);
        telefonocontacto5.setText(basededatosPref.getString("dbtelefonocontacto5", null));
        nombrecontacto5.setText(basededatosPref.getString("dbnombrecontacto5", null));
        mensaje1 = (TextView) findViewById(R.id.coordenadas);
        mensaje1.setMovementMethod(LinkMovementMethod.getInstance());
        mensaje2 = (TextView) findViewById(R.id.direccion);
        mensaje2.setMovementMethod(LinkMovementMethod.getInstance());


        //Este es el ID del botón
        botonHelpp = (Button) findViewById(R.id.botonHelpp);
        //Este es el que permite que el botón ejecute
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

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    mensaje2.setText(" "
                            + DirCalle.getAddressLine(0));
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
                //  Toast.makeText(getApplicationContext(), "Holi", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    private void sendSMS() {
        String smsfinal=mensaje1.getText().toString()+" "+mensaje2.getText().toString();
        String contactosms=telefonocontacto2.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(contactosms, null,(smsfinal), null, null);
        Toast.makeText(getApplicationContext(), "Mensaje enviado.",
        Toast.LENGTH_LONG).show();
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
        nombrecontacto1.setText("");
        telefonocontacto1.setText("");
    }

    public void borrarcontacto2(View v) {
        editor.remove("dbtelefonocontacto2");
        editor.remove("dbnombrecontacto2");
        editor.commit();
        nombrecontacto2.setText("");
        telefonocontacto2.setText("");
    }

    public void borrarcontacto3(View v) {
        editor.remove("dbtelefonocontacto3");
        editor.remove("dbnombrecontacto3");
        editor.commit();
        nombrecontacto3.setText("");
        telefonocontacto3.setText("");
    }

    public void borrarcontacto4(View v) {
        editor.remove("dbtelefonocontacto4");
        editor.remove("dbnombrecontacto4");
        editor.commit();
        nombrecontacto4.setText("");
        telefonocontacto4.setText("");
    }

    public void borrarcontacto5(View v) {
        editor.remove("dbtelefonocontacto5");
        editor.remove("dbnombrecontacto5");
        editor.commit();
        nombrecontacto5.setText("");
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

