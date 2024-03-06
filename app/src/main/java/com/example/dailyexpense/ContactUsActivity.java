package com.example.dailyexpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyexpense.Room.Expense;
import com.example.dailyexpense.Room.ExpenseDao;
import com.example.dailyexpense.Room.ExpenseDatabase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {
    Button btnCall,btnMsg,btnMail;
    ImageView imgLink,imgX,imgGithub;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        imgLink = findViewById(R.id.imgLink);
        imgX = findViewById(R.id.imgX);
        imgGithub = findViewById(R.id.imgGithub);
        btnCall = findViewById(R.id.btnCall);
        btnMsg = findViewById(R.id.btnMsg);
        btnMail = findViewById(R.id.btnMail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent idial = new Intent(Intent.ACTION_DIAL);
                idial.setData(Uri.parse("tel: +917048216866"));
                startActivity(idial);
                MainActivity.showToastStatic(ContactUsActivity.this,"Thanks for Contacting us");
            }
        });
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imsg = new Intent(Intent.ACTION_SENDTO);
                imsg.setData(Uri.parse("smsto:"+Uri.encode("+91000000000")));
                imsg.putExtra("sms_body","This App is Awesome!");
                startActivity(imsg);
                MainActivity.showToastStatic(ContactUsActivity.this,"Thanks for Contacting us");
            }
        });
        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imail = new Intent(Intent.ACTION_SEND);
                imail.setType("message/rfc822");
                imail.putExtra(Intent.EXTRA_EMAIL,new String[]{"idontknow@gmail.com"});
                imail.putExtra(Intent.EXTRA_SUBJECT,"Queries");
                imail.putExtra(Intent.EXTRA_TEXT,"This App is Awesome!");
                startActivity(Intent.createChooser(imail,"Email via"));
                MainActivity.showToastStatic(ContactUsActivity.this,"Thanks for Contacting us");
            }
        });

        imgLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLink = "https://www.linkedin.com/in/ankur-kushwaha";
                String packageName ="com.linkedin.android";
                String webLink ="https://www.linkedin.com/in/ankur-kushwaha";
                openLink(appLink,packageName,webLink);
            }
        });
        imgX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLink = "https://x.com/AnkurKushwaha23";
                String packageName ="com.twitter.android.";
                String webLink ="https://x.com/AnkurKushwaha23";
                openLink(appLink,packageName,webLink);
            }
        });
        imgGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLink = "https://github.com/AnkurKushwaha23";
                String packageName ="com.github.mobile.";
                String webLink ="https://github.com/AnkurKushwaha23";
                openLink(appLink,packageName,webLink);
            }
        });
    }
    private void openLink(String applink, String packageName, String webLink){
        try {
            Uri uri = Uri.parse(applink);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException activityNotFoundException){
            Uri uri = Uri.parse(webLink);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
