package com.huce.hma.common.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.view.LayoutInflater;


import com.huce.hma.R;

public class LoadingDialog {
   Activity activity;
   AlertDialog alertDialog;

   public LoadingDialog(Activity activity) {
       this.activity = activity;
   }

   public void startLoading() {
       AlertDialog.Builder builder = new AlertDialog.Builder(activity);

       LayoutInflater inflater = activity.getLayoutInflater();

       builder.setView(inflater.inflate(R.layout.component_common_loading, null));

       alertDialog = builder.create();
       alertDialog.setCanceledOnTouchOutside(false);

       alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

       alertDialog.show();
   }

   public void stopLoading() {
       alertDialog.dismiss();
   }
}