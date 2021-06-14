package com.transmedika.transmedikakitui.modul;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.transmedika.transmedikakitui.R;
import com.transmedika.transmedikakitui.models.bean.json.SignIn;
import com.transmedika.transmedikakitui.widget.CircleImageView;
import com.transmedika.transmedikakitui.widget.NetkromTextView;

import java.text.DecimalFormat;



public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("@#");

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = null;
        if (marker.getTag() instanceof SignIn) {
            view = View.inflate(context, R.layout.item_profile_map, null);

            SignIn userRealm = (SignIn) marker.getTag();
            NetkromTextView mTvName = view.findViewById(R.id.tv_name);
            CircleImageView mImgProfile = view.findViewById(R.id.img_profile);
            if (userRealm != null) {
                mTvName.setText(userRealm.getName());
                /*ImageLoader.load(context, userRealm.getImageProfileUrl(),
                        mImgProfile, R.drawable.empty_img);*/
            }
        }
        return view;
    }
}