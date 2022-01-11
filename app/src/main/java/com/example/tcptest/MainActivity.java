 package com.example.tcptest;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcptest.Controllers.ImageClientController;
import com.example.tcptest.Controllers.ImageServerController;

import java.io.IOException;

 public class MainActivity extends AppCompatActivity {

    private ImageServerController serverController;
    private ImageClientController clientController;
    private ImageView imageView;
    private Button button;
    private EditText editText;
    private TextView textView;
    private TextView txtIP;
    private Button btnUpload;

    int SELECT_PICTURE = 200;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        btnUpload = findViewById(R.id.btnUpload);
        editText = findViewById(R.id.editText);
        txtIP = findViewById(R.id.txtIP);
        textView = findViewById(R.id.textView);

        serverController = new ImageServerController(this);
        serverController.startServer();

        clientController = new ImageClientController(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToServer();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        //Get device's IP
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        txtIP.setText("Device's address: " + ipAddress);
    }

    //Called by serverController
    public void setImageView(Bitmap image){
        imageView.setImageBitmap(image);
    }

    //Call sendMessage from clientController
    public void sendMessageToServer(){
        clientController.sendMessage(editText.getText().toString(), bitmap);
    }

    //Called on server initialization
    public void responseToast(){
        Toast.makeText(this, "Message Sent", Toast.LENGTH_LONG).show();
    }

     public void confirmMessageReceived(){
         Toast.makeText(this, "Got a message from client", Toast.LENGTH_LONG).show();
     }

    //Called by serverController
    public void messageReceived(String message){
        textView.setText(message);
    }

    void imageChooser(){

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imageView.setImageURI(selectedImageUri);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}