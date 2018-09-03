package demo.play.com.ediucationdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private VoiceView voiceView;
    private TextView pathView;
    private boolean isPlay;
    private String url;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        findViewById(R.id.clickView).setOnClickListener(this);
        findViewById(R.id.videoView).setOnClickListener(this);
        findViewById(R.id.tagView).setOnClickListener(this);
        voiceView = findViewById(R.id.voiceView);
        pathView = findViewById(R.id.pathView);
        voiceView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TimeEvent timeEvent) {
        SharedPreferences sharePreferences = this.getSharedPreferences("sp_name_audio", MODE_PRIVATE);
        final String filePath = sharePreferences.getString("audio_path", "");
        long elpased = sharePreferences.getLong("elpased", 0);
        url = filePath;
        this.time = elpased;
        pathView.setText(TextUtils.isEmpty(url) ? "" : url);
        voiceView.setText((this.time/1000)+"");
        voiceView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlay = false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clickView:
                final RecordingDialogFragment fragment = new RecordingDialogFragment();
                fragment.show(getSupportFragmentManager(), RecordingDialogFragment.class.getSimpleName());
                fragment.setCancelable(false);
                break;
            case R.id.voiceView:
                if (!TextUtils.isEmpty(url)) {
                    if (isPlay) {
                        voiceView.stopPlaying();
                        isPlay = false;
                    } else {
                        voiceView.startPlaying(url, time);
                        isPlay = true;
                    }

                }
                break;
            case R.id.videoView:
                startActivity(new Intent(this,VideoActivity.class));
                break;
                case R.id.tagView:
                startActivity(new Intent(this,TabFragmentActivity.class));
                break;
        }
    }
}
