package com.huce.hma.common.component;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huce.hma.R;

public class PopupFragment {
    public static final int ERROR = 0;
    public static final int SUCCESS = 1;
    public static final int WARNING = 2;
    public static final int NOTIFICATION = 3;

    Activity activity;
    AlertDialog alertDialog;
    Button btnAgree;
    Button btnClose;
    TextView txtTitle;
    TextView txtDescription;
    ImageView ivThumbnail;

    int popupType;
    String popupTitle;
    String popupDescription;

    OnShow onshow;
    public PopupFragment(Activity activity, int popupType, String title, String description, OnShow onshow) {
        this.activity = activity;
        this.popupType = popupType;
        this.popupTitle = title;
        this.popupDescription = description;
        this.onshow = onshow;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.component_common_popup, null));

        alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();

//        btnAgree = alertDialog.findViewById(R.id.popupAgreeButton);
        btnClose = alertDialog.findViewById(R.id.popupCloseButton);
        txtTitle = alertDialog.findViewById(R.id.popupTitle);
        txtDescription = alertDialog.findViewById(R.id.popupDescription);
        ivThumbnail = alertDialog.findViewById(R.id.popupThumbnailIcon);

        txtTitle.setText(popupTitle);
        txtDescription.setText(popupDescription);

//        btnAgree.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                onshow.onAgree();
//            }
//        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                onshow.onClose();
            }
        });

        switch (popupType) {
            // error
            case 0:
//                btnAgree.setVisibility(View.INVISIBLE);
                ivThumbnail.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.error_p));
                break;
            // success
            case 1:
                ivThumbnail.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.success_p));
                break;
            // warning
            case 2:
                ivThumbnail.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.warnning_p));
                break;
            // noti
            case 3:
                ivThumbnail.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.notification_p));
                break;

            default:
                break;
        }

    }

    public interface OnShow {
//        default void onAgree() {}
        default void onClose() {}
    }
}

