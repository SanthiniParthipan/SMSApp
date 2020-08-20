package sg.edu.rp.c346.id19028654.smsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver br;
    TextView tvTo,tvCon;
    EditText etTo,etCon;
    Button btnSend,btnVia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTo=findViewById(R.id.textViewTo);
        tvCon=findViewById(R.id.textViewContent);
        etTo= findViewById(R.id.editTextTo);
        etCon=findViewById(R.id.editTextContent);
        btnSend= findViewById(R.id.buttonSend);
        btnVia=findViewById(R.id.buttonVia);

        checkPermission();

        btnVia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = etTo.getText().toString();
                String content = etCon.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + num));
                intent.putExtra("sms_body", content);
                startActivity(intent);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SmsManager smsManager = SmsManager.getDefault();
                if (etTo.getText().toString().contains(",")) {
                    String[] numArray = etTo.getText().toString().split(", ");
                    for (int x = 0; x < numArray.length; x++) {
                        smsManager.sendTextMessage(numArray[x], null, etCon.getText().toString(), null, null);
                        Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Message failed", Toast.LENGTH_LONG).show();
                }





            }
        });
    }
    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(br);
    }
}