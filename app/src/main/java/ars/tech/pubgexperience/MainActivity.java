package ars.tech.pubgexperience;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView device_id_tv, detail_tv;
    EditText uuid_et, nick_et;
    Spinner nick_symbols_s;
    Button backup_b, referesh_b, restore_b, increment_b, random_b, update_b, decrement_b, copy_b, backup_data_b, detail_b, restore_data_b, clear_b;
    ArrayAdapter nick_symbols_adapter;
    String device_id_string, uuid_string, device_id_updated;
    String[] nick_names_symbols = new String[] { "么", "々", "ツ", "乡", "彡", "๛", "☆", "★", "♛", "『』", "【】" };


    File backup_folder = new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience"),
            device_id_file_save = new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience/Saved/device_id.xml"),
            device_id_file_backup = new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience/device_id.xml"),
            active_save_file =  new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/SaveGames/Active.sav"),
            active_save_file_save =  new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience/Saved/Active.sav"),
            active_save_file_backup =  new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience/Active.sav"),
            cached_save_file =  new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/SaveGames/Cached.sav"),
            cached_save_file_save =  new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience/Saved/Cached.sav"),
            cached_save_file_backup =  new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience/Cached.sav"),
            settingconfig_slot_save_file =  new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/SaveGames/SettingConfig_Slot.sav"),
            settingconfig_slot_save_file_save =  new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience/Saved/SettingConfig_Slot.sav"),
            settingconfig_slot_save_file_backup =  new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience/SettingConfig_Slot.sav"),
            paks_file =  new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks"),
            paks_file_backup =  new File(Environment.getExternalStorageDirectory().toString() + "/PUBGExperience/Paks");

    public void save_data(){
        try {
            if(Helper.device_id_file.isFile() && !device_id_file_save.isFile()) {
                Helper.copyFile(Helper.device_id_file, device_id_file_save);
            }
            if(active_save_file.isFile() && !active_save_file_save.isFile()) {
                Helper.copyFile(active_save_file, active_save_file_save);
            }
            if(cached_save_file.isFile() && !cached_save_file_save.isFile()) {
                Helper.copyFile(cached_save_file, cached_save_file_save);
            }
            if(settingconfig_slot_save_file.isFile() && !settingconfig_slot_save_file_save.isFile()) {
                Helper.copyFile(settingconfig_slot_save_file, settingconfig_slot_save_file_save);
            }

        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void backup_device_id(){
        if(Helper.device_id_file.isFile()) {
            try {
                Helper.copyFile(Helper.device_id_file, device_id_file_backup);
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refresh_device_id(){
        if (Helper.device_id_file.isFile()){
            try {
                device_id_string = Helper.readFileToString(Helper.device_id_file);
                update_b.setText("Update");
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            device_id_string = Helper.device_id_sample;
            update_b.setText("Sample");
        }
        device_id_tv.setText(device_id_string);
        refresh_uuid();
    }

    public void restore_device_id(){
        if(device_id_file_backup.isFile()) {
            try {
                Helper.copyFile(device_id_file_backup, Helper.device_id_file);
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            refresh_device_id();
        }
    }

    public void increment_uuid(){
        if (uuid_string.length() < 32)
            return;
        try {
            uuid_et.setText(Helper.increment(uuid_string));
            update_uuid();
        } catch (NullPointerException e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void update_uuid(){
        device_id_updated = device_id_string.replace("<string name=\"uuid\">" + uuid_string + "</string>", "<string name=\"uuid\">" + uuid_et.getText().toString() + "</string>");
        try {
            Helper.writeStringToFile(device_id_updated, Helper.device_id_file);
            uuid_string = uuid_et.getText().toString();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        refresh_device_id();
    }

    public void refresh_uuid(){
        uuid_string = Helper.stringBetween(device_id_string, "<string name=\"uuid\">", "</string>");
        if (uuid_string.equals("sample"))
            uuid_et.setText(Helper.randomAlphanumeric(32).toLowerCase());
        else
            uuid_et.setText(uuid_string);
    }

    public void decrement_uuid(){
        if (uuid_string.length() < 32)
            return;
        try {
            uuid_et.setText(Helper.decrement(uuid_string));
            update_uuid();
        } catch (NullPointerException e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void backup_data(){
        try {
            if(active_save_file.isFile()) {
                Helper.copyFile(active_save_file, active_save_file_backup);
            }
            if(cached_save_file.isFile()) {
                Helper.copyFile(cached_save_file, cached_save_file_backup);
            }
            if(settingconfig_slot_save_file.isFile()) {
                Helper.copyFile(settingconfig_slot_save_file, settingconfig_slot_save_file_backup);
            }
            if(paks_file.isDirectory()) {
                Helper.copyDirectory(paks_file,paks_file_backup);
            }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void restore_data(){
        try {
            if(active_save_file_backup.isFile()) {
                Helper.copyFile(active_save_file_backup, active_save_file);
            }
            if(cached_save_file_backup.isFile()) {
                Helper.copyFile(cached_save_file_backup, cached_save_file);
            }
            if(settingconfig_slot_save_file_backup.isFile()) {
                Helper.copyFile(settingconfig_slot_save_file_backup, settingconfig_slot_save_file);
            }
            if(paks_file_backup.isDirectory()) {
                Helper.copyDirectory(paks_file_backup, paks_file);
            }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void get_detail(){
        StringBuilder sb = new StringBuilder();
        sb.append("Android ID : " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + "\n\n");
        if ("1".equals(Settings.Secure.getString(getContentResolver(), Settings.Secure.ADB_ENABLED)))
            sb.append("[Android Debug Bridge is Enabled]" + "\n\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                sb.append("[Permission to Read SD Storage is Granted]" + "\n\n");
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                sb.append("[Permission to Write SD Storage is Granted]" + "\n\n");
            }
        }
        sb.append("Device ID : " + uuid_string + "\n\n");
        sb.append("PUBGExperience Backup Folder:-" + "\n" + Helper.getFilesCount(backup_folder).replaceAll(".+?/PUBGExperience","PUBGExperience"));
        detail_tv.setText(sb.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        setContentView(R.layout.activity_main);

        device_id_tv = (TextView)findViewById(R.id.device_id_textview);
        detail_tv =  (TextView)findViewById(R.id.detail_textview);
        uuid_et = (EditText)findViewById(R.id.uuid_edittext);
        nick_et = (EditText)findViewById(R.id.nick_edittext);
        nick_symbols_s = (Spinner)findViewById(R.id.nick_symbols_spinner);
        backup_b = (Button)findViewById(R.id.backup_button);
        referesh_b = (Button)findViewById(R.id.referesh_button);
        restore_b = (Button)findViewById(R.id.restore_button);
        increment_b = (Button)findViewById(R.id.increment_button);
        random_b = (Button)findViewById(R.id.random_button);
        update_b = (Button)findViewById(R.id.update_button);
        decrement_b = (Button)findViewById(R.id.decrement_button);
        copy_b = (Button)findViewById(R.id.copy_button);
        backup_data_b = (Button)findViewById(R.id.backup_data_button);
        detail_b = (Button)findViewById(R.id.detail_button);
        restore_data_b = (Button)findViewById(R.id.restore_data_button);
        clear_b = (Button)findViewById(R.id.clear_button);

        save_data();
        refresh_device_id();
        refresh_uuid();

        backup_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backup_device_id();
            }
        });

        referesh_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_device_id();
            }
        });

        restore_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restore_device_id();
            }
        });

        increment_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment_uuid();
            }
        });

        random_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uuid_et.setText(Helper.randomAlphanumeric(32).toLowerCase());
            }
        });

        update_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_uuid();
            }
        });

        decrement_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement_uuid();
            }
        });

        copy_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 11) {
                    ((ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE)).setText(nick_et.getText().toString());
                    Toast.makeText((Context)MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("label", nick_et.getText().toString()));
                Toast.makeText((Context)MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        backup_data_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backup_data();
            }
        });

        detail_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_detail();
            }
        });

        restore_data_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restore_data();
            }
        });

        clear_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.delete(backup_folder);
                get_detail();
            }
        });

        uuid_et.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == 0 && keyCode == 66) {
                    update_b.performClick();
                    return true;
                }
                return false;
            }
        });

        uuid_et.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                if (uuid_et.getText().toString().trim().length() < 32) {
                    uuid_et.setError("Invalid Length");
                    update_b.setEnabled(false);
                } else {
                    update_b.setEnabled(true);
                    uuid_et.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        nick_symbols_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nick_names_symbols);
        nick_symbols_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nick_symbols_s.setAdapter((SpinnerAdapter)nick_symbols_adapter);
        nick_symbols_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                nick_et.setText(nick_symbols_s.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }
}