package net.berkayak.textdetector.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageRecognizerService {
    FirebaseVisionImage image;
    FirebaseVisionTextRecognizer detector;
    List<String> keyWords;
    IServiceRepoerter repoerter;

    Task<FirebaseVisionText> result;
    boolean status = false;

    private ImageRecognizerService(final Builder builder){
        this.image = FirebaseVisionImage.fromBitmap(builder.image);
        this.keyWords = builder.keyWords;
        this.repoerter = builder.repoerter;
        this.detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        doProcess();
    }

    private void doProcess(){
        result = detector.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        status = true;
                        repoerter.onCheckFinish(checkWords());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status = false;
                    }
                });
    }

    private String[] checkWords(){
        List<String> resultTexts = new ArrayList<>();
        for (FirebaseVisionText.TextBlock block: result.getResult().getTextBlocks()) {
            String blockText = block.getText();

            for (FirebaseVisionText.Line line: block.getLines()) {
                String lineText = line.getText();

                for (FirebaseVisionText.Element element: line.getElements()) {
                    String elementText = element.getText();

                    for (String key : keyWords){
                        if (key.toLowerCase().trim().equals(elementText.toLowerCase().trim()))
                            resultTexts.add(elementText);
                    }
                }
            }
        }

        return resultTexts.toArray(new String[resultTexts.size()]);
    }

    public static class Builder{
        Bitmap image;
        List<String> keyWords = new ArrayList<>();
        IServiceRepoerter repoerter;

        public Builder setImage(final Bitmap img){
            this.image = img;
            return this;
        }

        public Builder setKeyWord(final String keyWord){
            keyWords.add(keyWord);
            return this;
        }

        public Builder setKeyWords(final String[] keyWords){
            this.keyWords = Arrays.asList(keyWords);
            return this;
        }

        public Builder setRepoerter(IServiceRepoerter rp){
            this.repoerter = rp;
            return this;
        }

        public ImageRecognizerService create(){
            return new ImageRecognizerService(this);
        }

    }

    public interface IServiceRepoerter{
        void onCheckFinish(String[] rs);
    }
}
