package com.androidhive.jsonparsing;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AndroidJSONParsingActivity extends ListActivity {

	// url에 정의 JSON 데이터형태를 불러온다. contacts[]배열안에 {이름:값}형태의 객체가  있다.  
	private static String url = "http://api.androidhive.info/contacts/";
	
	// JSON Node names
	private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_GENDER = "gender";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_PHONE_MOBILE = "mobile";
	private static final String TAG_PHONE_HOME = "home";
	private static final String TAG_PHONE_OFFICE = "office";

	// contacts JSONArray클래스 초기화 
	JSONArray contacts = null;

	@Override
	//onCreate메서드에서 main.xml레이아웃을 읽어 androidJSONParsingActivity액티비티에 채운다. 
	public void onCreate(Bundle savedInstanceState) {
		//onCreate>프래그먼트 생성될때 자동호출된 함/savedInstanceState>재생성될때 이전상태값을 갖는 인수

		//		activity의 onCreate에서 setContentView 메서드 리턴 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		res>layout>main.xml파일(레이아웃 짜여진)을 activity에 부착하면 핸들러설치가능해지고,findViewById로 검색가능   
		
		// HashMap자료형인 contacts리스트 만듦 for ListView
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL:JSONbject클래스는 JSON파일에서 객체를 읽어들인다. 
		JSONObject json = jParser.getJSONFromUrl(url);

		try {
			// Getting Array of Contacts
			//contacts JSONArray클래스는 URI의 JSON파일에서 배열을 읽어들이고, 
			//생성자로 JSON 문자열(인자 )전달되어 파싱하여 내부 메모리에 배열형태로 저장된다. 

			contacts = json.getJSONArray(TAG_CONTACTS);
			
			// looping through All Contacts
			for(int i = 0; i < contacts.length(); i++){
				JSONObject c = contacts.getJSONObject(i);
				
				// Storing each json item in variable, 내부 메모리에 저장된 값을 읽기 위해서 getString 메서드로 호출한다.
				//배열타입이 String 이니깐, 
				String id = c.getString(TAG_ID);
				String name = c.getString(TAG_NAME);
				String email = c.getString(TAG_EMAIL);
				String address = c.getString(TAG_ADDRESS);
				String gender = c.getString(TAG_GENDER);
				
				// Phone number is agin JSON Object
				JSONObject phone = c.getJSONObject(TAG_PHONE);
				String mobile = phone.getString(TAG_PHONE_MOBILE);
				String home = phone.getString(TAG_PHONE_HOME);
				String office = phone.getString(TAG_PHONE_OFFICE);
				
				// creating new 
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				// adding each child node to HashMap key => value
				map.put(TAG_ID, id);
				map.put(TAG_NAME, name);
				map.put(TAG_EMAIL, email);
				map.put(TAG_PHONE_MOBILE, mobile);

				// adding HashList to ArrayList
				contactList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//결론적으로 JSON파일의 저장되는 데이터가 배열과 객체라서, JSONArray, JSONObject 두가지 클래스와 메서드로 정보를 추출한다.
		/**
		 * Updating parsed JSON data into ListView
		 * */
		ListAdapter adapter = new SimpleAdapter(this, contactList,
				R.layout.list_item,
				new String[] { TAG_NAME, TAG_EMAIL, TAG_PHONE_MOBILE }, new int[] {
						R.id.name, R.id.email, R.id.mobile });

		setListAdapter(adapter);

		// selecting single ListView item
		ListView lv = getListView();

		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
				String cost = ((TextView) view.findViewById(R.id.email)).getText().toString();
				String description = ((TextView) view.findViewById(R.id.mobile)).getText().toString();
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
				in.putExtra(TAG_NAME, name);
				in.putExtra(TAG_EMAIL, cost);
				in.putExtra(TAG_PHONE_MOBILE, description);
				startActivity(in);

			}
		});
		
		

	}

}