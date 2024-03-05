package com.example.dailyexpense;

import static com.example.dailyexpense.MainActivity.expenseDatabase;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyexpense.Adapter.ExpenseAdapter;
import com.example.dailyexpense.Fragments.MyBottomSheetDialogFragment;
import com.example.dailyexpense.Room.Expense;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    ArrayList<Expense> arrayList;
    Button btn_delete;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btn_delete = findViewById(R.id.btnDelete);
        Toolbar toolbar = findViewById(R.id.toolMain);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Check if arrayList is empty and set the visibility of the LinearLayout
        LinearLayout linearLayout = findViewById(R.id.emptyLay);
        arrayList = (ArrayList<Expense>) expenseDatabase.expenseDao().getAllExpenses();
        // Create and set up the adapter
        ExpenseAdapter adapter = new ExpenseAdapter(DetailActivity.this, arrayList,linearLayout);
        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        recyclerView.setAdapter(adapter);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adapter.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    builder.setMessage("Are you sure you want to Delete All Saved Expenses.")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.deleteAllItems();
                                    MainActivity.showToastStatic(getApplicationContext(), "All expenses have been deleted.");
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    MainActivity.showToastStatic(getApplicationContext(), "No items to delete.");
                }
            }
        });

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setResult(RESULT_OK);
                finish();
            }
        };
        // Register the callback
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            setResult(RESULT_OK);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}