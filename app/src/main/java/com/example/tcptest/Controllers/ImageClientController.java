package com.example.tcptest.Controllers;

import android.graphics.Bitmap;

import com.example.tcptest.MainActivity;
import com.example.tcptest.Models.TCPImageClient;

public class ImageClientController {
    private MainActivity mainActivity;
    private TCPImageClient model;

    public ImageClientController(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        model = new TCPImageClient(this);
    }

    public void sendMessage(String ip, Bitmap image){
        model.sendMessage(ip, image);
    }

    public void onMessageSent(){
        mainActivity.responseToast();
    }
}
