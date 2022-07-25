package com.example.onlinemusicapp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscMusic extends Fragment {
    View view;
    CircleImageView circleImageView;
    ObjectAnimator objectAnimator;

    @android.support.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dia_nhac, container, false);
        circleImageView = view.findViewById(R.id.imageviewcircle);
        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0f, 360f);
        objectAnimator.setDuration(10000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
        return view;
    }

    public void pauseDisc() {
        objectAnimator.pause();
    }

    public void startDisc() {
        objectAnimator.resume();
    }
}
