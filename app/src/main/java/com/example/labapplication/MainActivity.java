package com.example.labapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_STATE = "index";

    private TextView mTextView;

    private Button mAddButton;
    private Button mRemoveButton;
    private Button mSecondButton;
    private Button mLocaleButton;

    private ListView mListView;

    DataBase databaseHelper;
    SQLiteDatabase db;

    ItemAdapter adapter;
    static List<Item> List = new ArrayList<>();
//region name
    static int state = 0;
    int listSelectPosition;
//endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate(Bundle) called");

        if(savedInstanceState != null){
            state = savedInstanceState.getInt(KEY_STATE, 0);
        }

        databaseHelper = new DataBase(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        final String login = bundle.get("login").toString();

        mTextView = (TextView) findViewById(R.id.text_name);

        mListView = (ListView) findViewById(R.id.list_item);
        final List<Item> itemList = getList();
        adapter = new ItemAdapter(this, itemList);
        mListView.setAdapter(adapter);
        itemList.clear();
        ReadDataFromDataBase(itemList, login);
        adapter.UpdateList(itemList);
        mAddButton = (Button) findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alert1 = new AlertDialog.Builder(MainActivity.this);
                final EditText input1 = new EditText(MainActivity.this);
                alert1.setView(input1);

                final AlertDialog.Builder alert2 = new AlertDialog.Builder(MainActivity.this);
                final EditText input2 = new EditText(MainActivity.this);
                alert2.setView(input2);

                final AlertDialog.Builder alert3 = new AlertDialog.Builder(MainActivity.this);
                final EditText input3 = new EditText(MainActivity.this);
                alert3.setView(input3);

                String[] text = new String[3];

                alert1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Editable value1 = input1.getText();
                        text[0] = value1.toString();
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Editable value2 = input2.getText();
                        text[1] = value2.toString();
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                alert3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Editable value3 = input3.getText();
                        text[2] = value3.toString();

                        ContentValues newValues = new ContentValues();
                        newValues.put(DataBase.T2_COLUMN_USERS_ID, login);
                        newValues.put(DataBase.T2_COLUMN_NAME, text[0]);
                        newValues.put(DataBase.T2_COLUMN_COMPANY, text[1]);
                        newValues.put(DataBase.T2_COLUMN_POST, text[2]);

                        Thread addThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                db.insert(DataBase.TABLE_2, null, newValues);
                                db.close();
                            }
                        });
                        addThread.start();
                        try {
                            addThread.join();
                        }
                        catch (InterruptedException ex){
                            ex.printStackTrace();
                        }

                        itemList.clear();
                        ReadDataFromDataBase(itemList, login);
                        adapter.UpdateList(itemList);
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                if(state == 0){
                    alert1.setTitle("Данные о сотруднике");
                    alert1.setMessage("Имя");
                    alert2.setTitle("Данные о сотруднике");
                    alert2.setMessage("Компания");
                    alert3.setTitle("Данные о сотруднике");
                    alert3.setMessage("Должность");
                }
                else{
                    alert1.setTitle("Employee data");
                    alert1.setMessage("Name");
                    alert2.setTitle("Employee data");
                    alert2.setMessage("Company");
                    alert3.setTitle("Employee data");
                    alert3.setMessage("Post");
                }
                alert3.show();
                alert2.show();
                alert1.show();
            }
        });

        mRemoveButton = (Button) findViewById(R.id.remove_button);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = itemList.get(listSelectPosition);
                int posId = item.getId();

                Thread removeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                        db.delete(DataBase.TABLE_2, DataBase.T2_COLUMN_ID + " =" + posId, null);
                        db.close();
                    }
                });
                removeThread.start();
                try {
                    removeThread.join();
                } catch (InterruptedException ex){
                    ex.printStackTrace();
                }
                itemList.clear();
                ReadDataFromDataBase(itemList, login);
                adapter.UpdateList(itemList);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Clicked " + adapter.getItem(i), Toast.LENGTH_SHORT).show();
                listSelectPosition = i;
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int selectedItem = i;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.ic_launcher_foreground)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                itemList.remove(selectedItem);
                                adapter.UpdateList(itemList);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                if(state == 0){
                    builder.setTitle("Вы уверены?");
                    builder.setMessage("Вы действительно хотите удалить этот пункт?");
                }
                else {
                    builder.setTitle("Are you sure?");
                    builder.setMessage("Do you want to delete this item?");
                }
                        builder.show();
                return true;
            }
        });

        mSecondButton = (Button) findViewById(R.id.second_activity_button);
        mSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        mLocaleButton = (Button) findViewById(R.id.locale_button);
        mLocaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state == 0){
                    mAddButton.setText("Add");
                    mRemoveButton.setText("Remove");
                    mLocaleButton.setText("EN/RU");
                    mSecondButton.setText("Activity");
                    mTextView.setText("Data");
                    state = 1;
                }
                else {
                    mAddButton.setText("Добавить");
                    mRemoveButton.setText("Удалить");
                    mLocaleButton.setText("RU/EN");
                    mSecondButton.setText("Активити");
                    mTextView.setText("Данные");
                    state = 0;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSavedInstanceState");
        savedInstanceState.putInt(KEY_STATE, state);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
        databaseHelper.close();
    }

    private List<Item> getList(){
        return List;
    }

    public int ReadDataFromDataBase(List<Item> list, String login){
        int count = 0;

        db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBase.TABLE_2 + " WHERE " + DataBase.T2_COLUMN_USERS_ID + "=?", new String[]{login});

        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(2);
                String company = cursor.getString(3);
                String post = cursor.getString(4);
                list.add(new Item(name, company, post, id));
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count;
    }
}