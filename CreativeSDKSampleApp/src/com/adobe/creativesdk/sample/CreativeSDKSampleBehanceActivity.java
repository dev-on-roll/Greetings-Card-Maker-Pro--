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
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.creativesdk.behance.AdobeBehanceAddWIPWorkflowOptions;
import com.adobe.creativesdk.behance.AdobeUXBehanceWorkflow;
import com.adobe.creativesdk.behance.IAdobeBehanceSDKGetUserProfileListener;
import com.behance.sdk.BehanceSDKUserProfile;
import com.behance.sdk.exception.BehanceSDKUserNotAuthenticatedException;
import com.behance.sdk.exception.BehanceSDKUserNotEntitledException;


public class CreativeSDKSampleBehanceActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.activity_behance);
        TextView textView= (TextView) findViewById(R.id.ic_behanceText);
        Typeface adobeClean = Typeface.createFromAsset(getAssets(), "fonts/AdobeClean-Regular.otf");
        Typeface adobeCleanLight = Typeface.createFromAsset(getAssets(), "fonts/AdobeClean-Light.otf");
        textView.setTypeface(adobeClean);

        TextView textView2= (TextView) findViewById(R.id.ic_appLibrary);
        Typeface adobeClean2 = Typeface.createFromAsset(getAssets(), "fonts/AdobeClean-Regular.otf");
        Typeface adobeCleanLight2 = Typeface.createFromAsset(getAssets(), "fonts/AdobeClean-Light.otf");
        textView2.setTypeface(adobeClean2);

        TextView textView3= (TextView) findViewById(R.id.ic_Stock);
        Typeface adobeClean3 = Typeface.createFromAsset(getAssets(), "fonts/AdobeClean-Regular.otf");
        Typeface adobeCleanLight3 = Typeface.createFromAsset(getAssets(), "fonts/AdobeClean-Light.otf");
        textView3.setTypeface(adobeClean3);
        //String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /* startAddProject
     *
     * Method to launches Add Project functionality of the Behance Component in the Creative SDK.
     */

    public void startAddProject(View view) {

        try {
                AdobeUXBehanceWorkflow.launchAddProject(this.getClass(), this);

        } catch (BehanceSDKUserNotAuthenticatedException e) {
            e.printStackTrace();
        } catch (BehanceSDKUserNotEntitledException e) {
            e.printStackTrace();
        }

    }

    /* startAddWIP
     *
     * Method to launches Add Work in progress functionality of the Behance Component in the Creative SDK.
     */

    public void startAddWIP(View view) {

        try {
            AdobeUXBehanceWorkflow.launchAddWIP(new AdobeBehanceAddWIPWorkflowOptions(), this);

        } catch (BehanceSDKUserNotAuthenticatedException e) {
            e.printStackTrace();
        } catch (BehanceSDKUserNotEntitledException e) {
            e.printStackTrace();
        }

    }

    /* startGetUserProfile
     *
     * Retrieves user profile information and displays as toast message.
     */

    public void startGetUserProfile(View view) {

        try {
            BehanceSDKUserProfile userProfile = new BehanceSDKUserProfile();
            userProfile.getFirstName();
            AdobeUXBehanceWorkflow.getBehanceUserProfile(new IAdobeBehanceSDKGetUserProfileListener() {
                @Override
                public void onGetUserProfileSuccess(BehanceSDKUserProfile userProfile) {

                    Toast t = new Toast(getApplicationContext());
                    StringBuilder textContent = new StringBuilder();
                    textContent.append(userProfile.getFirstName() + " ");
                    textContent.append(userProfile.getLastName() + "\n");
                    //textContent.append(userProfile.getCountry());
                    t.makeText(getApplicationContext(), textContent, Toast.LENGTH_SHORT).show();
                    t.setGravity(Gravity.CENTER, 0, 0);

                }

                @Override
                public void onEditProfileFailure(Exception exception) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

                }
            }, this);


        } catch (BehanceSDKUserNotAuthenticatedException e) {
            e.printStackTrace();
        } catch (BehanceSDKUserNotEntitledException e) {
            e.printStackTrace();
        }
        //it is needed otherwise even after pressing back, it will relaunch Behance
        //setCurrentState(STATE_HOME);

    }

}

