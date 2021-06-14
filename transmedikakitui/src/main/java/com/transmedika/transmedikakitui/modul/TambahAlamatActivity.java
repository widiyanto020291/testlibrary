package com.transmedika.transmedikakitui.modul;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.base.BaseBindingActivity;
import com.transmedika.transmedikakitui.contract.TambahAlamatContract;
import com.transmedika.transmedikakitui.databinding.ActivityTambahAlamatBinding;
import com.transmedika.transmedikakitui.models.DataManager;
import com.transmedika.transmedikakitui.models.bean.BaseDropDown;
import com.transmedika.transmedikakitui.models.bean.json.Alamat;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.models.bean.json.param.AlamatParam;
import com.transmedika.transmedikakitui.presenter.TambahAlamatPresenter;
import com.transmedika.transmedikakitui.utils.Constants;
import com.transmedika.transmedikakitui.utils.HideUtil;
import com.transmedika.transmedikakitui.utils.MsgUiUtil;
import com.transmedika.transmedikakitui.utils.RxUtil;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;
import com.transmedika.transmedikakitui.widget.NetkromButton;
import com.transmedika.transmedikakitui.widget.NetkromEditText;
import com.transmedika.transmedikakitui.widget.WidgetUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TambahAlamatActivity extends BaseBindingActivity<ActivityTambahAlamatBinding, TambahAlamatContract.View, TambahAlamatPresenter>
    implements TambahAlamatContract.View{

    private static final String TAG = TambahAlamatActivity.class.getSimpleName();
    private BottomSheetBehavior<View> sheetBehavior;
    private GoogleMap gMap = null;
    private Marker gpsMe = null;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private LatLng mMarkerLocation;
    private Boolean mRequestingLocationUpdates = false;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private SignIn signIn;
    private TambahAlamatAdapter adapter;
    private final List<BaseDropDown> alamats = new ArrayList<>();
    private BaseDropDown addressType;
    private String flag;
    private Alamat alamat;
    private boolean gotoMyLocation = false;
    private HideUtil hideUtil;

    @NonNull
    @NotNull
    @Override
    protected TambahAlamatContract.View getBaseView() {
        return this;
    }

    @Override
    protected ActivityTambahAlamatBinding getViewBinding(@NonNull @NotNull LayoutInflater inflater) {
        return ActivityTambahAlamatBinding.inflate(inflater);
    }

    @Override
    protected void onViewCreated(Bundle bundle) {
        mPresenter = new TambahAlamatPresenter(DataManager.getDataManagerInstance(mContext));
        super.onViewCreated(bundle);
        binding.mapView.onCreate(bundle);
        signIn = mPresenter.selectLogin();
        Bundle b = getIntent().getBundleExtra(Constants.DATA_USER);
        if(b!=null) {
            flag = b.getString(Constants.FLAG);
            alamat = b.getParcelable(Constants.DATA);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initEventAndData(Bundle bundle) {
        setToolBar();
        setUpListener();
        setBottomSheet();
        setDataAlamat();
        setRv();
        initPlace();
        setHide();
        addressType = new BaseDropDown(getString(R.string.alamat_lain), Constants.ALAMAT_LAIN, R.drawable.ic_tambah_alamat);
        binding.mapView.getMapAsync(googleMap -> {
            gMap = googleMap;
            CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(-6.896171,  107.637755)); // cikutra default
            gMap.moveCamera(point);
            mapListener();
            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(mContext);
            gMap.setInfoWindowAdapter(customInfoWindow);

            init();
            gMap.getUiSettings().setZoomControlsEnabled(false);
            gMap.getUiSettings().setMyLocationButtonEnabled(false);
            mPresenter.checkPermissionLocation(new RxPermissions(this), mContext);
        });
        MapsInitializer.initialize(mContext);
        binding.mapView.onResume();
    }

    private void mapListener(){
        gMap.setOnMarkerClickListener(onMarkerClickListener);
        gMap.setOnInfoWindowClickListener(onInfoWindowClickListener);
        gMap.setOnMarkerDragListener(onDragMarker);
        gMap.setOnMapClickListener(onMapClickListener);
        gMap.setOnCameraMoveListener(onCameraMoveListener);
        gMap.setOnCameraIdleListener(onCameraIdleListener);
        gMap.setOnCameraMoveStartedListener(onCameraMoveStartedListener);
    }

    private void setUpListener(){
        binding.btnTerapkan.setOnClickListener(v -> {
            if(mMarkerLocation!=null) {
                AlamatParam param = new AlamatParam();
                param.setAddressType(addressType.getId());
                param.setAddress(Objects.requireNonNull(binding.inputAlamat.etAlamat.getText()).toString());
                param.setMapLat(String.valueOf(mMarkerLocation.latitude));
                param.setMapLng(String.valueOf(mMarkerLocation.longitude));
                param.setNote(Objects.requireNonNull(binding.inputAlamat.etNote.getText()).toString());
                if (flag.equals(Constants.FLAG_ADD)) {
                    mPresenter.tambahAlamat(signIn.getaUTHTOKEN(), param, mContext);
                } else if(flag.equals(Constants.FLAG_EDIT)){
                    param.setId(alamat.getId());
                    mPresenter.ubahAlamat(signIn.getaUTHTOKEN(), String.valueOf(alamat.getId()), param, mContext);
                }else {
                    Alamat alamat = new Alamat();
                    alamat.setNote(binding.inputAlamat.etNote.getText().toString());
                    alamat.setAddress(binding.inputAlamat.etAlamat.getText().toString());
                    alamat.setMapLng(String.valueOf(mMarkerLocation.longitude));
                    alamat.setMapLat(String.valueOf(mMarkerLocation.latitude));
                    setResult(alamat);
                }
            }else {
                MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                        getString(R.string.gps_tidak_ditemukan),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
            }
        });

        binding.inputAlamat.etNote.setOnClickListener(v -> {
            View view = View.inflate(mContext, R.layout.dialog_note, null);
            NetkromEditText mEdNoteD = view.findViewById(R.id.ed_note_d);
            NetkromButton btnKonfirmasi = view.findViewById(R.id.btn_konfirmasi);
            mEdNoteD.setText(Objects.requireNonNull(binding.inputAlamat.etNote.getText()).toString());
            BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
            btnKonfirmasi.setOnClickListener(vi -> {
                TransmedikaUtils.toggleSoftKeyBoard(TambahAlamatActivity.this, true);
                binding.inputAlamat.etNote.setText(mEdNoteD.getText());
                dialog.dismiss();
            });
            dialog.setContentView(view);
            dialog.show();
        });

        binding.fab.setOnClickListener(v -> {
            gotoMyLocation = true;
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), Constants.ZOOM), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    hideUtil.show();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

                @Override
                public void onCancel() {
                    hideUtil.show();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        });
    }

    private void setHide(){
        hideUtil = new HideUtil(binding.llSearch,binding.viewMain, false);
    }

    private void setToolBar(){
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbar.getTvTitle().setVisibility(View.GONE);
    }

    private void initPlace(){
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if(autocompleteFragment!=null && autocompleteFragment.getView()!=null) {
            EditText editText = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
            autocompleteFragment.setHint(getString(R.string.cari_lokasi));
            editText.setTextColor(ContextCompat.getColor(mContext, R.color.textDefault));
            editText.setPadding(0, 0, 0, 0);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.small_text));
            if(transmedikaSettings.getFontRegular()!=null)
                WidgetUtils.setCustomFont(mContext, transmedikaSettings.getFontRegular(), editText);
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,
                    Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG));

            ImageView searchIcon = (ImageView) ((LinearLayout) autocompleteFragment.getView()).getChildAt(0);
            Drawable searchIc = AppCompatResources.getDrawable(mContext, R.drawable.ic_search);
            if(searchIc!=null)
                searchIc.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.gray, null), PorterDuff.Mode.SRC_IN);
            searchIcon.setImageDrawable(searchIc);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    gotoMyLocation = true;
                    mMarkerLocation = new LatLng(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude);
                    addCustomMarker(mMarkerLocation);
                }

                @Override
                public void onError(@NonNull Status status) {
                    Log.i("LOKASI", "An error occurred: " + status);
                }
            });
        }
    }

    private void setDataEdit(){
        binding.llLoading.setVisibility(View.GONE);
        if(alamat.getMapLat()!=null && alamat.getMapLng()!=null) {
            mMarkerLocation = new LatLng(Double.parseDouble(alamat.getMapLat()),
                    Double.parseDouble(alamat.getMapLng()));
        }
        binding.inputAlamat.etNote.setText(alamat.getNote());
        for (int i = 0 ; i < alamats.size() ; i++){
            BaseDropDown drop = alamats.get(i);
            if(drop.getId().equals(alamat.getAddressType())){
                adapter.setSelected(i);
                adapter.notifyDataSetChanged();
                addressType = drop;
                break;
            }
        }
    }

    private void setRv(){
        adapter = new TambahAlamatAdapter(mContext, alamats);
        binding.rv.setItemAnimator(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setAdapter(adapter);
        adapter.setOnItemClickListener(b -> addressType = b);
    }

    private void setDataAlamat(){
        alamats.add(new BaseDropDown(getString(R.string.alamat_lain), Constants.ALAMAT_LAIN, R.drawable.ic_alamat_lain));
        alamats.add(new BaseDropDown(getString(R.string.alamat_rumah), Constants.ALAMAT_RUMAH, R.drawable.ic_alamat_rumah));
        alamats.add(new BaseDropDown(getString(R.string.alamat_kantor), Constants.ALAMAT_KANTOR, R.drawable.ic_alamat_kantor));
    }

    private final GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            mMarkerLocation = latLng;
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarkerLocation, Constants.ZOOM), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    if(gpsMe!=null) {
                        gpsMe.setPosition(latLng);
                        gotoMyLocation = false;
                    }
                }

                @Override
                public void onCancel() {}
            });
        }
    };

    private final GoogleMap.OnCameraMoveStartedListener onCameraMoveStartedListener = new GoogleMap.OnCameraMoveStartedListener() {
        @Override
        public void onCameraMoveStarted(int i) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            hideUtil.hide();
        }
    };

    private final GoogleMap.OnCameraMoveListener onCameraMoveListener = new GoogleMap.OnCameraMoveListener() {
        @Override
        public void onCameraMove() {
            RxUtil.runOnUi(o -> {
                if(mMarkerLocation!=null && gpsMe!=null && !gotoMyLocation) {
                    gpsMe.setPosition(gMap.getCameraPosition().target);
                }
            });

        }
    };

    private final GoogleMap.OnCameraIdleListener onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
        @Override
        public void onCameraIdle() {
            if(mMarkerLocation!=null && gpsMe!=null && !gotoMyLocation) {
                mMarkerLocation = gMap.getCameraPosition().target;
                setAlamat(mMarkerLocation);
                gotoMyLocation = false;
            }else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                hideUtil.show();
            }
        }
    };

    private final GoogleMap.OnMarkerClickListener onMarkerClickListener = marker -> {
        zoomToMarker(marker);
        return true;
    };
    private final GoogleMap.OnInfoWindowClickListener onInfoWindowClickListener = this::zoomToMarker;
    private final GoogleMap.OnMarkerDragListener onDragMarker = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {
            hideUtil.hide();
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            gotoMyLocation = true;
        }

        @Override
        public void onMarkerDrag(Marker marker) {}

        @Override
        public void onMarkerDragEnd(Marker marker) {
            zoomToMarker(marker);
        }
    };

    private void zoomToMarker(Marker marker){
        mMarkerLocation = marker.getPosition();
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), Constants.ZOOM), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {gotoMyLocation = false;}

            @Override
            public void onCancel() {}
        });
    }

    private void setAlamat(LatLng mMarkerLocation){
        RxUtil.runOnUi(o -> {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(mMarkerLocation.latitude, mMarkerLocation.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                /*String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();*/
                binding.inputAlamat.etAlamat.setText(address);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                hideUtil.show();
            } catch (IOException e) {
                MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                        getString(R.string.alamat_tidak_ditemuakn),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
                e.printStackTrace();
            }
        });
    }

    private void findLoc(boolean b){
        if(b){
            binding.fab.hide();
        }else {
            binding.fab.show();
        }
        binding.llLoading.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mSettingsClient = LocationServices.getSettingsClient(mContext);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                findLoc(false);
                mCurrentLocation = locationResult.getLastLocation();

                if(mMarkerLocation == null) {
                    if(flag.equals(Constants.FLAG_ADD)) {
                        binding.tvTitle.setText(R.string.tambah_alamat);
                        mMarkerLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    }else if(flag.equals(Constants.FLAG_EDIT)) {
                        binding.tvTitle.setText(R.string.ubah_alamat);
                        setDataEdit();
                    }else {
                        binding.tvTitle.setText(R.string.alamat_pengiriman);
                        mMarkerLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                        binding.rv.setVisibility(View.GONE);
                    }
                    updateLocationUI();
                }
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.LOCATION_CHANGE_INTERVAL_);
        mLocationRequest.setFastestInterval(Constants.LOCATION_CHANGE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void updateLocationUI() {
        if (mMarkerLocation != null) {
            addCustomMarker(mMarkerLocation);
        }
    }

    private MarkerOptions markerOptions(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.icon(bitmapDescriptorFromVector(mContext));
        return markerOptions;
    }


    private void addCustomMarker(LatLng latLng) {
        if (gpsMe != null) {
            gpsMe.remove();
        }

        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.ZOOM), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                gpsMe = gMap.addMarker(markerOptions(latLng));
                gpsMe.setTag(signIn);
                setAlamat(latLng);
                gotoMyLocation = false;
            }

            @Override
            public void onCancel() {
                gpsMe = gMap.addMarker(markerOptions(latLng));
                gpsMe.setTag(signIn);
            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context) {
        Drawable vectorDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_marker);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, Objects.requireNonNull(vectorDrawable).getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private final OnSuccessListener<LocationSettingsResponse> locationSettingsResponseOnSuccessListener = new OnSuccessListener<LocationSettingsResponse>() {
        @SuppressLint("MissingPermission")
        @Override
        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            findLoc(true);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    };

    private final OnFailureListener onFailureListener = e -> {
        int statusCode = ((ApiException) e).getStatusCode();
        switch (statusCode) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    ResolvableApiException rae = (ResolvableApiException) e;
                    rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sie) {
                    Log.i(Constants.TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                MsgUiUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0),
                        getString(R.string.setting_change_unavailable),mContext, R.drawable.ic_info, R.color.red, Snackbar.LENGTH_LONG);
        }
        updateLocationUI();
    };

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponseOnSuccessListener)
                .addOnFailureListener(this, onFailureListener);
    }

    public void stopLocationUpdates() {
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, task -> { });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestingLocationUpdates) {
            stopLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getMyLocation() {
        gMap.setMyLocationEnabled(true);
        binding.llLoading.setVisibility(View.GONE);
        mRequestingLocationUpdates = true;
        startLocationUpdates();
    }

    @Override
    public void tambahAlamatResp(BaseResponse<Alamat> response) {
        setResult(response.getData());
    }

    @Override
    public void ubahAlamatResp(BaseResponse<Alamat> response) {
        setResult(response.getData());
    }

    private void setResult(Alamat alamat){
        Bundle b = new Bundle();
        b.putParcelable(Constants.DATA, alamat);
        b.putString(Constants.FLAG, flag);
        Intent i = new Intent();
        i.putExtra(Constants.DATA_USER, b);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    private void setBottomSheet(){
        binding.bottomSheet.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        sheetBehavior.setHideable(false);
        sheetBehavior.setPeekHeight(TransmedikaUtils.dip2px(mContext, 60));
        sheetBehavior.addBottomSheetCallback(bottomSheetCallBack);
    }

    private final BottomSheetBehavior.BottomSheetCallback bottomSheetCallBack = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int i) {
            switch (i) {
                case BottomSheetBehavior.STATE_HIDDEN:
                case BottomSheetBehavior.STATE_EXPANDED:
                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_DRAGGING:
                case BottomSheetBehavior.STATE_SETTLING:
                case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    break;
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {
            Log.d("ON_SLIDE", String.valueOf(v));
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.e(Constants.TAG, "User agreed to make required location settings changes.");
                    if (mRequestingLocationUpdates) {
                        startLocationUpdates();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e(Constants.TAG, "User chose not to make required location settings changes.");
                    mRequestingLocationUpdates = false;
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bottomSheetCallBack!=null)
            sheetBehavior.removeBottomSheetCallback(bottomSheetCallBack);
    }
}
