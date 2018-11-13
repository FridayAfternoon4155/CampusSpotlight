package com.fridayafternoon.campusspotlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    DialogInterface.OnClickListener dialogClickListener;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseStorage mStorage;
    StorageReference storageReference;
    String usersName;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    try {
                        new GetXMLAsync().execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
            builder.setMessage("Are you sure?")
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


    /**
     * This is for scraping the webpage. Will start adding the code to do that soon.
     *
     * The current code for reading html is wrong. I will fix later.
     */
    private static class GetXMLAsync extends AsyncTask<String, String, String> {
        private InputStream inputStream = new URL("https://campusevents.uncc.edu/feed/cci-student-xml").openStream();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        private GetXMLAsync() throws IOException {}

        @Override
        protected String doInBackground(String... strings) {
            XmlPullParserFactory xmlFactoryObject = null;
            try {
                xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser myparser = xmlFactoryObject.newPullParser();
                myparser.setInput(inputStream, null);

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputStream);

                Element element = doc.getDocumentElement();
                element.normalize();

                NodeList nodeList = doc.getElementsByTagName("item");

                for (int i=0; i<nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Map<String, Object> event = new HashMap<>();
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element item = (Element) node;

                        String count = getValue("count", item);
                        String title = getValue("title", item);
                        String eventDatetime = getValue("event-datetime", item);
                        String location = getValue("location", item);
                        String eventType = getValue("event-type", item);
                        String organization = getValue("organization", item);
                        String path = getValue("path", item);

                        event.put("count", count);
                        event.put("title", title);
                        event.put("eventDatetime", eventDatetime);
                        event.put("location", location);
                        event.put("eventType", eventType);
                        event.put("organization", organization);
                        event.put("path", path);

                        db.collection("events")
                                .document("event")
                                .set(event)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("info", "onSuccess: Event Added. ");
                            }
                        })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("info", "onFailure: Failed to add event.");
                                }
                            });
                    }
                }
 




            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            return null;
        }

        private static String getValue(String tag, Element element) {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = nodeList.item(0);
            return node.getNodeValue();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO Check if there are new events to add to the database. If there are add them. If not, you know what to do.

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
