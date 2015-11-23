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
import android.app.Application;

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.adobeinternal.analytics.AdobeAnalyticsManager;
import com.adobe.creativesdk.foundation.adobeinternal.analytics.AdobeAnalyticsMode;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.adobe.creativesdk.foundation.internal.auth.AdobeAuthIMSEnvironment;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/***************************************************************************************************************************************
*  For using the Creative SDK, the Application will need to do the following mandatory Steps:
*  (1) Initialize AdobeCSDKFoundation
*  (2) Set ClientID and ClientSecret that you have obtained from Adobe(https://creativesdk.adobe.com/myapps.html)
*  (3) Use AuthSessionHelper for listening to the authentication status callbacks.
*
*  This class shows how to initialize the SDK and set the Client ID and Client Secret.
*  The application needs to define an ‘Application’ object and need to implement IAdobeAuthClientCredentials.
*  (1)Call AdobeCSDKFoundation.initializeCSDKFoundation to initialize Adobe Creative SDK.
*  (2)Override getClientID() and getClientSecret() to return the Client ID and Client Secret that you have obtained for your application.
****************************************************************************************************************************************/
public class CreativeSDKSampleApplication extends Application implements IAdobeAuthClientCredentials {
    private static final String CREATIVE_SDK_SAMPLE_CLIENT_ID =  "b7d5b07ece8048bebdacd47f67608f78";
    private static final String CREATIVE_SDK_SAMPLE_CLIENT_SECRET = "33174ab3-ac8e-466b-a17a-7a2627bc7411";

    @Override
    public void onCreate() {

        super.onCreate();
        /*Initialize the Creative SDK Foundation library.
        *This is the first call that needs to be made before using Creative SDK Foundation library.*/
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext(), AdobeAuthIMSEnvironment.AdobeAuthIMSEnvironmentProductionUS);

        AdobeAnalyticsManager.getInstance().enableAnalyticsReporting(AdobeAnalyticsMode.ADOBE_ANALYTICS_MODE_TEST);

       /* AdobeUXAuthManagerRestricted uxManager = AdobeUXAuthManagerRestricted.getSharedAuthManagerRestricted();
        uxManager.setAuthenticationEnvironment(AdobeAuthIMSEnvironment.AdobeAuthIMSEnvironmentProductionUS);*/

        //AdobeUXAuthManagerRestricted uxManagerRestricted = AdobeUXAuthManagerRestricted.getSharedAuthManagerRestricted();
        //uxManagerRestricted.setAdditionalAuthenticationParameters(ClientState, DeviceInfoProvider.getUniqueDeviceID(activity), android.os.Build.MODEL, null, null);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/AdobeClean-Light.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_SAMPLE_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_SAMPLE_CLIENT_SECRET;
    }
}
