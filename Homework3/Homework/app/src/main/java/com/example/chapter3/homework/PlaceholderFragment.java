package com.example.chapter3.homework;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


public class PlaceholderFragment extends Fragment {


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
    return inflater.inflate(R.layout.fragment_placeholder, container, false);
  }

  private class ListBaseAdapter extends BaseAdapter {

    private static final int NUM_LIST_ITEMS = 20;
    private Context mContext;

    public ListBaseAdapter(Context mContext) {
      this.mContext = mContext;
    }

    @Override
    public int getCount() {
      return NUM_LIST_ITEMS;
    }

    @Override
    public Object getItem(int position) {
      return null;
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      TextView textView;
      if (convertView == null) {
        textView = new TextView(mContext);
      } else {
        textView = (TextView) convertView;
      }
      textView.setText("item " + position);
      textView.setTextSize(30);
      return textView;
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    final ListView listView = getView().findViewById(R.id.list_view);
    listView.setAlpha(0.0f);
    listView.setAdapter(new ListBaseAdapter(getContext()));
    final LottieAnimationView animationView2 = getView().findViewById(R.id.animation_view2);
    animationView2.playAnimation();
    getView().postDelayed(new Runnable() {
      @Override
      public void run() {
        // 这里会在 5s 后执行
        // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
        ObjectAnimator animatorLottie = ObjectAnimator.ofFloat(animationView2, "alpha", 1.0f, 0.0f);
        animatorLottie.setDuration(1000);
        ObjectAnimator animatorList = ObjectAnimator.ofFloat(listView, "alpha", 0.0f, 1.0f);
        animatorLottie.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorLottie, animatorList);
        animatorSet.start();
      }
    }, 5000);
  }
}
