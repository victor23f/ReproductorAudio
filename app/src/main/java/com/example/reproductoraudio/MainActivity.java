package com.example.reproductoraudio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;
    int pause,currentProgress,songDuration;
    TextView segundosReproducidos,segundosTotales;
    ProgressBar progressBar;
    final Handler handler = new Handler();
    Runnable updateProgress=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progreso);
        segundosReproducidos = (TextView) findViewById(R.id.SegundosReproducidos);
        segundosTotales =(TextView) findViewById(R.id.segundosTotales);
    }

    public void play(View view) {

        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.falseneed);
            songDuration = mp.getDuration();
            segundosTotales.setText(milisegundosAsegundos(songDuration));
            mp.start();
            progresoCancion();
            Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();
        } else if (!mp.isPlaying()) {
            mp.seekTo(pause);
            progresoCancion();
            mp.start();
            Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
        }
    }

    public void pause(View view) {
        if (mp != null) {
            handler.removeCallbacks(updateProgress);
            mp.pause();
            pause = mp.getCurrentPosition();
            Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
        }
    }

    public void stop(View view) {
        if (mp != null)
            mp.stop();
        segundosTotales.setText("");
        segundosReproducidos.setText("");
        handler.removeCallbacks(updateProgress);
        progressBar.setProgress(0);
        mp = null;
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
    }

    public void botonBack(View view){
        if (mp!=null){
            if ((pause-10000)>= 0){
                mp.seekTo(pause-10000);
                mp.start();
                Toast.makeText(this, "-10sec", Toast.LENGTH_SHORT).show();
            }
            pause=mp.getCurrentPosition();
        }

    }
    public void botonForward(View view){
        if (mp!=null){
            if ((pause+10000)<= mp.getDuration()){
        mp.seekTo(pause+10000);
        mp.start();
        Toast.makeText(this, "+10sec", Toast.LENGTH_SHORT).show();
            }
            pause=mp.getCurrentPosition();
        }

    }

    public void progresoCancion() {

         updateProgress = new Runnable() {
            @Override
            public void run() {
                currentProgress = mp.getCurrentPosition();
                segundosReproducidos.setText(milisegundosAsegundos(currentProgress));
                int progress = (currentProgress * 100) / songDuration;
                progressBar.setProgress(progress);
               handler.postDelayed(this, 0);
            }
        };
        handler.postDelayed(updateProgress, 0);
    }
    public String milisegundosAsegundos(long milisegundos) {
        int segundos = (int) (milisegundos / 1000) % 60 ;
        int minutos = (int) ((milisegundos / (1000*60)) % 60);

        return String.format("%02d:%02d", minutos, segundos);
    }
}