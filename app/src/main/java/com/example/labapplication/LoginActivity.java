package com.example.labapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView mLoginTextView;
    private TextView mPasswordTextView;
    private TextView mWarningTextView;

    private EditText mLoginEditText;
    private EditText mPasswordEditText;

    private Button mEnterLoginButton;
    private Button mCreateLoginButton;

    public static final String PREFERENCES_FILE = "settings";
    public static final String PREFERENCES_LOGIN = "login";
    public static final String PREFERENCES_PASSWORD = "password";

    DataBase databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseHelper = new DataBase(getApplicationContext());

        mSettings = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        mLoginTextView = findViewById(R.id.login_textView);
        mPasswordTextView = findViewById(R.id.password_textView);
        mWarningTextView = findViewById(R.id.warning_textView);

        mLoginEditText = findViewById(R.id.login_edit);
        mPasswordEditText = findViewById(R.id.password_edit);
        mLoginEditText.setText(mSettings.getString(PREFERENCES_LOGIN, ""));
        mPasswordEditText.setText(mSettings.getString(PREFERENCES_PASSWORD, ""));

        mEnterLoginButton = findViewById(R.id.enter_login_button);
        mEnterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strLogin = mLoginEditText.getText().toString();
                String strPassword = mPasswordEditText.getText().toString();

                db = databaseHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM " + DataBase.TABLE_1 + " WHERE " + DataBase.T1_COLUMN_LOGIN + " =?", new String[]{strLogin});
                if(cursor.moveToNext()) {
                    String strCheck = cursor.getString(2);
                    if (strPassword.equals(strCheck)) {
                        mWarningTextView.setText("Ok");
                        mWarningTextView.setTextColor(Color.parseColor("#0000FF"));
                        db.close();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("login", strLogin);
                        startActivity(intent);
                    } else {
                        mWarningTextView.setText("Неверный логин или пароль!");
                        mWarningTextView.setTextColor(Color.parseColor("#FF0000"));
                        db.close();
                    }
                }
                else {
                    mWarningTextView.setText("Пользователя не существует!");
                    mWarningTextView.setTextColor(Color.parseColor("#FF0000"));
                    db.close();
                }
            }
        });

        mCreateLoginButton = findViewById(R.id.create_login_button);
        mCreateLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strLogin = mLoginEditText.getText().toString();
                String strPassword = mPasswordEditText.getText().toString();

                db = databaseHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM " + DataBase.TABLE_1 + " WHERE " + DataBase.T1_COLUMN_LOGIN + " =?", new String[]{strLogin});

                if(cursor.moveToNext()){
                    mWarningTextView.setText("Логин занят");
                    db.close();
                } else if(strLogin.length() > 0){
                    db.close();
                    if (strPassword.length() >= 8){
                        Thread createThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                CreateUser(strLogin, strPassword);
                            }
                        });
                        createThread.start();
                        try {
                            createThread.join();
                            mWarningTextView.setText("Успешная регистрация!");
                            mWarningTextView.setTextColor(Color.parseColor("#0000FF"));
                        } catch (InterruptedException ex){
                            ex.printStackTrace();
                        }
                    }
                    else {
                        mWarningTextView.setText("Слабый пароль!");
                        mWarningTextView.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
                else {
                    db.close();
                    mWarningTextView.setText("Некорректный пароль!");
                    mWarningTextView.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String strLogin = mLoginEditText.getText().toString();
        String strPassword = mPasswordEditText.getText().toString();

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(PREFERENCES_LOGIN, strLogin);
        editor.putString(PREFERENCES_PASSWORD, strPassword);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    public void CreateUser(String login, String password){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.T1_COLUMN_LOGIN, login);
        contentValues.put(DataBase.T1_COLUMN_PASSWORD, password);

        sqLiteDatabase.insert(DataBase.TABLE_1, null, contentValues);
        sqLiteDatabase.close();
    }
}