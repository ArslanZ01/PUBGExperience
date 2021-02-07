package ars.tech.pubgexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;

public class RandomID extends AppCompatActivity {
    String uuid_random, device_id_string, uuid_string, device_id_updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uuid_random = Helper.randomAlphanumeric(32).toLowerCase();
        try {
            if (Helper.device_id_file.isFile())
                device_id_string = Helper.readFileToString(Helper.device_id_file);
            else
                device_id_string = Helper.device_id_sample;
            uuid_string = Helper.stringBetween(device_id_string, "<string name=\"uuid\">", "</string>");
            device_id_updated = device_id_string.replace("<string name=\"uuid\">" + uuid_string + "</string>", "<string name=\"uuid\">" + uuid_random + "</string>");
            Helper.writeStringToFile(device_id_updated, Helper.device_id_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }
}