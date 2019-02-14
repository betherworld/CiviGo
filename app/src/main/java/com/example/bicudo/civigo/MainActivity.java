package com.example.bicudo.civigo;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 20;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_proposals:
                    mTextMessage.setText("Porposals");
                    return true;
                case R.id.navigation_fixes:
                    mTextMessage.setText("Fixes");
                    return true;
                case R.id.navigation_map:
	    		    return true;
            };
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        findViewById(R.id.navigation_proposals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent porposalActivity = new Intent(MainActivity.this, ProposalActivity.class);
                startActivity(porposalActivity);
            }
        });

        findViewById(R.id.navigation_fixes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fixesActivity = new Intent(MainActivity.this, FixesActivity.class);
                startActivity(fixesActivity);
            }
        });

        findViewById(R.id.navigation_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
		        Intent mapActivity = new Intent(MainActivity.this, MapActivity.class);
	    	    startActivity(mapActivity);

        	}
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        // Again, some permission checks, in this case for location access.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);
        }
    }

  }
