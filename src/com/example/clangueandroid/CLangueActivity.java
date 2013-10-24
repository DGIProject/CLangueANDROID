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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CLangueActivity extends Activity {
	private String username;
	
	private EditText usernameEdit;
	private EditText passwordEdit;
	private Button loginButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        
        Intent intent = getIntent();
        
        if(intent != null)
        {
        	username = intent.getStringExtra("username");
        	
        	System.out.println("username : " + username);
        }
        
        usernameEdit = (EditText)findViewById(R.id.usernameEdit);
		passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        
        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginButtonListener);
    }
    
    private OnClickListener loginButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
		            {
		                try
		                {
		                	HttpClient httpclient = new DefaultHttpClient();
		                	HttpPost httppost = new HttpPost("http://clangue.net/api/ANDROID/login.php");
		                	
		                	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		                	
		                	nameValuePairs.add(new BasicNameValuePair("username", usernameEdit.getText().toString()));
		                	nameValuePairs.add(new BasicNameValuePair("password", passwordEdit.getText().toString()));
		                	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
		     
		                    HttpResponse response = httpclient.execute(httppost);
		                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		                    
		                    int result = Integer.parseInt(reader.readLine());
		                    
		                    switch(result)
		                    {
		                    	case 1:
		                    		System.out.println("Incorrect username or password");
		                    		break;
		                    	case 2:
		                    		username = usernameEdit.getText().toString();
		                    		
		                    		Intent intent = new Intent(CLangueActivity.this, HomeworkActivity.class);
		                    		intent.putExtra("username", username);
		                            startActivity(intent);
		                    		break;
		                    	default:
		                    		System.out.println("Fill all fields");
		                    }
		                    
		                    System.out.println(result + "ok");
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
					
					System.out.println(usernameEdit.getText().toString());
					System.out.println(passwordEdit.getText().toString());
					System.out.println("Test");
				}
				
			}).start();
		}
    	
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clangue, menu);
        return true;
    }
    
}
