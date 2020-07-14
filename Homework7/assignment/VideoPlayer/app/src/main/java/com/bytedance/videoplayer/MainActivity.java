package com.bytedance.videoplayer;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.bytedance.videoplayer.player.IjkVideoPlayer;
import com.bytedance.videoplayer.player.VideoPlayerListener;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {

  private IjkVideoPlayer ijkPlayer;
  private TextView tvCurrentTime;
  private TextView tvTotalTime;
  private SeekBar seekBarVideo;
  private Button play;
  private Button pause;
  private TextView speed;

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    ActionBar supportActionBar = MainActivity.this.getSupportActionBar();
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      supportActionBar.hide();
      LayoutParams layoutParams = ijkPlayer.getLayoutParams();
      layoutParams.width = LayoutParams.MATCH_PARENT;
      layoutParams.height = LayoutParams.MATCH_PARENT;
      ijkPlayer.setLayoutParams(layoutParams);
      setFullScreen(true);
    } else {
      supportActionBar.show();
      LayoutParams layoutParams = ijkPlayer.getLayoutParams();
      layoutParams.width = LayoutParams.MATCH_PARENT;
      layoutParams.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
          240,
          getResources().getDisplayMetrics()));
      ijkPlayer.setLayoutParams(layoutParams);
      setFullScreen(false);
    }
  }

  private void setFullScreen(boolean fullScreen) {
    //隐藏最上面的时间显示和电池显示
    WindowManager.LayoutParams attrs = getWindow().getAttributes();
    if (fullScreen) {
      attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
      getWindow().setAttributes(attrs);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    } else {
      attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
      getWindow().setAttributes(attrs);
      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("ijkPlayer");
    ijkPlayer = findViewById(R.id.ijkPlayer);
    play = findViewById(R.id.buttonPlay);
    pause = findViewById(R.id.buttonPause);
    speed = findViewById(R.id.textSpeed);
    tvCurrentTime = findViewById(R.id.tvCurrentTime);
    tvTotalTime = findViewById(R.id.tvTotalTime);
    seekBarVideo = findViewById(R.id.seekBarVideo);
    handler.postDelayed(runnable, 3000); // 3秒后seekBar消失
    seekBarVideo.setMax(100000);
    seekBarVideo.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateTextViewWithTimeFormat(tvCurrentTime,
            (ijkPlayer.getDuration() / 1000) * progress / seekBar.getMax());
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacksAndMessages(null);
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        ijkPlayer.seekTo(seekBar.getProgress() * ijkPlayer.getDuration() / seekBar.getMax());
        handler.sendEmptyMessageDelayed(0, 200);
        handler.postDelayed(runnable, 3000); // 3秒后seekBar消失
      }
    });
    handler.sendEmptyMessageDelayed(0, 200);
    //加载native库
    try {
      IjkMediaPlayer.loadLibrariesOnce(null);
      IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    } catch (Exception e) {
      this.finish();
    }
    ijkPlayer.setListener(new VideoPlayerListener());
    ijkPlayer.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (seekBarVideo.getVisibility() == View.VISIBLE) {
          seekBarVideo.setVisibility(View.INVISIBLE);
          tvCurrentTime.setVisibility(View.INVISIBLE);
          tvTotalTime.setVisibility(View.INVISIBLE);
        } else {
          seekBarVideo.setVisibility(View.VISIBLE);
          tvCurrentTime.setVisibility(View.VISIBLE);
          tvTotalTime.setVisibility(View.VISIBLE);
          handler.postDelayed(runnable, 3000); // 3秒后seekBar消失
        }
      }
    });
    ijkPlayer.setVideoResource(R.raw.bytedance);
    findViewById(R.id.buttonPlay).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ijkPlayer.start();
      }
    });

    findViewById(R.id.buttonPause).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ijkPlayer.pause();
      }
    });

    findViewById(R.id.x0_5).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ijkPlayer.setSpeed(0.5f);
        speed.setText("SPEED: 0.5x");
      }
    });
    findViewById(R.id.x1_0).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ijkPlayer.setSpeed(1.0f);
        speed.setText("SPEED: 1.0x");
      }
    });
    findViewById(R.id.x1_25).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ijkPlayer.setSpeed(1.25f);
        speed.setText("SPEED: 1.25x");
      }
    });
    findViewById(R.id.x1_5).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ijkPlayer.setSpeed(1.5f);
        speed.setText("SPEED: 1.5x");
      }
    });
    findViewById(R.id.x2_0).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ijkPlayer.setSpeed(2.0f);
        speed.setText("SPEED: 2.0x");
      }
    });
  }

  private void updateTextViewWithTimeFormat(TextView view, long totalSeconds) {
    long seconds = totalSeconds % 60;
    long minutes = (totalSeconds / 60) % 60;
    long hours = totalSeconds / 3600;
    @SuppressLint("DefaultLocale") String time =
        hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
            : String.format("%02d:%02d", minutes, seconds);
    view.setText(time);
  }


  private String getVideoPath() {
    return "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8";
//        return "android.resource://" + this.getPackageName() + "/" + resId;
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (ijkPlayer.isPlaying()) {
      ijkPlayer.stop();
    }
    IjkMediaPlayer.native_profileEnd();
  }

  Runnable runnable = new Runnable() {
    @Override
    public void run() { // 5秒后执行该方法
      // handler自带方法实现定时器
      try {
        seekBarVideo.setVisibility(View.INVISIBLE); // 隐藏
        tvCurrentTime.setVisibility(View.INVISIBLE);
        tvTotalTime.setVisibility(View.INVISIBLE);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 0:
          if (ijkPlayer.getDuration() > 0) {
            seekBarVideo.setProgress(
                (int) (ijkPlayer.getCurrentPosition() * seekBarVideo.getMax() / ijkPlayer
                    .getDuration()));
          }
          updateTextViewWithTimeFormat(tvCurrentTime, ijkPlayer.getCurrentPosition() / 1000);
          updateTextViewWithTimeFormat(tvTotalTime, ijkPlayer.getDuration() / 1000);
          this.sendEmptyMessageDelayed(0, 200);
          break;
      }
    }
  };
}
