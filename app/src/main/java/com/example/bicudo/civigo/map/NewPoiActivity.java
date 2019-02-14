package com.example.bicudo.civigo.map;

import com.example.bicudo.civigo.R;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class NewPoiActivity extends AppCompatActivity {

    protected Double lat;
    protected Double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poi);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Location location = extras.getParcelable("key1");
            if (location != null) {
                // update lat lon in activity
                displayOutputMethod(location);
            }
        }

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button okButton = (Button) findViewById(R.id.save_button);
        Button commentButton = (Button) findViewById(R.id.comment_button);

        // cancel action
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButtonAction();
            }
        });

        // save action
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okButtonAction();
            }
        });

        // comment action
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentButtonAction();
            }
        });
    }


    protected void cancelButtonAction() {
        finish();
    }

    protected void WriteLine(String file, String line, String type) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File directory = Environment.getExternalStorageDirectory();
                File f = new File(directory, file);
                f.createNewFile();
                FileOutputStream outStream = new FileOutputStream(f, true);
                PrintWriter writer = new PrintWriter(outStream);
                writer.println(line);
                writer.close();
                outStream.close();

                Context context = getApplicationContext();
                CharSequence text = type + "stored in" + file;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("FileLog", "SD card not mounted");
        }

    }

    protected void okButtonAction () {

        // get data
        EditText p_name = (EditText) findViewById(R.id.edtName);
        EditText p_ort = (EditText) findViewById(R.id.edtOrt);
        EditText p_plz = (EditText) findViewById(R.id.edtPLZ);
        EditText p_tags = (EditText) findViewById(R.id.edtTags);

        String line = String.valueOf(lon) + ";" + String.valueOf(lat) + ";" + p_name.getText()
                + ";" + p_ort.getText() + ";" + p_plz.getText() + ";" + p_tags.getText();

        WriteLine("pois.csv", line, "POIs");

    }

    protected void commentButtonAction () {

        // get data
        EditText p_name = (EditText) findViewById(R.id.edtName);
        RatingBar p_rate = (RatingBar) findViewById(R.id.ratingBar);
        EditText p_comment = (EditText) findViewById(R.id.comment_edit);

        String line = p_name.getText()
                + ";" + String.valueOf(p_rate.getRating()) + ";" + p_comment.getText();

        WriteLine("comments.csv", line, "comments");

    }

    protected void displayOutputMethod(Location loc) {
        TextView longitudeValue = (TextView) findViewById(R.id.tvLongitude);
        TextView latitudeValue = (TextView) findViewById(R.id.tvLatitude);

        // update class lat lon
        lon = loc.getLongitude();
        lat = loc.getLatitude();

        // update activity view lat lon
        longitudeValue.setText(String.valueOf(lon));
        latitudeValue.setText(String.valueOf(lat));
    }
}
