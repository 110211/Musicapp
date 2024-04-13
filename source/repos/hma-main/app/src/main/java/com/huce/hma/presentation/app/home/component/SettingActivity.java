package com.huce.hma.presentation.app.home.component;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huce.hma.R;
import com.huce.hma.common.services.JWTTokenManager;
import com.huce.hma.presentation.app.profile.changepassword.ChangePasswordActivity;
import com.huce.hma.presentation.app.profile.reportbug.ReportBugActivity;
import com.huce.hma.presentation.guest.login.LoginActivity;

public class SettingActivity extends AppCompatActivity {
    private Button btn_change_pass;
    private Button btn_contact;
    private Button btn_report_bug;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.component_app_home_setting);

        btn_change_pass = findViewById(R.id.btn_change_pass);
        btn_contact = findViewById(R.id.btn_contact);
        btn_report_bug = findViewById(R.id.btn_report_bug);
        btn_logout = findViewById(R.id.btn_logout);

        //change info
        //change pass
        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        //contact
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:email@gmail.com"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(SettingActivity.this, "No email client installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //report bug
        btn_report_bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ReportBugActivity.class);
                startActivity(intent);
            }
        });

        //logout
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JWTTokenManager.clearToken(getApplicationContext());
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
