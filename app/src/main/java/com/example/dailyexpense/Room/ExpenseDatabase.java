package com.example.dailyexpense.Room;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Expense.class}, version = 1)
public abstract class ExpenseDatabase extends RoomDatabase {
    private static final String DB_NAME="expensedb";
    private static ExpenseDatabase instance;
    public static synchronized ExpenseDatabase getDb(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context, ExpenseDatabase.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract ExpenseDao expenseDao();
}

