package com.example.tcptest.Models;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.example.tcptest.Controllers.ImageClientController;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPImageClient {

    ImageClientController controller;

    public TCPImageClient(ImageClientController controller){
        this.controller = controller;
    }

    public void sendMessage(String ip, Bitmap bitmap){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        String IP = ip;

        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background
                try{
                    //Open socket and output stream
                    Socket mSocket = new Socket(IP, 9700);
                    DataOutputStream outputStream = new DataOutputStream(mSocket.getOutputStream());
                    //outputStream.writeUTF("Message from second device");

                    //Compress Bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] array = bos.toByteArray();

                    //Add byte array to Output stream
                    outputStream.writeInt(array.length);
                    outputStream.write(array, 0, array.length);

                    //Close Stream
                    outputStream.close();
                    mSocket.close();

                } catch (Exception e){
                    e.printStackTrace();
                }

                //Handler
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        controller.onMessageSent();
                    }
                });
            }
        });
    }

    public static File bitmapToFile(Bitmap bitmap){
        File file = null;

        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.png");
            file.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapData = bos.toByteArray();

            //Write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);

        }catch (Exception e){
            e.printStackTrace();
        }

        return file;
    }
}
