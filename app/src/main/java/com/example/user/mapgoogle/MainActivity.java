package com.example.user.mapgoogle;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 17/06/2017.
 */

public final class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnGroundOverlayClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener,
        /*GoogleMap.InfoWindowAdapter,*/
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMyLocationButtonClickListener {

    //private static final int TRANSPARENCY_MAX = 100;

    private static final LatLng PARIS = new LatLng(48.886517, 2.37105);     //coordonnées Paris (latitude, longitude)

    /*private static final LatLng NEAR_NEWARK =
            new LatLng(NEWARK.latitude - 0.001, NEWARK.longitude - 0.025);*/
    private static final LatLng logo_franprix3 = new LatLng(48.886517, 2.371049);
    private static final LatLng logo_franprix4 = new LatLng(48.885461 , 2.369635);
    private static final LatLng logo_franprix5 = new LatLng(48.884332, 2.377017);
    private static final LatLng logo_franprix6 = new LatLng(48.880127, 2.360494);
    private static final LatLng logo_franprix7 = new LatLng(48.887492, 2.367103);
    private static final LatLng logo_franprix8 = new LatLng(48.894857, 2.367404);
    private static final LatLng logo_franprix9 = new LatLng(48.898778, 2.381351);
    private static final LatLng logo_franprix10 = new LatLng(48.878179, 2.371566);
    private static final LatLng logo_franprix11 = new LatLng(48.882808, 2.384999);
    private static final LatLng logo_franprix12 = new LatLng(48.881143, 2.392209);
   /* private static final LatLng logo_franprix3 = new LatLng(48.886517, 2.371049);
    private static final LatLng logo_franprix3 = new LatLng(48.886517, 2.371049);*/

    private GoogleMap mMap = null;
    private Marker mSelectedMarker;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();
    private com.google.android.gms.maps.model.GroundOverlay mGroundOverlay;
    private com.google.android.gms.maps.model.GroundOverlay mGroundOverlayRotated;
    //public boolean isMapToolbarEnabled;
    private int mCurrentEntry = 0; //google cluster



    private ClusterManager<Person> mClusterManager;
    private Random mRandom = new Random(1984);

    // private TextView textName;

    int width;
    int height;
    int padding;


    InfoMarkerFragment infoMarkerFragment;


    private class PersonRenderer extends DefaultClusterRenderer<Person> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;
        private Context context;




        public PersonRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);
            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }
        /*@Override
        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(person.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));


            //textName.setText(mClusterManager.);
        }*/

        /*@Override
        protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Person p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.profilePhoto);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }*/

       /* @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }*/
    }

    /*@Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        //String firstName = cluster.getItems().iterator().next().name;

        Toast.makeText(this, cluster.getSize() + " including", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;

    }*/

   /* @Override
    public void onClusterInfoWindowClick(Cluster<Person> cluster) {
    }

    @Override
    public boolean onClusterItemClick(Person person) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Person person) {

    }*/





    private void addItems() {
        View view = LayoutInflater.from(this).inflate(R.layout.info_window, null, false);


        // http://www.flickr.com/photos/sdasmarchives/5036248203/
        mClusterManager.addItem(new Person(new LatLng(48.886517  , 2.37105), view, R.drawable.logo_franprix3));

        // http://www.flickr.com/photos/usnationalarchives/4726917149/
        mClusterManager.addItem(new Person(new LatLng(48.884332, 2.377017), view, R.drawable.newark_nj_1922));

        // http://www.flickr.com/photos/nypl/3111525394/
        mClusterManager.addItem(new Person(new LatLng(48.894857, 2.367404), view, R.drawable.logo_franprix3));

        // http://www.flickr.com/photos/smithsonian/2887433330/
        mClusterManager.addItem(new Person(new LatLng(48.887492, 2.367103), view, R.drawable.logo_franprix3));

        // http://www.flickr.com/photos/library_of_congress/2179915182/
        mClusterManager.addItem(new Person(new LatLng(48.898778, 2.381351), view, R.drawable.logo_franprix3));

        // http://www.flickr.com/photos/nationalmediamuseum/7893552556/
        mClusterManager.addItem(new Person(new LatLng(48.878179, 2.371566), view, R.drawable.logo_franprix3));

        // http://www.flickr.com/photos/sdasmarchives/5036231225/
        mClusterManager.addItem(new Person(new LatLng(48.882808, 2.384999), view, R.drawable.logo_franprix3));

        // http://www.flickr.com/photos/anmm_thecommons/7694202096/
        mClusterManager.addItem(new Person(new LatLng(48.881143, 2.392209), view, R.drawable.logo_franprix3));

        // http://www.flickr.com/photos/usnationalarchives/4726892651/
        //mClusterManager.addItem(new Person(position(), "Teach", R.drawable.logo_franprix3));
    }

    /*public LatLng position() {
        return new LatLng(random(51.6723432, 51.38494009999999), random(0.148271, -0.3514683));
    }
    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }*/








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        padding = (int) (width * 0.12); // offset from edges of the map 12% of screen


        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        infoMarkerFragment = new InfoMarkerFragment();
        ft.add(R.id.containerInfoMarker, infoMarkerFragment)
                //.addToBackStack("")
                .commit();

    }
    protected GoogleMap getMap() {
        return mMap;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Register a listener to respond to clicks on GroundOverlays.
        mMap = map;
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        // mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().isMapToolbarEnabled();

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        //mMap.getUiSettings().setCompassEnabled(true);

        // Hide the zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().isMapToolbarEnabled();
        // Add lots of markers to the map.
        addMarkersToMap();

        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        mMap.setOnMapClickListener(this);

        mMap.setOnInfoWindowClickListener(this);
        //mMap.setInfoWindowAdapter(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localized.
        map.setContentDescription("Demo showing how to close the info window when the currently"
                + " selected marker is re-tapped.");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(logo_franprix3)
                .include(logo_franprix4)
                .include(logo_franprix5)
                .include(logo_franprix6)
                .include(logo_franprix7)
                .include(logo_franprix8)
                .include(logo_franprix9)
                .include(logo_franprix10)
                .include(logo_franprix11)
                .include(logo_franprix12)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height,padding));
        map.setOnGroundOverlayClickListener(this);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(PARIS, 11));

        mImages.clear();
        /*mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.newark_nj_1922));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.newark_prudential_sunny));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3));*/


        //Resources res = getResources();
        //Drawable drawable = res.getDrawable(R.drawable.ic_logo_franprix);

        // Add a small, rotated overlay that is clickable by default
        // (set by the initial state of the checkbox.)
        /*mGroundOverlayRotated = map.addGroundOverlay(new GroundOverlayOptions().image(mImages.get(2)).anchor(0, 1)
                .position(new LatLng(48.886517, 2.37105), 30f, 30f));*/
        //.bearing(30)                                                                                                  //l'inclinaison de l'image
        //.clickable(((CheckBox) findViewById(R.id.toggleClickability)).isChecked())
        ;


        // Add a large overlay at Newark on top of the smaller overlay.
        /*mGroundOverlay = map.addGroundOverlay(new GroundOverlayOptions()
                .image(mImages.get(mCurrentEntry)).anchor(0, 1)
                .position(NEWARK, 8600f, 6500f));*/

        //mTransparencyBar.setOnSeekBarChangeListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        map.setContentDescription("Google Map with ground overlay.");
        //getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.886517, -2.37105), 9.5f));

        /*mClusterManager = new ClusterManager<Person>(this, getMap());
        mClusterManager.setRenderer(new PersonRenderer());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();*/

    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    //ARRETE D'EFFACER MAINTENANT'


    /*public void onMapSearch(View view) {

        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }

    }*/

    private void addMarkersToMap() {
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix3)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix4)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix5)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix6)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix7)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix8)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix9)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix10)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix11)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));
        mMap.addMarker(new MarkerOptions()
                .position(logo_franprix12)
                /*.title("Franprix")
                .snippet("Magasin pas cher")*/
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_franprix3)));

    }


    @Override
    public void onMapClick(final LatLng point) {
        // Any showing info window closes when the map is clicked.
        // Clear the currently selected marker.
        mSelectedMarker = null;
    }


    @Override
    public void onInfoWindowClick(Marker logo_franprix3) {
        /*Toast.makeText(this, "Tu as cliquer sur la fenêtre d'info",
                Toast.LENGTH_SHORT).show();*/
    }


    @Override
    public void onGroundOverlayClick(com.google.android.gms.maps.model.GroundOverlay groundOverlay) {

    }


    @Override
    public boolean onMarkerClick(final Marker marker) {



        infoMarkerFragment.onClickMarker();

        // The user has re-tapped on the marker which was already showing an info window.
        if (marker.equals(mSelectedMarker)) {
            // The showing info window has already been closed - that's the first thing to happen
            // when any marker is clicked.
            // Return true to indicate we have consumed the event and that we do not want the
            // the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).


            mSelectedMarker = null;
            return true;
        }

        mSelectedMarker = marker;

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur.
        return false;
    }


    /*@Override
    public View getInfoWindow(Marker marker) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_window, null, false);
        View view = LayoutInflater.from(this).inflate(R.layout.info_window, null, false);
        return view;
    }*/

    /*@Override
    public View getInfoContents(Marker marker) {

        return null;
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    //@Override
    // public void onStopTrackingTouch(SeekBar seekBar) {
    //}

    //@Override
    //public void onStartTrackingTouch(SeekBar seekBar) {
    // }

    //@Override
    //public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    //  if (mGroundOverlay != null) {
    //    mGroundOverlay.setTransparency((float) progress / (float) TRANSPARENCY_MAX);
    //  }
    //}

    /*public void switchImage(View view) {
        mCurrentEntry = (mCurrentEntry + 1) % mImages.size();
        mGroundOverlay.setImage(mImages.get(mCurrentEntry));
    }*/




}









































