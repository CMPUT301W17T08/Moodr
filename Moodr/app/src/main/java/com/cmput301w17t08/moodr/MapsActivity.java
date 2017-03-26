package com.cmput301w17t08.moodr;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            GoogleMap.OnMarkerDragListener,
            GoogleMap.OnMapLongClickListener,
            GoogleMap.OnMarkerClickListener,
            View.OnClickListener {

        private static final String TAG = "MapsActivity";
        private GoogleMap mMap;
        private double longitude;
        private double latitude;
        private GoogleApiClient googleApiClient;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            //Initializing googleApiClient
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMapOptions.mapType(googleMap.MAP_TYPE_HYBRID)
            //    .compassEnabled(true);

            // Add a marker in Sydney and move the camera
            LatLng edmonton = new LatLng(53,-113);
            mMap.addMarker(new MarkerOptions().position(edmonton).title("Marker in Edmonton"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(edmonton));
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMapLongClickListener(this);
        }





        //Getting current location
        private void getCurrentLocation() {
            mMap.clear();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                //moving the map to location
                moveMap();
            }
        }

        private void moveMap() {
            /**
             * Creating the latlng object to store lat, long coordinates
             * adding marker to map
             * move the camera with animation
             */
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(true)
                    .title("Marker in Edmonton"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mMap.getUiSettings().setZoomControlsEnabled(true);


        }

        @Override
        public void onClick(View view) {
            Log.v(TAG,"view click event");
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            getCurrentLocation();
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        public void onMapLongClick(LatLng latLng) {
            // mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        }

        @Override
        public void onMarkerDragStart(Marker marker) {
            Toast.makeText(MapsActivity.this, "onMarkerDragStart", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            Toast.makeText(MapsActivity.this, "onMarkerDrag", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            // getting the Co-ordinates
            latitude = marker.getPosition().latitude;
            longitude = marker.getPosition().longitude;

            //move to current position
            moveMap();
        }

        @Override
        protected void onStart() {
            googleApiClient.connect();
            super.onStart();
        }

        @Override
        protected void onStop() {
            googleApiClient.disconnect();
            super.onStop();
        }


        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(MapsActivity.this, "onMarkerClick", Toast.LENGTH_SHORT).show();
            return true;
        }

    }