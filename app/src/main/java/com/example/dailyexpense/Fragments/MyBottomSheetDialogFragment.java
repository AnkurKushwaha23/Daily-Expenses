package com.example.dailyexpense.Fragments;
import static com.example.dailyexpense.MainActivity.expenseDatabase;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.dailyexpense.R;

import java.util.Calendar;

import com.example.dailyexpense.ContactUsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.total_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText txtStartDate = view.findViewById(R.id.txtStartDate);
        ImageView imgStartDate = view.findViewById(R.id.imgStartDate);
        EditText txtEndDate = view.findViewById(R.id.txtEndDate);
        ImageView imgEndDate = view.findViewById(R.id.imgEndDate);
        Button btnDelete = view.findViewById(R.id.deleteBetween);
        ImageView imgDismiss = view.findViewById(R.id.dialogDismiss);
        Button totalButton = view.findViewById(R.id.totalBetween);

        imgStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        androidx.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                txtStartDate.setText(String.valueOf(dayOfMonth)+"-"+String.valueOf(month+1)+"-"+String.valueOf(year));
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        1
                );
                dialog.show();
            }
        });
        imgEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        androidx.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                txtEndDate.setText(String.valueOf(dayOfMonth)+"-"+String.valueOf(month+1)+"-"+String.valueOf(year));
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            }
        });
        totalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = txtStartDate.getText().toString();
                String endDate = txtEndDate.getText().toString();

                if (startDate.isEmpty() || endDate.isEmpty()) {
                    // At least one of the fields is empty
                    Toast.makeText(getContext(), "Please fill in both start date and end date", Toast.LENGTH_SHORT).show();
                }else {
                    double total = expenseDatabase.expenseDao().getTotalPriceBetweenDates(startDate, endDate);
                    Intent intent = new Intent(getActivity(), ContactUsActivity.class);

                    // Put the total value into the Intent's Bundle
                    intent.putExtra("START_DATE",startDate);
                    intent.putExtra("END_DATE",endDate);
                    intent.putExtra("TOTAL_VALUE_KEY", total);

                    // Start the new activity
                    startActivity(intent);
                    dismiss();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = txtStartDate.getText().toString();
                String endDate = txtEndDate.getText().toString();

                if (startDate.isEmpty() || endDate.isEmpty()) {
                    // At least one of the fields is empty
                    Toast.makeText(getContext(), "Please fill in both start date and end date", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure you want to Delete Expenses? Between "+startDate +" to "+endDate)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    expenseDatabase.expenseDao().deleteBetweenDate(startDate, endDate);
                                    dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        imgDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}

