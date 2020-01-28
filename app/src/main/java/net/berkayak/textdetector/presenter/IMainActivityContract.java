package net.berkayak.textdetector.presenter;

import android.content.Intent;

public interface IMainActivityContract {
    interface View{
        void initItems();
        String[] getKeyWords();
        void showResult(String[] result);
    }

    interface Presenter{
        void onCreate();
        void dispatchTakePictureIntent();
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
