package com.adobe.creativesdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;


public class Step3 extends Activity {
    ImageView imageg;
    Button sendwhatsapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
        sendwhatsapp = (Button)findViewById(R.id.button3);
        imageg = (ImageView) findViewById(R.id.imageView2);
        Bundle extras = getIntent().getExtras();
        if(extras==null){
            return;
        }
        byte[] b_1 = extras.getByteArray("picture");

        final Bitmap bmp = BitmapFactory.decodeByteArray(b_1, 0, b_1.length);
        imageg.setImageBitmap(bmp);


        String root = Environment.getExternalStorageDirectory().toString();
        final File myDir = new File(root + "/greetings_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        final String fname = "Imge-"+ n +".png";
        final File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG,100, out);
            Toast.makeText(getApplicationContext(), "image saved in " + myDir + fname, Toast.LENGTH_LONG).show();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // imageg.setImageBitmap(bmp);
        //    sendBroadcast(new Intent(
        //           Intent.ACTION_MEDIA_MOUNTED,
        //         Uri.parse("file://" + Environment.getExternalStorageDirectory())));

        sendwhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String completePath = myDir+"/"+fname;

                File file1 = new File(completePath);
                Uri imageUri = Uri.fromFile(file1);
                Toast.makeText(getApplicationContext(),"path "+completePath,Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                intent.putExtra(Intent.EXTRA_STREAM,imageUri);
                intent.setType("image/png");
                // intent.setPackage("com.whatsapp");
                startActivity(intent);
                finish();
            }
        });






    }
    @Override
    protected void onDestroy() {

        super.onDestroy();

    }


}
