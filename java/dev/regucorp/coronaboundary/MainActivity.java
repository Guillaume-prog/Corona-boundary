package dev.regucorp.coronaboundary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import dev.regucorp.coronaboundary.notifications.NotificationSender;
import dev.regucorp.coronaboundary.position.LocationHandler;

public class MainActivity extends BaseActivity {

    // Views
    private EditText addressView;
    private Button findLocationBtn, startMapsBtn;

    // Location Stuff
    private LocationHandler handler;
//bonjour
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTag("MAIN");

        handler = LocationHandler.getInstance();
        NotificationSender sender = NotificationSender.getInstance(this);
        sender.notify("Test", "test message");

        initViews();
        run();
    }

    private void run() {
        addressView.setText( sharedPreferences.getString("home_address", "") );

        findLocationBtn.setOnClickListener(v -> {
            log("Click");
            handler.getLocation(location -> {
                addressView.setText(handler.getAddressFrom(location));
            });
        });

        startMapsBtn.setOnClickListener(v -> {
            String address = addressView.getText().toString();
            log(address);
            if(address.equals("")) {
                toast(R.string.main_fillHome);
            } else {
                editor.putString("home_address", address);
                editor.apply();

                Intent mapIntent = new Intent(this, MapsActivity.class);
                mapIntent.putExtra("address", handler.getLocationFrom(address));

                startActivity(mapIntent);
            }
        });
    }

    private void initViews() {
        addressView = findViewById(R.id.main_addressInput);
        findLocationBtn = findViewById(R.id.main_getMyLocation);
        startMapsBtn = findViewById(R.id.main_startBtn);
    }
}
