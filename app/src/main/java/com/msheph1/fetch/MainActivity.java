package com.msheph1.fetch;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.msheph1.fetch.databinding.ActivityMainBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ArrayList<ArrayList<String>> infoList;
    ActivityMainBinding binding;
    ListAdapter adapter;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeAllLists();
        new fetchData().start();
    }

    private void initializeAllLists() {
        infoList = new ArrayList<>();
        adapter = new ListAdapter(MainActivity.this, infoList);
        binding.infoList.setAdapter(adapter);
    }


    class fetchData extends Thread {
        String data = "";
        @Override
        public void run() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching Data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });
            try {
                //Grab json from the url
                URL url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null) {
                    data += line;
                }
                //add it to info arrayList
                if (!data.isEmpty()) {
                    JSONArray arr = new JSONArray(data);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject info = arr.getJSONObject(i);
                        String name = info.getString("name");
                        //filter out null names
                        if(!name.equals("null") && !name.equals(""))
                        {
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(info.getString("listId"));
                            temp.add(name);
                            temp.add(info.getString("id"));
                            infoList.add(temp);
                        }

                    }
                    //sort the arrayList based on the list id first then sort it based on the name
                    Collections.sort(infoList, Comparator.comparing((ArrayList<String> innerList) -> Integer.parseInt(innerList.get(0))).thenComparing(innerList -> Integer.parseInt(innerList.get(1).substring(5))));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }  catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
