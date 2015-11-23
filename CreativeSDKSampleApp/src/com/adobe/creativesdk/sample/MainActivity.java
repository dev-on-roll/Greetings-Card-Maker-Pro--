package com.adobe.creativesdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.adobe.creativesdk.color.AdobeColorPickerMode;
import com.adobe.creativesdk.color.AdobeColorPickerResult;
import com.adobe.creativesdk.color.AdobeUXColorComponentLauncher;

public class MainActivity extends Activity {
    private static final int COLOR_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button move_to_step_2 = (Button)findViewById(R.id.button);
        final EditText occasion = (EditText)findViewById(R.id.editText);
        final EditText message = (EditText)findViewById(R.id.editText2);

        final Spinner spinner_color = (Spinner)findViewById(R.id.spinner);
        final Spinner spinner_size = (Spinner)findViewById(R.id.spinner2);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.spincolor,android.R.layout.simple_spinner_dropdown_item);
        spinner_color.setAdapter(adapter);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,R.array.spinsize,android.R.layout.simple_spinner_dropdown_item);
        spinner_size.setAdapter(adapter1);

        spinner_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    occasion.setTextColor(Color.WHITE);
                    occasion.setBackgroundColor(Color.rgb(74, 20, 140));
                    message.setTextColor(Color.WHITE);
                    message.setBackgroundColor(Color.rgb(74, 20, 140));
                } else if (position == 1) {
                    occasion.setTextColor(Color.WHITE);
                    occasion.setBackgroundColor(Color.rgb(183, 28, 28));
                    message.setTextColor(Color.WHITE);
                    message.setBackgroundColor(Color.rgb(183, 28, 28));
                } else if (position == 2) {

                    occasion.setTextColor(Color.WHITE);
                    occasion.setBackgroundColor(Color.rgb(0, 77, 64));
                    message.setTextColor(Color.WHITE);
                    message.setBackgroundColor(Color.rgb(0, 77, 64));
                } else if (position == 3) {

                    occasion.setTextColor(Color.WHITE);
                    occasion.setBackgroundColor(Color.rgb(245, 0, 87));

                    message.setTextColor(Color.WHITE);
                    message.setBackgroundColor(Color.rgb(245, 0, 87));

                } else if (position == 4) {
                    new AdobeUXColorComponentLauncher.Builder(MainActivity.this, COLOR_REQUEST_CODE).build().launch();
                //    occasion.setTextColor(Color.WHITE);
                 //   occasion.setBackgroundColor(colorPickerResult.getColor());

                  //  occasion.setBackgroundColor(Color.rgb(13, 71, 161));

                    // message.setTextColor(Color.WHITE);
                    // message.setBackgroundColor(Color.rgb(13, 71, 161));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_size.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                {

                    occasion.setTextSize(20);
                }else if (position == 1)
                {
                    occasion.setTextSize(25);

                }else if(position==2)
                {
                    occasion.setTextSize(30);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        move_to_step_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int get_spinner_size = spinner_size.getSelectedItemPosition();
                ColorDrawable cd = (ColorDrawable)occasion.getBackground();
             //   int colorCode = cd.getColor();
                int get_spinner_color = cd.getColor();
             //   occasion.getBackground()
                Intent i = new Intent(MainActivity.this,Step2.class);

                i.putExtra("occasion",occasion.getText().toString());
                i.putExtra("message",message.getText().toString());
                i.putExtra("size",get_spinner_size);
                i.putExtra("color",get_spinner_color);

                startActivity(i);
            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == COLOR_REQUEST_CODE) {
            AdobeColorPickerResult colorPickerResult = AdobeColorPickerResult.fromResultIntent(resultCode, data);
            if (colorPickerResult != null && !colorPickerResult.isCancelled() && colorPickerResult.getMode() == AdobeColorPickerMode.SINGLECOLOR) {
                if (colorPickerResult.isAllColorsOptionSelected()) {
                    Toast toast = Toast.makeText(this, "All Colors Option Selected", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    EditText occasion = (EditText)findViewById(R.id.editText);
                    EditText message = (EditText)findViewById(R.id.editText2);


                    occasion.setTextColor(Color.WHITE);
                    occasion.setBackgroundColor(colorPickerResult.getColor());
                    message.setTextColor(Color.WHITE);
                    message.setBackgroundColor(colorPickerResult.getColor());
                }
            }
            if (colorPickerResult != null && !colorPickerResult.isCancelled() && colorPickerResult.getMode() == AdobeColorPickerMode.THEME) {
                int[] theme = colorPickerResult.getTheme();
                for (int i = 0; i < 5; i++) {

                    EditText occasion = (EditText)findViewById(R.id.editText);
                    EditText message = (EditText)findViewById(R.id.editText2);

                    occasion.setTextColor(Color.WHITE);
                    occasion.setBackgroundColor(theme[i]);
                    message.setTextColor(Color.WHITE);
                    message.setBackgroundColor(theme[i]);


                }
            }
        }
    }

}
