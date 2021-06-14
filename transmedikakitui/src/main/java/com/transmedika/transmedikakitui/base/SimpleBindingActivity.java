package com.transmedika.transmedikakitui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;


import com.transmedika.transmedikakitui.utils.TransmedikaSettings;
import com.transmedika.transmedikakitui.utils.TransmedikaUtils;

import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created by Widiyanto02 on 1/4/2018.
 */

public abstract class SimpleBindingActivity<T extends ViewBinding> extends SupportActivity {
    protected abstract T getViewBinding(@NonNull LayoutInflater inflater);
    protected void onViewCreated(Bundle bundle) {}
    protected abstract void initEventAndData(Bundle bundle);
    protected Activity mContext;
    protected TransmedikaSettings transmedikaSettings;
    protected T binding;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mContext = this;
        binding = getViewBinding(getLayoutInflater());
        setContentView(binding.getRoot());
        transmedikaSettings = TransmedikaUtils.transmedikaSettings(mContext);
        onViewCreated(bundle);
        initEventAndData(bundle);
        //getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_RIGHT);
        //checkConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //CheckAvailableNetwork.setConnectivityListener(this);
    }

    /*@Override
    public boolean swipeBackPriority() {
        return super.swipeBackPriority();
    }

    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
    }*/

    /*@Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showMsgNetwork(isConnected);
    }

    private void checkConnection(){
        boolean isConnected = CheckAvailableNetwork.isConnected(getApplicationContext());
        showMsgNetwork(isConnected);
    }

    private void showMsgNetwork(boolean isConnected){
        String msg = isConnected ? networkAvailable:networkUnAvailable;
        if(!isConnected) {
            MsgUiUtil.show(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), msg);
        }
        Log.d(Constants.TAG, "Network Available : "+msg);
    }*/
}
