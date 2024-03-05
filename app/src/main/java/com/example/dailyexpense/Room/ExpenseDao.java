package com.example.dailyexpense.Room;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);

    @Query("SELECT * FROM expense_table")
    List<Expense> getAllExpenses();

    @Insert
    void addExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);
    @Query("DELETE FROM expense_table WHERE id = :id")
    abstract void deleteById(int id);

    @Query("DELETE FROM expense_table")
    abstract void deleteAll();

    @Query("SELECT SUM(price) FROM expense_table ")
    double getTotal();

    @Query("SELECT SUM(price) FROM expense_table WHERE date BETWEEN :startDate AND :endDate")
    int getTotalPriceBetweenDates(String startDate,String endDate);

    @Query("DELETE FROM expense_table WHERE date BETWEEN :startDate AND :endDate")
    void deleteBetweenDate(String startDate,String endDate);

    @Query("SELECT DISTINCT category FROM expense_table WHERE date BETWEEN :startDate AND :endDate ORDER BY price ASC")
    List<String> getCategoriesByPriceBetweenDates(String startDate, String endDate);

    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate")
    List<Expense> getExpensesBetweenDates(String startDate, String endDate);
}
