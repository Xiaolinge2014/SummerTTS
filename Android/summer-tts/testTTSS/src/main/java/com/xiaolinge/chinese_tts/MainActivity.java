package com.xiaolinge.chinese_tts;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.xiaolinge.chinese_tts.utils.ChineseTTSUtils;
import com.xiaolinge.chinese_tts.utils.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {

    Context context;

    MediaPlayer mp;

    EditText inputText;

    Button ttsButton;

    String modelPath;
    String modelName = "multi_speakers.bin";
//    String modelName = "single_speaker_fast.bin";
//            = context.getFilesDir().getPath() + "/zh_tts/model/single_speaker_fast.bin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        modelPath = context.getFilesDir().getPath() + "/zh_tts/model/" + modelName;

        inputText = findViewById(R.id.input);
        ttsButton = findViewById(R.id.tts_button);
        mp = new MediaPlayer();
        ttsButton.setOnClickListener(v -> onClick());
//        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_AUDIO);
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 1);
//        }
        FileCopyUtils.copyDirFromAssets(context, "zh_tts/model", context.getFilesDir().getPath() + "/zh_tts/model");
//        FileCopyUtil.copyDirFromAssets(context,"zh_tts/exe/" + ABIS[0],context.getFilesDir().getPath() + "/zh_tts/exe/" + ABIS[0]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void writeFile(String text, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.write(text.getBytes(StandardCharsets.UTF_8));
            raf.close();
        } catch (IOException e) {
            Log.e("ChineseTTS", e.getMessage());
        }
    }

    private void tts() throws IOException {
        String text = inputText.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        String textPath = context.getFilesDir().getPath() + "/text.txt";
        modelPath = context.getFilesDir().getPath() + "/zh_tts/model/single_speaker_fast.bin";
        String wavPath = context.getFilesDir().getPath() + "/audio.wav";
        writeFile(text, textPath);

        ChineseTTSUtils.synthAndWrite(text, modelPath, wavPath);
    }

    private void play() throws IOException {
        String wavPath = context.getFilesDir().getPath() + "/audio.wav";
        mp.setLooping(false);
        mp.setDataSource(wavPath);
        mp.prepareAsync();
        mp.setOnPreparedListener(mediaPlayer -> mp.start());
        mp.setOnCompletionListener(mediaPlayer -> mp.reset());
    }

    private void onClick() {
        try {
            tts();
            play();
        } catch (IOException e) {
            Log.e("ChineseTTS", e.getMessage());
        }
    }
}
