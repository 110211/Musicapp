package com.huce.hma.common.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.huce.hma.R;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicThumbnailWithInfoComponent extends LinearLayout {
    private ImageView thumbnail;
    private TextView title;
    private TextView desc;
    public MusicThumbnailWithInfoComponent(@NonNull Context context) {
        super(context);
        init();
    }

    @SuppressLint({"CustomViewStyleable", "ResourceType"})
    public MusicThumbnailWithInfoComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        @SuppressLint("Recycle") TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.MusicThumbnailWithInfoComponent);
        String name = typeArr.getString(R.styleable.MusicThumbnailWithInfoComponent_title);
        String description = typeArr.getString(R.styleable.MusicThumbnailWithInfoComponent_desc);
        thumbnail.setImageDrawable(AppCompatResources.getDrawable(context, R.styleable.MusicThumbnailWithInfoComponent_thumbnail));

        // Set the values to the components
        title.setText(name);
        desc.setText(description);
    }

    public MusicThumbnailWithInfoComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init() {
        // Set orientation to vertical
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.5); // Set the desired proportion, e.g., half of the width

        this.setLayoutParams(layoutParams);

        setOrientation(LinearLayout.VERTICAL);

        // Create ImageView for thumbnail
        thumbnail = new ImageView(getContext());
        LayoutParams thumbnailParams = new LayoutParams(180, 180);
        thumbnailParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.3);
        thumbnailParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.15);
        thumbnailParams.setMargins(16,16,16,16);

        thumbnail.setLayoutParams(thumbnailParams);
        thumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        thumbnail.setPadding(5, 5, 5, 5); // Adjust padding as needed
        thumbnail.setBackgroundResource(R.drawable.st_rounded_corner);
        thumbnail.setClipToOutline(true);
//        thumbnail.setBackgroundResource(R.drawable.rounded_corner); // Add a drawable for rounded corners
        this.setGravity(Gravity.CENTER);
        addView(thumbnail);

        // Create TextView for title
        title = new TextView(getContext());
        LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        title.setLayoutParams(titleParams);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(android.R.color.darker_gray)); // Set text color
        addView(title);

        // Create TextView for description
        desc = new TextView(getContext());
        LayoutParams descParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        desc.setLayoutParams(descParams);
        desc.setGravity(Gravity.CENTER);
        desc.setTextColor(getResources().getColor(android.R.color.darker_gray)); // Set text color
        addView(desc);
    }
}
