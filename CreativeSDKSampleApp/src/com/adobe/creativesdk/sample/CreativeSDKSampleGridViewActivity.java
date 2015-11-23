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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.creativesdk.foundation.IAdobeGenericErrorCallback;
import com.adobe.creativesdk.foundation.adobeinternal.storage.library.AdobeAssetLibraryItemCharacterStyle;
import com.adobe.creativesdk.foundation.adobeinternal.storage.library.AdobeAssetLibraryItemLayoutStyle;
import com.adobe.creativesdk.foundation.internal.analytics.AdobeAnalyticsSDKReporter;
import com.adobe.creativesdk.foundation.internal.storage.AdobePhotoAssetsDataSource;
import com.adobe.creativesdk.foundation.internal.storage.asset.AdobeAssetCompPage;
import com.adobe.creativesdk.foundation.internal.storage.asset.AdobeAssetDrawPage;
import com.adobe.creativesdk.foundation.internal.storage.asset.AdobeAssetLinePage;
import com.adobe.creativesdk.foundation.internal.storage.asset.AdobeAssetPSMixPage;
import com.adobe.creativesdk.foundation.internal.storage.asset.AdobeAssetSketchbookPage;
import com.adobe.creativesdk.foundation.internal.utils.AdobeCSDKActionBarController;
import com.adobe.creativesdk.foundation.internal.utils.AdobeCSDKException;
import com.adobe.creativesdk.foundation.storage.AdobeAsset;
import com.adobe.creativesdk.foundation.storage.AdobeAssetCompFile;
import com.adobe.creativesdk.foundation.storage.AdobeAssetDrawFile;
import com.adobe.creativesdk.foundation.storage.AdobeAssetException;
import com.adobe.creativesdk.foundation.storage.AdobeAssetFile;
import com.adobe.creativesdk.foundation.storage.AdobeAssetFileRenditionType;
import com.adobe.creativesdk.foundation.storage.AdobeAssetImageDimensions;
import com.adobe.creativesdk.foundation.storage.AdobeAssetLibrary;
import com.adobe.creativesdk.foundation.storage.AdobeAssetLibraryItem;
import com.adobe.creativesdk.foundation.storage.AdobeAssetLibraryItemBrush;
import com.adobe.creativesdk.foundation.storage.AdobeAssetLibraryItemColor;
import com.adobe.creativesdk.foundation.storage.AdobeAssetLibraryItemColorTheme;
import com.adobe.creativesdk.foundation.storage.AdobeAssetLibraryItemImage;
import com.adobe.creativesdk.foundation.storage.AdobeAssetLineFile;
import com.adobe.creativesdk.foundation.storage.AdobeAssetPSMixFile;
import com.adobe.creativesdk.foundation.storage.AdobeAssetPackagePages;
import com.adobe.creativesdk.foundation.storage.AdobeAssetSketchbook;
import com.adobe.creativesdk.foundation.storage.AdobePhotoAsset;
import com.adobe.creativesdk.foundation.storage.AdobePhotoException;
import com.adobe.creativesdk.foundation.storage.AdobeSelection;
import com.adobe.creativesdk.foundation.storage.AdobeSelectionAsset;
import com.adobe.creativesdk.foundation.storage.AdobeSelectionCompFile;
import com.adobe.creativesdk.foundation.storage.AdobeSelectionDrawAsset;
import com.adobe.creativesdk.foundation.storage.AdobeSelectionLibraryAsset;
import com.adobe.creativesdk.foundation.storage.AdobeSelectionLineAsset;
import com.adobe.creativesdk.foundation.storage.AdobeSelectionPSMixFile;
import com.adobe.creativesdk.foundation.storage.AdobeSelectionPhotoAsset;
import com.adobe.creativesdk.foundation.storage.AdobeSelectionSketchAsset;
import com.adobe.creativesdk.foundation.storage.AdobeUXAssetBrowser;
import com.adobe.creativesdk.foundation.storage.IAdobeGenericRequestCallback;
import com.adobe.creativesdk.foundation.storage.IAdobeRequestCompletionCallback;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rkumarba on 08-12-2014.
 */
public class
        CreativeSDKSampleGridViewActivity extends ActionBarActivity {
    private static final int RENDITION_MAX_WIDTH = 256;
    private static final int RENDITION_MAX_HEIGHT = 256;
    GridView gridview;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_grid_view);


        Toolbar toolbar = (Toolbar) findViewById(com.adobe.creativesdk.foundation.assetux.R.id.actionbar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        AdobeCSDKActionBarController.setTitle(findViewById(android.R.id.content), getString(R.string.selection_header));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);

        gridview = (GridView) findViewById(R.id.gridview);

        gridview.setAdapter(new CreativeSDKSampleGridViewAdapter(this));
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
    public class CreativeSDKSampleGridViewAdapter extends BaseAdapter {
        private Activity mContext;
        private AdobeUXAssetBrowser.ResultProvider mAssetBrowserResult;
        private ArrayList<AdobeSelection> mSelectedAssetsList;
        public CreativeSDKSampleGridViewAdapter(Activity c) {
            mContext = c;
            mAssetBrowserResult = new AdobeUXAssetBrowser.ResultProvider(mContext.getIntent());
            mSelectedAssetsList = mAssetBrowserResult.getSelectionAssetArray();
        }
        private void attachPhoto(final AdobePhotoAsset asset, final ImageView imageView) {
            //TODO: get rendition for photo
            IAdobeGenericRequestCallback<byte[], AdobePhotoException> downloadCallback = new IAdobeGenericRequestCallback<byte[], AdobePhotoException>() {
                @Override
                public void onCancellation() {
                }
                @Override
                public void onCompletion(byte[] data) {
                    InputStream inData = new ByteArrayInputStream(data);
                    Bitmap image = BitmapFactory.decodeStream(inData);
                    imageView.setImageBitmap(image);
                }

                @Override
                public void onError(AdobePhotoException error) {
                    imageView.setImageResource(R.drawable.no_preview_9patch);
                }

                @Override
                public void onProgress(double progress) {
                }
            };
            AdobePhotoAssetsDataSource.getRenditionForAsset(asset, downloadCallback);
        }
        private void attachImage(AdobeAsset asset, final ImageView imageView) {
            if (asset instanceof AdobeAssetFile) {
                final AdobeAssetFile aFile = (AdobeAssetFile) asset;
                Log.d("AtachImage","Calling AtachImage>>>>>>>>>>>>>>>>>>>>>>>");
            aFile.getRenditionWithType(AdobeAssetFileRenditionType.ADOBE_ASSET_FILE_RENDITION_TYPE_PNG,
                    new AdobeAssetImageDimensions(RENDITION_MAX_WIDTH /*/ level*/, RENDITION_MAX_HEIGHT /*/ level*/),
                    new IAdobeGenericRequestCallback<byte[], AdobeAssetException>() {
                        @Override
                        public void onCompletion(byte[] data) {
                            InputStream inData = new ByteArrayInputStream(data);
                            Bitmap image = BitmapFactory.decodeStream(inData);
                            imageView.setImageBitmap(image);
                            Log.d("AtachImage", "COMPLETED getRenditionWithType>>>>>>>>>>>>>>>>>>>>>>>");
                        }

                        @Override
                        public void onCancellation() {
                        }

                        @Override
                        public void onError(AdobeAssetException error) {
                            imageView.setImageResource(R.drawable.no_preview_9patch);
                            Log.d("ERROR", "ERROR getRenditionWithType>>>>>>>>>>>>>>>>>>>>>>>");
                        }

                        @Override
                        public void onProgress(double progress) {
                        }
                    });
            }
        }

        //Dummy function not being used but kept for reference
       /* private void attachLibrary(AdobeAsset asset, final ImageView imageView){


            final AdobeAssetLibrary library = (AdobeAssetLibrary)asset;

            AdobeAssetLibraryItem item = null;
            AdobeAssetFile file = null;

            // a comparison arbitrarily using the itemID, so we get a deterministic
            // ordering of the library elements. The mod and created dates appear
            // to be null for these items.
            Comparator nameOrIdComparator = new Comparator() {

                @Override
                public int compare(Object obj1, Object obj2) {
                    String id1 = ((AdobeAssetLibraryItem) obj1).nameOrIdForDisplay();
                    String id2 = ((AdobeAssetLibraryItem) obj2).nameOrIdForDisplay();
                    return id1.compareToIgnoreCase(id2);
                }
            };

            // the colors are special, since the name is actually the hex value
            Comparator colorComparator = new Comparator() {

                @Override
                public int compare(Object obj1, Object obj2) {
                    String id1 = ((AdobeAssetLibraryItemColor) obj1).colorNameOrHexValue();
                    String id2 = ((AdobeAssetLibraryItemColor) obj2).colorNameOrHexValue();
                    return id1.compareToIgnoreCase(id2);
                }
            };

            //Iterator<String> keys;

            //image items
            ArrayList<AdobeAssetLibraryItemImage> imageItemsArray = new ArrayList<>(library.getImages().values());

            if(imageItemsArray.size() >0){
                Collections.sort(imageItemsArray, nameOrIdComparator);
                item = (AdobeAssetLibraryItem) imageItemsArray.get(0);
                if(item instanceof AdobeAssetLibraryItemImage){
                    AdobeAssetLibraryItemImage imageItem = (AdobeAssetLibraryItemImage)item;
                    if(imageItem.getPrimaryComponentType().equals("image/vnd.adobe.shape+svg")){
                        file = imageItem.getRendition();
                    }
                    else{
                        file = imageItem.getImage();
                    }
                }
            }
            else{
                //if no images, search for color themes
                ArrayList<AdobeAssetLibraryItemColorTheme> colorThemeArray = new ArrayList<>(library.getColorThemes().values());

                if(colorThemeArray.size() > 0){
                    Collections.sort(colorThemeArray, nameOrIdComparator);
                    AdobeAssetLibraryItemColorTheme colorTheme = colorThemeArray.get(0);
                    //collectionCellViewHolder.setColorTheme(colorTheme.getRGBColors());
                    return;
                }
                else
                {
                    //if no color themes, search for color
                    ArrayList<AdobeAssetLibraryItemColor> colorArray = new ArrayList<>(library.getColors().values());

                    if(colorArray.size() > 0) {
                        Collections.sort(colorArray, colorComparator);
                        AdobeAssetLibraryItemColor color = colorArray.get(0);
                        //collectionCellViewHolder.setColor(color.color());
                        return;
                    }
                    else{
                        //if no color, search for brushes
                        ArrayList<AdobeAssetLibraryItemBrush> brushArray = new ArrayList<>(library.getBrushes().values());

                        if(brushArray.size() > 0) {
                            Collections.sort(brushArray, nameOrIdComparator);
                            item = (AdobeAssetLibraryItem) brushArray.get(0);
                            file = ((AdobeAssetLibraryItemBrush) item).getRendition();
                        }
                        else{
                            //if no brushes, search for characterStyles
                            ArrayList<AdobeAssetLibraryItemCharacterStyle> charStylesArray = new ArrayList<>(library.getCharacterStyles().values());

                            if(charStylesArray.size() > 0) {
                                Collections.sort(charStylesArray, nameOrIdComparator);
                                item = (AdobeAssetLibraryItem) charStylesArray.get(0);
                                file = ((AdobeAssetLibraryItemCharacterStyle) item).getRendition();
                            }
                            else{
                                //if no characterStyles, search for layout styles
                                ArrayList<AdobeAssetLibraryItemLayoutStyle> layoutStylesArray = new ArrayList<>( library.getLayoutStyles().values());

                                if(layoutStylesArray.size() > 0) {
                                    Collections.sort(layoutStylesArray, nameOrIdComparator);
                                    item = (AdobeAssetLibraryItem) layoutStylesArray.get(0);
                                    file = ((AdobeAssetLibraryItemLayoutStyle) item).getRendition();
                                }
                            }
                        }
                    }
                }
            }
        } */
        private void attachLibraryItem(AdobeSelectionLibraryAsset librarySelection, final ImageView imageView){
            AdobeAssetFile file = null;
            // the colors are special, since the name is actually the hex value
            Comparator colorComparator = new Comparator() {

                @Override
                public int compare(Object obj1, Object obj2) {
                    String id1 = ((AdobeAssetLibraryItemColor) obj1).colorNameOrHexValue();
                    String id2 = ((AdobeAssetLibraryItemColor) obj2).colorNameOrHexValue();
                    return id1.compareToIgnoreCase(id2);
                }
            };

           if(librarySelection.getSelectedColorIDs() != null)
           {
               final AdobeAssetLibrary library = (AdobeAssetLibrary)librarySelection.getSelectedItem();
               AdobeAssetLibraryItemColor color = library.getColors().get(librarySelection.getSelectedColorIDs().get(0));
               imageView.setBackgroundColor(color.color());
               return;

           }
            else if(librarySelection.getSelectedColorThemeIDs() != null)
           {

               final AdobeAssetLibrary library = (AdobeAssetLibrary)librarySelection.getSelectedItem();
              // LinearLayout ly = (LinearLayout) findViewById(R.id.colorThemeView);
              /* LinearLayout cv1 = (LinearLayout) findViewById(R.id.colorThemeView01);
               LinearLayout cv2 = (LinearLayout) findViewById(R.id.colorThemeView02);
               LinearLayout cv3 = (LinearLayout) findViewById(R.id.colorThemeView03);
               LinearLayout cv4 = (LinearLayout) findViewById(R.id.colorThemeView04);
               LinearLayout cv5 = (LinearLayout) findViewById(R.id.colorThemeView05);*/

               ArrayList<Integer> myColors = new ArrayList<Integer>();
               AdobeAssetLibraryItemColorTheme colorTheme = library.getColorThemes().get(librarySelection.getSelectedColorThemeIDs().get(0));
               ArrayList<Integer> colorArrayList = colorTheme.getRGBColors();
               imageView.setBackgroundColor(colorArrayList.get(0));
              /* cv1.setBackgroundColor(colorArrayList.get(0));
               cv1.setBackgroundColor(colorArrayList.get(1));
               cv1.setBackgroundColor(colorArrayList.get(2));
               cv1.setBackgroundColor(colorArrayList.get(3));
               cv1.setBackgroundColor(colorArrayList.get(4));*/



               return;

           }
           else if(librarySelection.getSelectedImageIDs() != null)
           {
               final AdobeAssetLibrary library = (AdobeAssetLibrary)librarySelection.getSelectedItem();
               AdobeAssetLibraryItemImage imageItem = library.getImages().get(librarySelection.getSelectedImageIDs().get(0));
               if(imageItem.getPrimaryComponentType().equals("image/vnd.adobe.shape+svg")){
                   file = imageItem.getRendition();
               }
               else{
                   file = imageItem.getImage();
               }
           }

            if (file != null){
                file.getRenditionWithType(AdobeAssetFileRenditionType.ADOBE_ASSET_FILE_RENDITION_TYPE_PNG,
                        new AdobeAssetImageDimensions(RENDITION_MAX_WIDTH /*/ level*/, RENDITION_MAX_HEIGHT /*/ level*/), new IAdobeGenericRequestCallback<byte[], AdobeAssetException>() {
                            @Override
                            public void onCancellation() {

                            }

                            @Override
                            public void onCompletion(byte[] requestResult) {
                                setImageViewBitmap(imageView, requestResult);
                            }

                            @Override
                            public void onError(AdobeAssetException error) {

                            }

                            @Override
                            public void onProgress(double progress) {

                            }
                        });
            }
        }
        private void attachLine(AdobeSelectionLineAsset asset, final ImageView imageView){

            final int index = asset.getSelectedPageIndex();

                final AdobeAssetLineFile lineFile = (AdobeAssetLineFile) asset.getSelectedItem();
                lineFile.loadMetadata(new IAdobeRequestCompletionCallback() {
                    @Override
                    public void onCompletion() {
                        TestCase.assertTrue(lineFile.getPages().size() > 0);
                        AdobeAssetLinePage assetLinePage =null;
                        int pageCounter = index;
                        //for(int pageCounter =0; pageCounter<lineFile.getPages().size();pageCounter++) {
                        assetLinePage = (AdobeAssetLinePage) lineFile.getPages().get(pageCounter);
                        TestCase.assertEquals(pageCounter, assetLinePage.pageNumber);
                        AdobeAssetPackagePages packagePages = assetLinePage._package;
                        TestCase.assertEquals(lineFile.getPages(), packagePages.getPages());
                        assetLinePage.getRenditionWithType(AdobeAssetFileRenditionType.ADOBE_ASSET_FILE_RENDITION_TYPE_PNG,new AdobeAssetImageDimensions(90,90),new IAdobeGenericRequestCallback<byte[], AdobeAssetException>() {
                            @Override
                            public void onCancellation() {

                            }

                            @Override
                            public void onCompletion(byte[] requestResult) {
                                InputStream inData = new ByteArrayInputStream(requestResult);
                                Bitmap image = BitmapFactory.decodeStream(inData);
                                imageView.setImageBitmap(image);
                            }

                            @Override
                            public void onError(AdobeAssetException error) {
                                TestCase.fail("GetRendition Error. Error:" + error.getDescription());
                            }

                            @Override
                            public void onProgress(double progress) {

                            }
                        });
                        //}

                    }
                }, new IAdobeGenericErrorCallback<AdobeCSDKException>() {
                    @Override
                    public void onError(AdobeCSDKException error) {
                        TestCase.fail("loadMetadata Error Block :" + error.getDescription());
                    }
                });

        }
        private void attachDraw(AdobeSelectionDrawAsset asset, final ImageView imageView){

            final int index = asset.getSelectedPageIndex();

            final AdobeAssetDrawFile drawFile = (AdobeAssetDrawFile) asset.getSelectedItem();

                drawFile.loadMetadata(new IAdobeRequestCompletionCallback() {
                    @Override
                    public void onCompletion() {
                        TestCase.assertTrue(drawFile.getPages().size() > 0);
                        AdobeAssetDrawPage assetDrawPage = null;
                        int pageCounter = index;
                        //for(int pageCounter =0; pageCounter<lineFile.getPages().size();pageCounter++) {
                        assetDrawPage = (AdobeAssetDrawPage) drawFile.getPages().get(pageCounter);
                        TestCase.assertEquals(pageCounter, assetDrawPage.pageNumber);
                        AdobeAssetPackagePages packagePages = assetDrawPage._package;
                        TestCase.assertEquals(drawFile.getPages(), packagePages.getPages());
                        assetDrawPage.getRenditionWithType(AdobeAssetFileRenditionType.ADOBE_ASSET_FILE_RENDITION_TYPE_PNG, new AdobeAssetImageDimensions(90, 90), new IAdobeGenericRequestCallback<byte[], AdobeAssetException>() {
                            @Override
                            public void onCancellation() {

                            }

                            @Override
                            public void onCompletion(byte[] requestResult) {
                                InputStream inData = new ByteArrayInputStream(requestResult);
                                Bitmap image = BitmapFactory.decodeStream(inData);
                                imageView.setImageBitmap(image);
                            }

                            @Override
                            public void onError(AdobeAssetException error) {
                                TestCase.fail("GetRendition Error. Error:" + error.getDescription());
                            }

                            @Override
                            public void onProgress(double progress) {

                            }
                        });
                        //}

                    }
                }, new IAdobeGenericErrorCallback<AdobeCSDKException>() {
                    @Override
                    public void onError(AdobeCSDKException error) {
                        TestCase.fail("loadMetadata Error Block :" + error.getDescription());
                    }
                });
        }
        private void attachSketch(AdobeSelectionSketchAsset asset, final ImageView imageView){

            final int index = asset.getSelectedPageIndex();

            final AdobeAssetSketchbook sketchBook = (AdobeAssetSketchbook) asset.getSelectedItem();


               sketchBook.loadMetadata(new IAdobeRequestCompletionCallback() {
                    @Override
                    public void onCompletion() {
                        TestCase.assertTrue(sketchBook.getPages().size() > 0);
                        AdobeAssetSketchbookPage assetSketchbookPage = null;
                        int pageCounter = index;
                        //for(int pageCounter =0; pageCounter<lineFile.getPages().size();pageCounter++) {
                        assetSketchbookPage = (AdobeAssetSketchbookPage) sketchBook.getPages().get(pageCounter);
                        TestCase.assertEquals(pageCounter, assetSketchbookPage.pageNumber);
                        AdobeAssetPackagePages packagePages = assetSketchbookPage._package;
                        TestCase.assertEquals(sketchBook.getPages(), packagePages.getPages());
                        assetSketchbookPage.getRenditionWithType(AdobeAssetFileRenditionType.ADOBE_ASSET_FILE_RENDITION_TYPE_PNG, new AdobeAssetImageDimensions(90, 90), new IAdobeGenericRequestCallback<byte[], AdobeAssetException>() {
                            @Override
                            public void onCancellation() {

                            }

                            @Override
                            public void onCompletion(byte[] requestResult) {
                                InputStream inData = new ByteArrayInputStream(requestResult);
                                Bitmap image = BitmapFactory.decodeStream(inData);
                                imageView.setImageBitmap(image);
                            }

                            @Override
                            public void onError(AdobeAssetException error) {
                                TestCase.fail("GetRendition Error. Error:" + error.getDescription());
                            }

                            @Override
                            public void onProgress(double progress) {

                            }
                        });
                        //}

                    }
                }, new IAdobeGenericErrorCallback<AdobeCSDKException>() {
                    @Override
                    public void onError(AdobeCSDKException error) {
                        TestCase.fail("loadMetadata Error Block :" + error.getDescription());
                    }
                });
        }
        private void attachComp(AdobeSelectionCompFile asset, final ImageView imageView){

            final int index = asset.getSelectedPageIndex();

            final AdobeAssetCompFile compFile = (AdobeAssetCompFile) asset.getSelectedItem();


            compFile.loadMetadata(new IAdobeRequestCompletionCallback() {
                @Override
                public void onCompletion() {
                    TestCase.assertTrue(compFile.getPages().size() > 0);
                    AdobeAssetCompPage assetCompPage = null;
                    int pageCounter = index;
                    //for(int pageCounter =0; pageCounter<lineFile.getPages().size();pageCounter++) {
                    assetCompPage = (AdobeAssetCompPage) compFile.getPages().get(pageCounter);

                    AdobeAssetPackagePages packagePages = assetCompPage._package;

                    assetCompPage.getRenditionWithType(AdobeAssetFileRenditionType.ADOBE_ASSET_FILE_RENDITION_TYPE_PNG, new AdobeAssetImageDimensions(90, 90), new IAdobeGenericRequestCallback<byte[], AdobeAssetException>() {
                        @Override
                        public void onCancellation() {

                        }

                        @Override
                        public void onCompletion(byte[] requestResult) {
                            InputStream inData = new ByteArrayInputStream(requestResult);
                            Bitmap image = BitmapFactory.decodeStream(inData);
                            imageView.setImageBitmap(image);
                        }

                        @Override
                        public void onError(AdobeAssetException error) {
                            TestCase.fail("GetRendition Error. Error:" + error.getDescription());
                        }

                        @Override
                        public void onProgress(double progress) {

                        }
                    });
                    //}

                }
            }, new IAdobeGenericErrorCallback<AdobeCSDKException>() {
                @Override
                public void onError(AdobeCSDKException error) {
                    TestCase.fail("loadMetadata Error Block :" + error.getDescription());
                }
            });
        }

        private void attachMix(AdobeSelectionPSMixFile asset, final ImageView imageView){

            final int index = asset.getSelectedPageIndex();

            final AdobeAssetPSMixFile mixFile = (AdobeAssetPSMixFile) asset.getSelectedItem();


            mixFile.loadMetadata(new IAdobeRequestCompletionCallback() {
                @Override
                public void onCompletion() {
                    TestCase.assertTrue(mixFile.getPages().size() > 0);
                    AdobeAssetPSMixPage mixPage = null;
                    int pageCounter = index;
                    //for(int pageCounter =0; pageCounter<lineFile.getPages().size();pageCounter++) {
                    mixPage = (AdobeAssetPSMixPage) mixFile.getPages().get(pageCounter);
                  //  TestCase.assertEquals(pageCounter, mixPage.pageNumber);
                    AdobeAssetPackagePages packagePages = mixPage._package;
                  //  TestCase.assertEquals(mixFile.getPages(), packagePages.getPages());
                    mixPage.getRenditionWithType(AdobeAssetFileRenditionType.ADOBE_ASSET_FILE_RENDITION_TYPE_PNG, new AdobeAssetImageDimensions(90, 90), new IAdobeGenericRequestCallback<byte[], AdobeAssetException>() {
                        @Override
                        public void onCancellation() {

                        }

                        @Override
                        public void onCompletion(byte[] requestResult) {
                            InputStream inData = new ByteArrayInputStream(requestResult);
                            Bitmap image = BitmapFactory.decodeStream(inData);
                            imageView.setImageBitmap(image);
                        }

                        @Override
                        public void onError(AdobeAssetException error) {
                            TestCase.fail("GetRendition Error. Error:" + error.getDescription());
                        }

                        @Override
                        public void onProgress(double progress) {

                        }
                    });
                    //}

                }
            }, new IAdobeGenericErrorCallback<AdobeCSDKException>() {
                @Override
                public void onError(AdobeCSDKException error) {
                    TestCase.fail("loadMetadata Error Block :" + error.getDescription());
                }
            });
        }
        public int getCount() {
            return mSelectedAssetsList != null ? mSelectedAssetsList.size() : 0;
        }
        public Object getItem(int position) {
            return null;
        }
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public boolean areAllItemsEnabled()
        {
            return false;
        }
        @Override
        public boolean isEnabled(int position)
        {
            return false;
        }
        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new GridViewItem(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                imageView = (ImageView) convertView;
            }

            bindViewToAsset(imageView,position);

            return imageView;
        }
        private void bindViewToAsset(ImageView imageView,int position){

            AdobeSelection selectionObj = mSelectedAssetsList.get(position);
            if(selectionObj instanceof AdobeSelectionAsset)
                attachImage(((AdobeSelectionAsset) selectionObj).getSelectedItem(), imageView);
            else if(selectionObj instanceof AdobeSelectionPhotoAsset)
                attachPhoto(((AdobeSelectionPhotoAsset) selectionObj).getSelectedItem(), imageView);
            else if (selectionObj instanceof AdobeSelectionLibraryAsset)
                attachLibraryItem((AdobeSelectionLibraryAsset) selectionObj, imageView);
            else if (selectionObj instanceof AdobeSelectionDrawAsset)
                attachDraw((AdobeSelectionDrawAsset) selectionObj, imageView);
            else if (selectionObj instanceof AdobeSelectionLineAsset)
                attachLine((AdobeSelectionLineAsset) selectionObj, imageView);
            else if (selectionObj instanceof AdobeSelectionSketchAsset)
                attachSketch((AdobeSelectionSketchAsset) selectionObj, imageView);
            else if (selectionObj instanceof AdobeSelectionCompFile)
                attachComp((AdobeSelectionCompFile) selectionObj, imageView);
            else if (selectionObj instanceof AdobeSelectionPSMixFile)
                attachMix((AdobeSelectionPSMixFile) selectionObj, imageView);

        }
        public class GridViewItem extends ImageView {
            public GridViewItem(Context context) {
                super(context);
            }
            public GridViewItem(Context context, AttributeSet attrs) {
                super(context, attrs);
            }
            public GridViewItem(Context context, AttributeSet attrs, int defStyle) {
                super(context, attrs, defStyle);
            }
            @Override
            public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
            }

        }

    }
    public void setImageViewBitmap(ImageView imageView, byte[] data){
        InputStream inData = new ByteArrayInputStream(data);
        Bitmap image = BitmapFactory.decodeStream(inData);
        imageView.setImageBitmap(image);
    }

}
