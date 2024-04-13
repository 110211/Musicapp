package com.huce.hma.presentation.app.profile.reportbug;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.huce.hma.R;
import com.huce.hma.common.component.BaseActivity;
import com.huce.hma.presentation.app.home.HomeActivity;
import com.huce.hma.presentation.guest.login.LoginActivity;
import com.huce.hma.presentation.guest.register.RegisterActivity;

public class ReportBugActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
    private Spinner spn_type_of_report;
    private Button btn_send_rp;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_report_bug);

        btn_send_rp = findViewById(R.id.btn_send_rp);
        spn_type_of_report = findViewById(R.id.spn_type_report);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_of_bug, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spn_type_of_report.setAdapter(adapter);
        spn_type_of_report.setOnItemSelectedListener(this);


        btn_send_rp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ReportBugActivity.this, "Gửi yêu cầu thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReportBugActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
