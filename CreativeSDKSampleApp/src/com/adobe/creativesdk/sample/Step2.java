package com.adobe.creativesdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
public class Step2 extends Activity {
    private static final int SELECT_PICTURE = 1;
    ImageView imageview;
    Button sendcard;
    RelativeLayout mylayout;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
        imageview = (ImageView)findViewById(R.id.imageView);
        TextView get_msg = (TextView)findViewById(R.id.textView4);
        TextView head = (TextView)findViewById(R.id.occa);
        sendcard= (Button)findViewById(R.id.button4);
        mylayout = (RelativeLayout)findViewById(R.id.layout1);
        Bundle extras = getIntent().getExtras();
        if(extras==null){
            return;
        }
        String s1 = extras.getString("occasion");
        String s2 = extras.getString("message");

        int color1 = extras.getInt("color");
        int size1 = extras.getInt("size");

        if(color1 == 0)
        {

            head.setTextColor(Color.WHITE);
            head.setBackgroundColor(Color.rgb(74, 20, 140));
            get_msg.setTextColor(Color.WHITE);
            get_msg.setBackgroundColor(Color.rgb(74, 20, 140));
            mylayout.setBackgroundColor(Color.rgb(74, 20, 140));
        }else if (color1 == 1)
        {

            head.setTextColor(Color.WHITE);
            head.setBackgroundColor(Color.rgb(183, 28, 28));
            get_msg.setTextColor(Color.WHITE);
            get_msg.setBackgroundColor(Color.rgb(183, 28, 28));

            mylayout.setBackgroundColor(Color.rgb(183, 28, 28));
        }else if(color1 == 2)
        {

            head.setTextColor(Color.WHITE);
            head.setBackgroundColor(Color.rgb(0, 77, 64));
            get_msg.setTextColor(Color.WHITE);
            get_msg.setBackgroundColor(Color.rgb(0, 77, 64));
            mylayout.setBackgroundColor(Color.rgb(0, 77, 64));
        }else if(color1==3)
        {

            head.setTextColor(Color.WHITE);
            head.setBackgroundColor(Color.rgb(245,0,87));

            get_msg.setTextColor(Color.WHITE);
            get_msg.setBackgroundColor(Color.rgb(245,0,87));
            mylayout.setBackgroundColor(Color.rgb(245,0,87));
        }else if(color1==4)
        {


            head.setTextColor(Color.WHITE);
            head.setBackgroundColor(color1);

            get_msg.setTextColor(Color.WHITE);
            get_msg.setBackgroundColor(color1);
            mylayout.setBackgroundColor(color1);
        }
        else
        {

            head.setTextColor(Color.WHITE);
            head.setBackgroundColor(color1);

            get_msg.setTextColor(Color.WHITE);
            get_msg.setBackgroundColor(color1);
            mylayout.setBackgroundColor(color1);
        }


        if(size1 == 0)
        {

            head.setTextSize(20);
        }else if (size1 == 1)
        {
            head.setTextSize(25);
        }else if(size1==2)
        {
            head.setTextSize(30);
        }


        head.setText(s1);
        get_msg.setText(s2);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
        //  mylayout = (RelativeLayout)findViewById(R.id.layout1);

        sendcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mylayout.setDrawingCacheEnabled(true);
                mylayout.buildDrawingCache();
                bitmap = mylayout.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                final byte[] b = baos.toByteArray();
                //  Toast.makeText(getApplicationContext(),"card made",Toast.LENGTH_LONG).show();
                Intent intent_1 = new Intent(Step2.this, Step3.class);
                intent_1.putExtra("picture",b);
                startActivity(intent_1);
                finish();

            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                imageview.setImageURI(selectedImageUri);

            }
        }
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

}
