package com.example.liua4835.mymapsproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText locationSearch = ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Add a marker at your place of birth and move the camera to it
        // When the marker is tapped, display "Born here"
        LatLng birth = new LatLng(32.7157, -117.1611);
        mMap.addMarker(new MarkerOptions().position(birth).title("Born here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(birth));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MyMapsApp", "Failed FINE Permission Check");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MyMapsApp", "Failed COARSE Permission Check");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);
        }
        locationSearch = (EditText) findViewById(R.id.editText_addr);
    }



    public void onSearch(View v){
        String location = locationSearch.getText().toString();

        List<Address> addressList = null;
        List <Address> addressListzip = null;

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria,false);

        Log.d("MyMapsApp","onSearch: location = " + location);
        Log.d("MyMapsApp","onSearch: provider " + provider);

        LatLng userLocation = null;

        try{
            if(locationManager != null){
                Log.d("MyMapsApp","onSearch: LocationManager is not null");

                if((myLocation = locationManager.getLasKnownLocation(LocationManager.NETWORK_PROVIDER)) != null) {
                    userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude);
                    Log.d("MyMapApp","onSearch: Using NETWORK_PROVIDER userLocation is: " +myLocation.getLatitude() + " " + myLocation.getLongitude());
                    Toast.makeText(this, "UserLoc" + myLocation.getLatitude() + " " + myLocation.getLongitude()).show();
                } else if((myLocation = locationManager.getLasKnownLocation(LocationManager.GPS_PROVIDER))!= null){
                    userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude);
                    Log.d("MyMapApp","onSearch: Using NETWORK_PROVIDER userLocation is: " +myLocation.getLatitude() + " " + myLocation.getLongitude());
                    Toast.makeText(this, "UserLoc" + myLocation.getLatitude() + " " + myLocation.getLongitude();
                }
                else{
                    Log.d("MyMapsApp","onSearch: myLocation is null from getLastKnownLocation");
                }
            }
        }
        catch(SecurityException|IllegalArgumentException e){
            Log.d("MyMapsApp","onSearch:Exception getLastKnownLocation");
            Toast.makeText(this, "onSearch: Exception getLastKnowLocation",Toast.LENGTH_SHORT);
    }
    if(!location.matches("")){
            Log.d("MyMapsApp","onSearch: location field is populated");
            Geocoder geocoder = new Geocoder(this, Locale.US);

            try{
                addressList = geocoder.getFromLocationName(location,100,
                        userLocation.latitude - (5.0/60),
                        userLocation.longitude - (5.0/60),
                        userLocation.latitude + (5.0/60),
                        userLocation.longitude + (5.0/60));
                Log.d("MyMapsApp","onSearch: location field is created");

            }
            catch (IOException e){
                e.printStackTrace();
        }
        if (!addressList.isEmpty()){
            Log.d("MyMapsApp","onSearch: AddressList size is: " + addressList.size());
            for(int i = 0; i<addressList.size();i++){
                Address address = addressList.get(i);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                mMap.addMarker(new MarkerOptions().position(latLng).title(i+ ": " + address.getSubThoroughfare()+address.getSubThoroughfare()));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }

        }
    }
    }
}
