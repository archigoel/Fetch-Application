package com.example.fetchapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fetchapplication.HttpHandler;
import com.example.fetchapplication.model.Item;
import com.example.fetchapplication.ItemDecoration;
import com.example.fetchapplication.R;
import com.example.fetchapplication.adapters.FetchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FetchAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Item> itemList;
    private static String web_url = "https://fetch-hiring.s3.amazonaws.com/hiring.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ItemInfo().execute();
    }
    private class ItemInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            itemList = new ArrayList<>();
            HttpHandler handler = new HttpHandler();
            String jsonStr = handler.makeServiceCall(web_url);

            if (jsonStr != null) {
                try {
                    JSONArray items = new JSONArray(jsonStr);
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject temp = items.getJSONObject(i);
                        Item item = new Item();
                        if(temp.getString("name").equals("") || temp.getString("name").equals("null"))
                            continue;

                        item.setName(temp.getString("name"));
                        item.setListId(temp.getInt("listId"));

                        itemList.add(item);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }

                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            recyclerView = findViewById(R.id.simple_recyclerview);
            layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            Collections.sort(itemList, new ListIdComparator());//sort the list by listid
            Collections.sort(itemList, new NameComparator());// sort the list by item
            mAdapter = new FetchAdapter(itemList);
            recyclerView.addItemDecoration(new ItemDecoration(MainActivity.this));
            recyclerView.setAdapter(mAdapter);
        }
    }

    class ListIdComparator implements Comparator<Item> {
        public int compare(Item i1, Item i2) {
            return i1.getListId() - i2.getListId();
        }
    }

    class NameComparator implements Comparator<Item> {
        public int compare(Item i1, Item i2) {
            if(i1.getListId() == i2.getListId())
             return extractInt(i1.getName()) - extractInt(i2.getName());
            return 0;
        }
        int extractInt(String s) {
            String num = s.replaceAll("\\D", "");
            // return 0 if no digits found
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }
    }
}


