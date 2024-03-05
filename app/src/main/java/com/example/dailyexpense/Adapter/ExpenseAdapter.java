package com.example.dailyexpense.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.dailyexpense.R;
import com.example.dailyexpense.Room.Expense;
import com.example.dailyexpense.Room.ExpenseDao;
import com.example.dailyexpense.Room.ExpenseDatabase;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private final LinearLayout emptyLayout;
    private final Context context;
    private final List<Expense> expenseList;
    ExpenseDatabase db;
    public ExpenseAdapter(Context context, List<Expense> expenseList, LinearLayout emptyLayout) {
        this.context = context;
        this.expenseList = expenseList;
        this.db = ExpenseDatabase.getDb(context);
        this.emptyLayout = emptyLayout;
        updateEmptyLayoutVisibility();
    }

    private void updateEmptyLayoutVisibility() {
        emptyLayout.setVisibility(expenseList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Expense expense = expenseList.get(position);
        // Set data to views in the ViewHolder
        holder.showDate.setText(expense.getDate());
        holder.showProduct.setText(expense.getProduct());
        holder.showQuantity.setText(String.valueOf(expense.getQuantity()));
        holder.showSpinnerValue.setText(expense.getSpinnerValue());
        holder.showPrice.setText(String.valueOf(expense.getPrice()));
        holder.recyclerRow.setOnLongClickListener(new View.OnLongClickListener() {
            final int position = holder.getAdapterPosition();
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteItem(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView showDate, showProduct, showQuantity, showSpinnerValue, showPrice;
        LinearLayout recyclerRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerRow = itemView.findViewById(R.id.recyclerRow);
            showDate = itemView.findViewById(R.id.showDate);
            showProduct = itemView.findViewById(R.id.showProduct);
            showQuantity = itemView.findViewById(R.id.showQuantity);
            showSpinnerValue = itemView.findViewById(R.id.showSpinnerValue);
            showPrice = itemView.findViewById(R.id.showPrice);
        }
    }
    public boolean isEmpty() {
        return expenseList.isEmpty();
    }

    public void deleteItem(int position) {
        Expense item =expenseList.get(position);
        ExpenseDao dao = db.expenseDao();
        dao.deleteById(item.getId());
        expenseList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, expenseList.size());
        updateEmptyLayoutVisibility();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void deleteAllItems() {
        ExpenseDao dao = db.expenseDao();
        dao.deleteAll();
        expenseList.clear();
        notifyDataSetChanged();
        updateEmptyLayoutVisibility();
    }
}
