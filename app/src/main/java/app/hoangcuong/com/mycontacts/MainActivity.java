package app.hoangcuong.com.mycontacts;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Contacts> listContacts;
    private RecyclerView recyclerView;
    private DBAdapter db;
    private RecycleViewAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBAdapter(getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onClickListener);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        listContacts = new ArrayList<Contacts>();
        GetContacts();
        setupRecycleView();
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.fab){
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, addContacts.class);
                startActivity(intent);
            }
        }
    };
    private void setupRecycleView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        setContactsAdapter(listContacts);
    }
    private void setContactsAdapter(ArrayList<Contacts> contactses){
        adapter = new RecycleViewAdapter(this,contactses);
        recyclerView.setAdapter(adapter);


    }
    public void GetContacts() {
        //--get all contacts---
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst())
        {
            do {
                listContacts.add(new Contacts(c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getBlob(6)));
            } while (c.moveToNext());
        }
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    adapter.filter("");
                }else {
                    adapter.filter(newText);
                }
                return true;
            }
        });
        return true;
    }
}
