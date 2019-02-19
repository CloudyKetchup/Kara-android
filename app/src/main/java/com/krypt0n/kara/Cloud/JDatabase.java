package com.krypt0n.kara.Cloud;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import com.krypt0n.kara.R;
import com.mongodb.*;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintStream;

public class JDatabase {
    private MongoClient mongo_client;
    private boolean mongo_connected = false;
    private DB database;                                    //Cloud.Database object
    private DBCollection users;                             //collection
    private DBObject temp_data;
    private BasicDBObject user,initial_user,acc;
    private String acc_name,acc_pass;
    private String ip = "192.168.0.14";
    private int port  = 27017;
    private Account local_acc;
    private JSONObject config,updated_config;               //account config for offline mode
    private File acc_file;
    private FileWriter fw;
    private Activity activity;

    public DBObject get_temp_data() {
        return temp_data;
    }
    public JSONObject get_config(){
        return config;
    }
    public JDatabase(Activity activity){
        this.activity = activity;
    }
    //create database
    public void init() {
        try {
            mongo_client = new MongoClient("mongodb://192.168.0.14/27017");
            mongo_connected = true;
            Snackbar.make(activity.findViewById(R.id.root_layout),"Mongo Connected",Snackbar.LENGTH_LONG).show();
        }catch (Exception e){
            File f = new File(activity.getFilesDir() + "/log.txt");
            PrintStream p = null;
            try {
                p = new PrintStream(f);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace(p);
            Snackbar.make(activity.findViewById(R.id.root_layout),"Mongo Fail",Snackbar.LENGTH_LONG).show();
        }
        if (mongo_connected) {
            database    = mongo_client.getDB("Kara");
            users       = database.getCollection("users");
            //clone of db data wich will be used
            temp_data   = users.findOne();
            Snackbar.make(activity.findViewById(R.id.root_layout),temp_data.toString(),Snackbar.LENGTH_LONG).show();
        }
    }
}
