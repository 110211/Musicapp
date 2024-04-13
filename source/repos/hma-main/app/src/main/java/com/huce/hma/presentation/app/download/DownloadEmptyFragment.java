package com.huce.hma.presentation.app.download;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huce.hma.R;

public class DownloadEmptyFragment extends Fragment {
    private String type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.component_app_music_download_empty, container, false);
        TextView title = view.findViewById(R.id.textView5);
        TextView desc = view.findViewById(R.id.textView6);

        title.setText(title.getText().toString().replace("{{type}}", type));
        desc.setText(desc.getText().toString().replace("{{type}}", type));
        return view;
    }

    public DownloadEmptyFragment(String type) {
        this.type = type;
    }
}
