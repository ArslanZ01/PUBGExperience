package ars.tech.pubgexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

public class CopyNick extends AppCompatActivity {
    String nick_name = "么";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 11) {
            ((ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE)).setText(nick_name);
            return;
        }
        ((ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("label", nick_name));

        finish();
    }
}