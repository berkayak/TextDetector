package net.berkayak.textdetector.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import net.berkayak.textdetector.model.ImageRecognizerService;

import java.util.ArrayList;

public class MainActivityPresenter implements IMainActivityContract.Presenter {
    IMainActivityContract.View mViewPusher;
    Activity mContext;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public MainActivityPresenter(IMainActivityContract.View vPusher, Activity context){
        this.mViewPusher = vPusher;
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        mViewPusher.initItems();
    }

    public void onCapture(Bitmap bm) {
        String[] words = mViewPusher.getKeyWords();
        if (bm == null || words.length <= 0){
            return;
        }

        ImageRecognizerService service = new ImageRecognizerService.Builder()
                .setImage(bm)
                .setRepoerter(sr)
                .setKeyWords(words)
                .create();
    }

    @Override
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            onCapture(imageBitmap);



        }
    }

    private ImageRecognizerService.IServiceRepoerter sr = new ImageRecognizerService.IServiceRepoerter() {
        @Override
        public void onCheckFinish(String[] rs) {
            Log.i("REGS", String.valueOf(rs.length));
            mViewPusher.showResult(rs);
        }
    };
}
