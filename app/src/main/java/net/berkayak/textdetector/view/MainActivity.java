package net.berkayak.textdetector.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.berkayak.textdetector.R;
import net.berkayak.textdetector.presenter.IMainActivityContract;
import net.berkayak.textdetector.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements IMainActivityContract.View {

    Button mTakePictureBtn;
    EditText mSearchWordsET;
    TextView mResultTV;
    MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainActivityPresenter(this, this);
        mPresenter.onCreate();
    }

    @Override
    public void initItems() {
        mTakePictureBtn = findViewById(R.id.take_picture_Btn);
        mTakePictureBtn.setOnClickListener(takePictureListener);
        mSearchWordsET = findViewById(R.id.search_word_ET);
        mResultTV = findViewById(R.id.result_TV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String[] getKeyWords(){
        if (!mSearchWordsET.getText().toString().equals(""))
            return mSearchWordsET.getText().toString().split(",");
        else {
            Toast.makeText(this, R.string.fill_searh_words, Toast.LENGTH_LONG).show();
            return new String[0];
        }
    }

    @Override
    public void showResult(String[] result) {
        StringBuilder sb = new StringBuilder();
        for (String s:result){
            sb.append(s + "\n");
        }
        mResultTV.setText(sb.toString());
    }

    //TAKE PICTURE BUTTON LISTENER
    public View.OnClickListener takePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPresenter.dispatchTakePictureIntent();
        }
    };
}
