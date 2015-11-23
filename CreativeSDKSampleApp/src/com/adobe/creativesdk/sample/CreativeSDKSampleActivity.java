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
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adobe.creativesdk.foundation.applibrary.AdobeAppLibraryLauncher;
import com.adobe.creativesdk.foundation.auth.AdobeAuthException;
import com.adobe.creativesdk.foundation.auth.AdobeAuthSessionHelper;
import com.adobe.creativesdk.foundation.auth.AdobeAuthSessionLauncher;
import com.adobe.creativesdk.foundation.auth.AdobeUXAuthManager;
import com.adobe.creativesdk.foundation.internal.utils.AdobeCSDKException;
import com.adobe.creativesdk.foundation.storage.AdobeUXAssetBrowser;
import com.adobe.creativesdk.foundation.storage.AdobeUXAssetBrowserConfiguration;
import com.adobe.creativesdk.foundation.storage.AdobeUXAssetBrowserOption;

import java.util.EnumSet;

//import com.adobe.creativesdk.foundation.applibrary.AppLibrarySelector;

public class CreativeSDKSampleActivity extends Activity {
    private static final int STATE_HOME = 0;
    private static final int STATE_ASSET_BROWSER = 1;
    private static final int STATE_ASSET_GRIDVIEW = 2;
    private static final int STATE_ASSET_GRIDVIEW_IMAGE_DETAIL = 3;
    private static final int STATE_BEHANCE = 4;
    private static final int STATE_LAUNCH_GRID_FOR_SELECTED = 5;
    private static final int STATE_NONE = 6;
    private static final int STATE_COLOR = 7;
    private static final int IMAGE_DETAIL_PADDING = 10;
    private static final int TEXT_SIZE = 12;
    private static final int TEXT_BACKGROUND = Color.rgb(64, 64, 64);
    private static final int TEXT_FOREGROUND = Color.rgb(255, 255, 255);
    private static final float TEXT_ALPHA = 0.8f;
    private final int CREATIVE_SDK_SAMPLE_REQUEST_CODE = 100;
    private final int DEFAULT_LOGIN_ACTIVITY_REQUEST_CODE = 202;

    /**
     * AdobeAuthSessionHelper is used to retrieve the current status and results of authentication
     * related operations.
     *
     * Example: The client Activity which is interested in the authentication status can use it in the
     * following way.
     *
     * Here the callback checks for a status change in Authentication workflow.
     * If the user is still logged in, the user is taken to the last viewed page.
     * */
    private AdobeAuthSessionHelper _authSessionHelper = null;
    private int mCurrentState;
    private SampleActionsAdapter adapter;
    private AdobeAuthSessionHelper.IAdobeAuthStatusCallback _statusCallback = new AdobeAuthSessionHelper.IAdobeAuthStatusCallback()
    {
        @Override
        public void call(AdobeAuthSessionHelper.AdobeAuthStatus status, AdobeAuthException exception)
        {
        if ( AdobeAuthSessionHelper.AdobeAuthStatus.AdobeAuthLoggedIn == status )
        {
            moveToScreen();
        }
        }
    };
    private Typeface adobeCleanLight;
    private Typeface adobeClean;
    private LinearLayout fullScreenLayout;
    private ImageView fullScreenImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        TextView launcherText= (TextView) findViewById(R.id.ic_launchHeaderText);
        launcherText.setTypeface(adobeCleanLight);
        _authSessionHelper = new AdobeAuthSessionHelper(_statusCallback);
        _authSessionHelper.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        _authSessionHelper.onStart();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        _authSessionHelper.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        _authSessionHelper.onPause();
    }
    //member variables

    @Override
    protected void onStop()
    {
        super.onStop();
        _authSessionHelper.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        _authSessionHelper.onDestroy();
    }

    private void showGridViewOfSelectedAssets(final Intent data)
    {
        mCurrentState = STATE_HOME;
        View appMainListView = findViewById(R.id.assetbrowsercontainer);
        appMainListView.setEnabled(true);

        View appSpinner = findViewById(R.id.sdk_sample_app_busy_spinner);
        appSpinner.setVisibility(appMainListView.GONE);

        Intent intent = new Intent(this, CreativeSDKSampleGridViewActivity.class);
        Bundle resultData = data.getExtras();
        intent.putExtras(resultData);
        startActivity(intent);
    }



    private void showGridViewOfSelectedAssetsAfterDelay(final Intent data)
    {
        View appMainListView = findViewById(R.id.assetbrowsercontainer);
        appMainListView.setEnabled(false);

        View appSpinner = findViewById(R.id.sdk_sample_app_busy_spinner);
        appSpinner.setVisibility(appMainListView.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showGridViewOfSelectedAssets(data);
            }
        }, 1500);

        mCurrentState = STATE_LAUNCH_GRID_FOR_SELECTED; //be in the home screen.
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        _authSessionHelper.onActivityResult(requestCode, resultCode, data);

        /* This block will be executed once files are selected in the Asset Browser and user clicks on "Open" Files button to open the selected files.
         * The Application gets a handle to the selected files which can be further used by the application.
        * The sample application created a Grid view of all the selected files from the Asset Browser.
        */
        if (requestCode == CREATIVE_SDK_SAMPLE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            showGridViewOfSelectedAssetsAfterDelay(data);
        }

        /*This block will be executed when back button is pressed from AssetBrowser Activity*/
        else if(requestCode == CREATIVE_SDK_SAMPLE_REQUEST_CODE && resultCode == RESULT_CANCELED){
            setCurrentState(STATE_HOME);
            moveToScreen();
        }


    }

    @Override
    public void onBackPressed() {
        switch (mCurrentState) {
            case STATE_ASSET_GRIDVIEW:
                setCurrentState(STATE_HOME);
                gotoHomeScreen();
                break;

            case STATE_ASSET_GRIDVIEW_IMAGE_DETAIL:
                setCurrentState(STATE_ASSET_GRIDVIEW);
                break;

            default:
                super.onBackPressed();
        }
    }

    /* init() initializes the Adobe Creative Cloud Authentication Manager with the CLient ID and Secret that is provided.
     * This initialization should be done before any authentication operations like Sign-in, Login or Logout.
    */
    private void init() {
        AdobeUXAuthManager sharedAuthManager = AdobeUXAuthManager.getSharedAuthManager();
        //Full screen layout for showing the image information
        fullScreenLayout = new LinearLayout(this);
        fullScreenLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        fullScreenLayout.setOrientation(LinearLayout.VERTICAL);
        fullScreenLayout.setPadding(IMAGE_DETAIL_PADDING, IMAGE_DETAIL_PADDING, IMAGE_DETAIL_PADDING, IMAGE_DETAIL_PADDING);

        fullScreenImageView = new ImageView(this);
        fullScreenImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        adapter = new SampleActionsAdapter(this);

        adobeClean = Typeface.createFromAsset(getAssets(), "fonts/AdobeClean-Regular.otf");
        adobeCleanLight = Typeface.createFromAsset(getAssets(), "fonts/AdobeClean-Light.otf");

        int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
        if(actionBarTitleView != null){
            actionBarTitleView.setTypeface(adobeCleanLight);
        }

        TextView header = (TextView) findViewById(R.id.ic_launchHeaderText);
        if(header != null) {
            header.setTypeface(adobeClean);
        }
        setCurrentState(STATE_HOME);
        moveToScreen();
    }

    /*
     * startAssetBrowser()
     * Check if the user is authenticated and then invoke the Asset Browser which lists all the files/folders that are present
     * in the users account on Adobe Creative Cloud
     */
    public void startAssetBrowser(View view) {
        //if we are in process of launching the grid view for selected items then prevent further launches.
        if(mCurrentState == STATE_LAUNCH_GRID_FOR_SELECTED)
            return;
        setCurrentState(STATE_ASSET_BROWSER);
        login();
    }

    /* sendMessage
     * This method check if the user is authenticated and then launches the Behance component from the Creative Cloud SDK.
     */

    public void sendMessage(View view) {

        if(mCurrentState == STATE_LAUNCH_GRID_FOR_SELECTED)
            return;

        setCurrentState(STATE_BEHANCE);
        login();

    }


    /**
     * Launch the app library, no need for authentication
     */


    public void startColorActivity(View view) {

        if(mCurrentState == STATE_LAUNCH_GRID_FOR_SELECTED)
            return;
        else
        {
            login();
        }
        setCurrentState(STATE_COLOR);

    }

    /**
     * Launch the app library, no need for authentication
     */
    public void startAppLibrary(View view) {
        AdobeAppLibraryLauncher.launchAppLibrarySelector(this);
    }

    public Typeface getDefaultTypeface() {
        return adobeCleanLight;
    }

    /* Login
     *
     * Method to login to creative cloud. The login call will invoke the Sign-in page from Creative Cloud. The error conditions are handled by the
     * Sign-in page.
     */
    private void login() {
        final AdobeUXAuthManager sharedAuthManager = AdobeUXAuthManager.getSharedAuthManager();
        sharedAuthManager.login(new AdobeAuthSessionLauncher.Builder().withActivity(this).withRequestCode(DEFAULT_LOGIN_ACTIVITY_REQUEST_CODE).build());
    }

    private void moveToScreen() {
        switch(mCurrentState) {
            case STATE_HOME:
                gotoHomeScreen();
                break;

            case STATE_ASSET_BROWSER:
                gotoAssetBrowserScreen();
                break;

            case STATE_BEHANCE:
                gotoBehanceScreen();
                break;

            case STATE_COLOR:
                gotoColorScreen();
                break;
            default:
                break;
        }
    }

    private void gotoColorScreen(){
        Intent intent = new Intent(this,CreativeSDKSampleColorActivity.class);
        startActivity(intent);
        finish();

    }

    private void gotoHomeScreen() {

        setContentView(R.layout.sampleassetbrowseractivity);

        ListView listView = (ListView) findViewById(R.id.ic_listview);
        listView.setAdapter(adapter);
    }

    /* gotoAssetBrowserScreen
     *
     * Method to launch the Asset Browser Component in the Creative SDK.
     * Ensure that the AdobeUXAssetOneUpViewerActivity and AdobeUXAssetBrowserActivity are added in your AndroidManifest.xml
     */
    private void gotoAssetBrowserScreen() {
        /**
         * Displays a Creative Cloud asset browser component for viewing and selecting Adobe Creative Cloud assets.
         * This API pops up the default File Browser as an Activity
         *
         * Filters and DataSource can be modified accordingly as shown in the commented example code below.
        **/
    	AdobeUXAssetBrowser sharedAssetBrowserInstance = AdobeUXAssetBrowser.getSharedInstance();
        AdobeUXAssetBrowserConfiguration browserConfiguration = new AdobeUXAssetBrowserConfiguration();
        browserConfiguration.options = EnumSet.of(AdobeUXAssetBrowserOption.ENABLE_MULTI_SELECT,AdobeUXAssetBrowserOption.SHOW_MULTI_SELECT_ON_POPUP,AdobeUXAssetBrowserOption.ENABLE_MYACCOUNT_OPTION);

        //browserConfiguration.designLibraryItemFilter = AdobeAssetDesignLibraryItemFilter.createFromDesignLibraryItems(EnumSet.of(AdobeAssetDesignLibraryItemType.AdobeAssetDesignLibraryItemColors,AdobeAssetDesignLibraryItemType.AdobeAssetDesignLibraryItemColorThemes) ,
          //          AdobeAssetDesignLibraryItemFilterType.ADOBE_ASSET_DESIGNLIBRARYITEM_FILTER_INCLUSION);
        //browserConfiguration.mimeTypeFilter = AdobeAssetMIMETypeFilter.createFromMimeTypes(EnumSet.of(AdobeAssetMimeTypes.MIMETYPE_JPEG) ,
        //AdobeAssetMIMETypeFilterType.ADOBE_ASSET_MIMETYPE_FILTERTYPE_INCLUSION);
        //browserConfiguration.dataSourceFilter = AdobeAssetDataSourceFilter.createFromDataSources(EnumSet.of(AdobeAssetDataSourceType.AdobeAssetDataSourceFiles),
        //        AdobeAssetDataSourceFilterType.ADOBE_ASSET_DATASOUCE_FILTER_INCLUSION);
        try {
            sharedAssetBrowserInstance.popupFileBrowser(this, CREATIVE_SDK_SAMPLE_REQUEST_CODE,browserConfiguration);
        } catch (AdobeCSDKException e) {
            e.printStackTrace();
        }
    }

    /* gotoBehanceScreen
     *
     * Method to launch the Behance Component in the Creative SDK.
     * Ensure that the BehanceSDKCreateProjectWFActivity or BehanceSDKCreateWIPWorkflowActivity are added in your AndroidManifest.xml
     */


    private void gotoBehanceScreen() {

        Intent intent = new Intent(this, CreativeSDKSampleBehanceActivity.class);
        setCurrentState(STATE_HOME);
        startActivity(intent);
        //it is needed otherwise even after pressing back, it will relaunch Behance


    }

    private void setCurrentState(int state) {
        this.mCurrentState = state;
    }
}

class SampleActionsAdapter extends BaseAdapter {
    private CreativeSDKSampleActivity context;

    public SampleActionsAdapter(CreativeSDKSampleActivity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            View view = inflater.inflate(R.layout.sampleactions, null);

            TextView assetLauncherText = (TextView) view.findViewById(R.id.ic_assetLauncherText);
            if(assetLauncherText != null) {
                assetLauncherText.setTypeface(context.getDefaultTypeface());
            }

            TextView behanceText = (TextView) view.findViewById(R.id.ic_behanceText);
            if(behanceText != null) {
                behanceText.setTypeface(context.getDefaultTypeface());
            }

            TextView appLibraryText = (TextView) view.findViewById(R.id.ic_appLibrary);
            if(appLibraryText != null) {
                appLibraryText.setTypeface(context.getDefaultTypeface());
            }


            TextView colorText = (TextView) view.findViewById(R.id.ic_Color);
            if(colorText != null) {
                colorText.setTypeface(context.getDefaultTypeface());
            }

            return view;
        }

        return convertView;
    }
}
