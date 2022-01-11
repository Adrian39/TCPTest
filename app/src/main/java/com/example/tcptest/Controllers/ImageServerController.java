package com.example.tcptest.Controllers;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.example.tcptest.MainActivity;
import com.example.tcptest.Models.TCPImageClient;
import com.example.tcptest.Models.TCPImageServer;

public class ImageServerController {

    private MainActivity mainActivity;
    private TCPImageServer model;

    public ImageServerController(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        model = new TCPImageServer(this);
    }

    //Called by Main Activity to start server
    public void startServer(){
        Thread thread= new Thread(model);
        thread.start();
        Toast.makeText(mainActivity, "Server started", Toast.LENGTH_LONG).show();
    }

    //Called by Server model
    public void imageReceived(Bitmap image){
        mainActivity.setImageView(image);
    }

    //Called by Server model
    public void messageReceived(String message){
        mainActivity.messageReceived(message);
    }

    //Confirm Message received
    public void confirmMessageReceived(){
        mainActivity.confirmMessageReceived();
    }
}
