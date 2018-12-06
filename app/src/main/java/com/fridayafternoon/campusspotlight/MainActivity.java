package com.fridayafternoon.campusspotlight;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnListFragmentInteractionListener,
        DashboardFragment.OnListFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    DialogInterface.OnClickListener dialogClickListener;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseStorage mStorage;
    StorageReference storageReference;
    String usersName;
    ArrayList<Event> events = new ArrayList<>();
    Fragment homeFragment = new HomeFragment();
    Fragment dashboardFragment = new DashboardFragment();
    Fragment profileFragment = new ProfileFragment();
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    Fragment selectedFragment;





    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                        new GetXMLAsync().execute();
                    selectedFragment = homeFragment;
                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(MainActivity.this, "Dashboard Clicked", Toast.LENGTH_SHORT).show();
                    selectedFragment = dashboardFragment;
                    return true;
                case R.id.navigation_profile:
                    Toast.makeText(MainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                    selectedFragment = profileFragment;
                    return true;
            }

            fragmentTransaction.replace(R.id.fragment, selectedFragment)
            .addToBackStack(null)
            .commit();

            return false;
        }
    };

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() &&
                (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();
        if (mAuth.getCurrentUser() != null) {
            usersName = mAuth.getCurrentUser().getDisplayName();
        }

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("email", mAuth.getCurrentUser().getEmail());
                        editor.commit();

                        mAuth.signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No was clicked
                        break;
                }
            }
        };

        //set users name
        //usersName.setText(mAuth.getCurrentUser().getDisplayName());

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(mAuth.getCurrentUser() != null) {
            inflater.inflate(R.menu.logout_menu, menu);
        } else {
            inflater.inflate(R.menu.login_menu, menu);
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(Html.fromHtml("<font color='#000000'>Are you sure?</font>"))
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            return true;
        } if (item.getItemId() == R.id.loginButton) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else {
            return false;
        }
        return false;
    }

    @Override
    public void onListFragmentInteraction(Event event) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * This is for scraping the webpage. Will start adding the code to do that soon.
     *
     * The current code for reading html is wrong. I will fix later.
     */
    private class GetXMLAsync extends AsyncTask<String, String, String> {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        @Override
        protected String doInBackground(String... strings) {
            try {
                    HttpURLConnection connection = null;
                    URL url = new URL("https://campusevents.uncc.edu/feed/cci-student-xml");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = url.openStream();
                    XmlPullParserFactory xmlFactoryObject = null;

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();
                    myparser.setInput(inputStream, null);

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(inputStream);

                    Element element = doc.getDocumentElement();
                    element.normalize();

                    NodeList nodeList = doc.getElementsByTagName("item");
                    String count = "";
                    String title = "";
                    String eventDatetime = "";
                    String location = "";
                    String eventType = "";
                    String organization = "";
                    String path = "";

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        //Map<String, Object> event = new HashMap<>();
                        Event event = new Event();
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element item = (Element) node;
                            try {
                                if (getValue("count", item) != null
                                        & getValue("title", item) != null
                                        & getValue("event-datetime", item) != null
                                        & getValue("location", item) != null
                                        & getValue("event-type", item) != null
                                        & getValue("organization", item) != null
                                        & getValue("path", item) != null) {

                                    count = getValue("count", item);
                                    title = getValue("title", item);
                                    eventDatetime = getValue("event-datetime", item);
                                    location = getValue("location", item);
                                    eventType = getValue("event-type", item);
                                    organization = getValue("organization", item);
                                    path = getValue("path", item);

                                    Log.i("eventsDebug", "doInBackground: Events:" + " "
                                            + count + " " + title + " " + eventDatetime + " "
                                            + location + " " + eventType + " " + organization + " "
                                            + path);

                                    event.setCount(count);
                                    event.setTitle(title);
                                    event.setDate(eventDatetime);
                                    event.setLocation(location);
                                    event.setType(eventType);
                                    event.setOrganization(organization);
                                    event.setLink(path);

                                    Log.i("eventToString", "doInBackground: Event.toString()" + event.toString());

                                    events.add(event);

                                } else {
                                    Log.d("XMLDebug", "doInBackground: The parse of the XML failed. ");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                connection.disconnect();
            } catch (XmlPullParserException | IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("events", events);
            homeFragment.setArguments(bundle);

            return null;
        }

        private String getValue(String tag, Element element) {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = nodeList.item(0);
            return node.getNodeValue();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO Check if there are new events to add to the database. If there are add them. If not, do nothing.

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}
