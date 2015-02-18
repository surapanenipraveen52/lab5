package com.example.praveen.qa;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


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
                String json=null;
                System.out.println(getAnswer(postData.toString(), null));
               /* try  {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost request = new HttpPost(baseURL+ "/v1/question/healthcare");
                    StringEntity params = new StringEntity(postData.toString());
                    request.addHeader("content-type", "application/json");
                    request.addHeader("Accept", "application/json");
                    request.addHeader("X-SyncTimeout", "30");
                    request.addHeader("Authorization", encodes);
                    request.setEntity(params);
                    System.out.println("hi");
                    HttpResponse result = httpClient.execute(request);
                    System.out.println("hi2");
                    System.out.println(result.getEntity());
                    json = EntityUtils.toString(result.getEntity(), "UTF-8");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }*/
            }});
    }
    private String getAnswer (String question, String dataSet) {
       getAnswerASYNC task = new getAnswerASYNC();
        String[] data=new String[2];
        data[0]=question;
        data[1]=dataSet;
       task.execute(data);
       return "pk";
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
    private class getAnswerASYNC extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... question) {
            String encodes="Basic MmFmMDhkMzUtOWMzNy00YmRkLWIwYzItZmM4NGRhOGJkMTY3OlY5Q2dGWVpYdFo4QQ==";
            String baseURL = "https://gateway.watsonplatform.net/question-and-answer-beta/api";
            String json="No Answer";
            try  {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(baseURL+ "/v1/question/healthcare");
                StringEntity params = new StringEntity(question[0]);
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept", "application/json");
                request.addHeader("X-SyncTimeout", "30");
                request.addHeader("Authorization", encodes);
                request.setEntity(params);
                System.out.println("hi");
                HttpResponse result = httpClient.execute(request);
                System.out.println("hi2");
                json = EntityUtils.toString(result.getEntity(), "UTF-8");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
        @Override
        protected void onPostExecute(String answer) {
            try {

                JSONArray res = (JSONArray) new JSONParser().parse(answer);
                JSONObject pipelineOne = (JSONObject) res.get(0);
                JSONObject question = (JSONObject) pipelineOne.get("question");
                JSONArray answers = (JSONArray) question.get("evidencelist");
                JSONObject answerOne = (JSONObject) answers.get(0);
                System.out.println(answerOne.get("text").toString());
                TextView answerView=(TextView) findViewById(R.id.answer);
                answerView.setText(answerOne.get("text").toString());
            }catch (Exception e){

            }
        }
        }
    }
