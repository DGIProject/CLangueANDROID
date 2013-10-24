package com.example.clangueandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HomeworkActivity extends Activity {
	private String username;
	private int tabHomeworkId[];
	
	private TextView usernameText;
	private ListView homeworkList;
	private Button logoutButton;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homework_activity);
        
        Intent intent = getIntent();
        
        if(intent != null)
        {
        	username = intent.getStringExtra("username");
        }
        
        System.out.println(username);
        
        usernameText = (TextView)findViewById(R.id.usernameText);
        homeworkList = (ListView)findViewById(R.id.homeworkList);
        logoutButton = (Button)findViewById(R.id.logoutButton);
        
        usernameText.setText(username);
        
        //String items[] = {"France", "Angleterre", "Allemagne", "Espagne"};
        
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items);
        
        //homeworkList.setAdapter(adapter);
        
        homeworkList.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,
        		int position, long id) {

        		//On récupère le titre de l'Item dans un String
        		String item = (String) homeworkList.getAdapter().getItem(position);
        		
        		System.out.println("position : " + position);
        		
        		AlertDialog.Builder adb = new AlertDialog.Builder(HomeworkActivity.this);
        		//Le titre à notre boite de dialogue
        		adb.setTitle("Votre Item");
        		//Le message que l'on veut afficher 
        		adb.setMessage("Vous avez séléctionné : "+item);
        		//On ajoute le bouton OK
        		adb.setPositiveButton("Ok", null);
        		//on affiche la boite de dialogue
        		adb.show();
        	}
        });
        
        logoutButton.setOnClickListener(logoutButtonListener);
        
        getListHomework();
    }
	
	private void getListHomework() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
	            {
	                try
	                {
	                	HttpClient httpclient = new DefaultHttpClient();
	                	HttpPost httppost = new HttpPost("http://clangue.net/api/ANDROID/getListHomework.php");
	                	
	                	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	                	
	                	nameValuePairs.add(new BasicNameValuePair("username", username));
	                	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
	     
	                    HttpResponse response = httpclient.execute(httppost);
	                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	                    
	                    String result = reader.readLine();
	                    
	                    JSONObject json = new JSONObject(result);
	                    JSONArray tabJSON = json.getJSONArray("list");
	                    
	                    String items[] = null;
	                    
	                    for(int i = 0; i < tabJSON.length(); i++)
	                    {
	                    	JSONObject dataTabJSON = tabJSON.getJSONObject(i);
	                    	
	                    	System.out.println("id : " + dataTabJSON.getInt("id"));
	                    	tabHomeworkId[i] = dataTabJSON.getInt("id");
	                    	
	                    	items[i] = dataTabJSON.getString("name");
	                    }
	                    
	                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items);
	                    
	                    //homeworkList.setAdapter(adapter);
	                } catch (ClientProtocolException e) {
	                // TODO
	                	System.out.println("error1");
	                } catch (IOException e) {
	                // TODO
	                	System.out.println("error2");
	                }
	                
	                System.out.println("good");
	            }
	            catch (Exception e)
	            {
	                e.printStackTrace();
	                
	                System.out.println("error3");
	            }
			}
			
		}).start();
	}
	
	private OnClickListener logoutButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("logout");
			
			Intent intent = new Intent(HomeworkActivity.this, CLangueActivity.class);
    		intent.putExtra("username", username);
            startActivity(intent);
		}
		
	};
}
