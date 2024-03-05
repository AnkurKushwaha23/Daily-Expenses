package com.example.dailyexpense;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyexpense.Adapter.ExpenseAdapter;
import com.example.dailyexpense.Room.Expense;
import com.example.dailyexpense.Room.ExpenseDatabase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final  int REQUEST_CODE = 1;
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    public static ExpenseDatabase expenseDatabase;
    AutoCompleteTextView autoCompleteTextView;
    EditText txtPrice,txtQuantity;
    Spinner spinnerCategories,spinnerQuantity;
    Button btnAdd,btnHistory;
    TextView total;
    final private String[] domesticItems = {
            "Grocery","Vegetables","Fruits","LPG Gas","Cleaning Supplies","Atta","Milk","Eggs","Cooking Oil","Spices","Patrol","Detergent",
            "Dishwash Soap","Clothes","Toothpaste","Shampoo","Soap","Chawal","Daal","Hair Oil","Medicine","Cosmetics","Skin Cream","Room Rent","Light Bill",
            "Travel Fare","Mobile Recharge","FastFoods","Book","Note Book","Stationary","School Fees","College Fees","Tution Fees"};

    private ArrayAdapter<String> autoCompleteAdapter;
    @SuppressLint({"MissingInflatedId", "SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolMain);
        setSupportActionBar(toolbar);
        //Room
        expenseDatabase = ExpenseDatabase.getDb(MainActivity.this);
        //UI elements
        spinnerCategories = findViewById(R.id.spinnerCategories);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtPrice = findViewById(R.id.txtPrice);
        spinnerQuantity = findViewById(R.id.spinnerQuantity);
        btnAdd = findViewById(R.id.btnAdd);
        btnHistory = findViewById(R.id.btnHistory);
        //total
        total = findViewById(R.id.total);

        updateTotal();
        refreshPieChart();

        String[] categories = {"Grocery","Personal Care","Education","Home Expenses","Medical Expenses","Transportation Expenses","Mobile Communications","Miscellaneous"};
        ArrayAdapter<String> spinCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,categories);
        spinnerCategories.setAdapter(spinCategories);

        //autocompleteText
        autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, domesticItems);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories[position];

                // Check if the selected category is "Grocery"
                if ("Grocery".equals(selectedCategory)) {
                    String[] groceryItems = {
                            "Vegetables", "Fruits", "LPG Gas", "Cleaning Supplies", "Atta (Flour)","Chawal (Rice)","Daal (Lentis)", "Milk", "Eggs", "Cooking Oil", "Spices", "Detergent",
                            "Dishwash Soap","Salt"};
                    autoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,groceryItems);
                } else if ("Personal Care".equals(selectedCategory)) {
                    String[] personalCareItems = {"Clothes", "Toothpaste", "Shampoo", "Soap", "Hair Oil", "Cosmetics", "Skin Cream","Hair Cut","Beard Shave"};
                    autoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,personalCareItems);
                } else if ("Education".equals(selectedCategory)) {
                    String[] educationalExpenses = {"Book", "Note Book", "Stationary", "School Fees", "College Fees", "Tuition Fees"};
                    autoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,educationalExpenses);
                } else if ("Home Expenses".equals(selectedCategory)) {
                    String[] homeExpenses = {"Room Rent", "Light Bill"};
                    autoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,homeExpenses);
                } else if ("Medical Expenses".equals(selectedCategory)) {
                    String[] medical = {"Medicines","Syrup","Hospital Bill"};
                    autoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,medical);
                } else if ("Transportation Expenses".equals(selectedCategory)) {
                    String[] transportation = {"Travel Fare", "Patrol","Bike Servicing"};
                    autoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,transportation);
                } else if ("Mobile Communications".equals(selectedCategory)) {
                    String[] mobile = {"Mobile Recharge","Mobile Care"};
                    autoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,mobile);
                }else if ("Miscellaneous".equals(selectedCategory)) {
                    String[] etc = {"Fast Food"};
                    autoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,etc);
                }
                autoCompleteTextView.setAdapter(autoCompleteAdapter);
                autoCompleteTextView.setThreshold(1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] quantity = {"Piece","Kg","Day","Month","Semester"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,quantity);
        spinnerQuantity.setAdapter(spinnerAdapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from views
                String product = autoCompleteTextView.getText().toString().trim();
                String quantityText = txtQuantity.getText().toString().trim();
                String priceText = txtPrice.getText().toString().trim();

                // Check if EditText fields are not empty
                if (!product.isEmpty() && !quantityText.isEmpty() && !priceText.isEmpty()) {
                    // Parse values if fields are not empty
                    String currentDate = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
                    String categories = spinnerCategories.getSelectedItem().toString();
                    double quantity = Double.parseDouble(quantityText);
                    String spinnerValue = spinnerQuantity.getSelectedItem().toString();
                    double price = Double.parseDouble(priceText);

                    // Create Expense object
                    Expense expense = new Expense(currentDate,categories,product, quantity, spinnerValue, price);

                    // Insert into Room database
                    expenseDatabase.expenseDao().insert(expense);
                    // Show toast
                    showToastStatic(getApplicationContext(),"Expense Saved.");
                    // Clear input fields
                    getClear();
                    // Update total
                    updateTotal();
                    // Refresh pie chart
                    refreshPieChart();
                } else {
                    // Show a message or handle the case where one or more fields are empty
                    showToastStatic(getApplicationContext(),"Please fill in all fields.");
                }
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        // Add an OnBackPressedCallback to handle back button presses
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to Exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        // Register the callback
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

    }
    public void getClear(){
        spinnerCategories.setSelection(0);
        autoCompleteTextView.getText().clear();
        txtQuantity.getText().clear();
        spinnerQuantity.setSelection(0);
        txtPrice.getText().clear();
    }
    public static void showToastStatic(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView text = layout.findViewById(R.id.textToast);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
    @SuppressLint("DefaultLocale")
    private void updateTotal() {
        double totalExpenses = expenseDatabase.expenseDao().getTotal();
        total.setText(String.format("Your Total Expenses: ₹%d", (int) totalExpenses));
    }

    private void refreshPieChart() {
        PieChart pieChart = findViewById(R.id.pieChart);

        // Get your expenses data between two dates
        List<Expense> expenses = expenseDatabase.expenseDao().getAllExpenses();

        if (expenses.isEmpty()) {
            // Handle the case where there are no expenses
            pieChart.clear();
            pieChart.setNoDataText("No expenses available");
            return;
        }
        // Aggregate expenses by category
        Map<String, Double> categoryMap = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double totalAmount = categoryMap.getOrDefault(category, 0.0);
            totalAmount += expense.getPrice();
            categoryMap.put(category, totalAmount);
        }

        // Create entries for the PieChart
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        // Set up the PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        // Set up the PieData
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        // Additional customization
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Expense Distribution");
        pieChart.setUsePercentValues(true); // Show values as percentages
        pieChart.animateY(1000);

        // Refresh the chart
        pieChart.invalidate();
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE && resultCode == RESULT_OK){
            updateTotal();
            refreshPieChart();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem menuItem = menu.findItem(R.id.contactInfo);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.contactInfo){
            startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //Resource Management
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        if (expenseDatabase != null && expenseDatabase.isOpen()) {
            expenseDatabase.close();
        }
    }
}