package net.berkayak.textdetector.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import net.berkayak.textdetector.R;
import net.berkayak.textdetector.presenter.MainActivityPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@LargeTest
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityTestRule.getActivity();
    }

    @Test
    public void myTest(){
        onView(withId(R.id.search_word_ET)).perform(typeText("deneme, berkay, gillete"));

        Intent i = new Intent();
        Bundle bnd = new Bundle();
        bnd.putParcelable("data", getImage());
        i.putExtras(bnd);
        mainActivity.onActivityResult(MainActivityPresenter.REQUEST_IMAGE_CAPTURE, Activity.RESULT_OK, i);
    }

    private Bitmap getImage(){

        AssetManager assetManager = mainActivity.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open("mySample.png");
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (Exception e) {
            // handle exception
        }

        return bitmap;
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}