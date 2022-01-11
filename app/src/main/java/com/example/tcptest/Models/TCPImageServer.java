package com.example.tcptest.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.example.tcptest.Controllers.ImageServerController;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPImageServer implements Runnable{

    ImageServerController controller;

    ServerSocket ss;
    Socket mySocket;
    DataInputStream dataInputStream;
    String message;
    Bitmap bitmap;

    int len;

    public TCPImageServer(ImageServerController controller){
        this.controller = controller;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    ss = new ServerSocket(9700);
                    while (true){
                        mySocket = ss.accept();
                        dataInputStream = new DataInputStream(mySocket.getInputStream());

                        len = dataInputStream.readInt();
                        byte[] data = new byte[len];
                        if (len > 0){
                            dataInputStream.readFully(data, 0, data.length);
                        }

                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        //message = dataInputStream.readUTF();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                controller.imageReceived(bitmap);
                                //controller.messageReceived(message);
                                controller.confirmMessageReceived();
                            }
                        });
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
