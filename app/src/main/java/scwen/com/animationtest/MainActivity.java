package scwen.com.animationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoadingView load = findViewById(R.id.load);

        load.postDelayed(new Runnable() {
            @Override
            public void run() {
                load.loadEnd();
            }
        },1500);
    }

//    public void start(View view) {
//        AnimFrameworkActivity.start(this);
//    }
}
