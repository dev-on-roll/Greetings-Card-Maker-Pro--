/*
 * Copyright (c) 2015 Adobe Systems Incorporated. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */
 
package com.adobe.creativesdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.adobe.creativesdk.color.AdobeColorPickerMode;
import com.adobe.creativesdk.color.AdobeColorPickerResult;
import com.adobe.creativesdk.color.AdobeUXColorComponentLauncher;


public class CreativeSDKSampleColorActivity extends Activity {

    private static final int COLOR_REQUEST_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_color);
    }

   /* public void openColorPicker(View view) {
        new AdobeUXColorPickerLauncher.Builder(this).build().launch();
    }

    public void openThemePicker(View view) {
        new AdobeUXThemePickerLauncher.Builder(this).build().launch();
    }*/

    public void openColorComponent(View view) {
        new AdobeUXColorComponentLauncher.Builder(this, COLOR_REQUEST_CODE).build().launch();
    }

    /*public void openColorPickerWithAllColorsOptions(View view) {
        new AdobeUXColorPickerLauncher.Builder(this).setAllColorsOptionEnabled(true).setAllColorsOptionSelected(true).setEnabledPickerTypes(EnumSet.of(AdobeColorPickerType.WHEEL)).build().launch();
    }*/

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
                    Toast toast = Toast.makeText(this, "Selected Color", Toast.LENGTH_SHORT);
                    toast.getView().setBackgroundColor(colorPickerResult.getColor());
                    toast.show();
                }
            }
            if (colorPickerResult != null && !colorPickerResult.isCancelled() && colorPickerResult.getMode() == AdobeColorPickerMode.THEME) {
                int[] theme = colorPickerResult.getTheme();
                for (int i = 0; i < 5; i++) {
                    Toast toast = Toast.makeText(this, "Selected Color", Toast.LENGTH_SHORT);
                    toast.getView().setBackgroundColor(theme[i]);
                    toast.show();

                }
            }
        }
    }
}

