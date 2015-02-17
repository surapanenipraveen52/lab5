package com.example.praveen.qa;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
//import org.apache.http.entity.ContentType;
import org.apache.http.entity.ContentType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.net.URI;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String baseURL = "https://gateway.watsonplatform.net/question-and-answer-beta/api";
        final String username = "2af08d35-9c37-4bdd-b0c2-fc84da8bd167";
        final String password = "V9CgFYZXtZ8A";
        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[]{"Health Care", "Medical"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        final Button ask= (Button)findViewById(R.id.ask);
        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et=(EditText)findViewById(R.id.question);
                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                String dataSet = spinner.getSelectedItem().toString();
                String question= et.getText().toString();
                JSONObject questionJson = new JSONObject();
                questionJson.put("questionText",question);
                JSONObject evidenceRequest = new JSONObject();
                evidenceRequest.put("items",5);
                questionJson.put("evidenceRequest",evidenceRequest);
                JSONObject postData = new JSONObject();
                postData.put("question",questionJson);
                TextView answer=(TextView) findViewById(R.id.answer);
                answer.setText(postData.toString());
                try {
                    Executor executor = Executor.newInstance().auth(username, password);
                    URI serviceURI = new URI(baseURL+ "/v1/question/"+"healthcare").normalize();
                    String t= executor.execute(Request.Post(serviceURI)
                                    .addHeader("Accept", "application/json")
                                    .addHeader("X-SyncTimeout", "30")
                                    .bodyString(postData.toString(), ContentType.APPLICATION_JSON)
                    ).returnContent().asString();

            }catch (Exception e) {
                    answer.setText(e.getMessage());
                }


            }});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
