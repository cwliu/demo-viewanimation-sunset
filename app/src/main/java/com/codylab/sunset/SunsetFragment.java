package com.codylab.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSeaView;
    private View mSkyView;
    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    public static SunsetFragment newInstance() {

        Bundle args = new Bundle();

        SunsetFragment fragment = new SunsetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;

        mSunView = view.findViewById(R.id.sun);
        mSeaView = view.findViewById(R.id.sea);
        mSkyView = view.findViewById(R.id.sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSunsetAndSunrise();
            }
        });

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        return view;
    }

    private AnimatorSet getSunsetAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator(2));

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator.ofInt(
                mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor
        ).setDuration(1500);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());


        ObjectAnimator nightskyAnimator = ObjectAnimator.ofInt(
                mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor
        );
        nightskyAnimator.setEvaluator(new ArgbEvaluator());
        nightskyAnimator.setDuration(1500);

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(heightAnimator).with(sunsetSkyAnimator).before(nightskyAnimator);
//        animationSet.start();
        return animationSet;
    }

    private AnimatorSet getSunriseAnimation() {
        float sunYStart = mSkyView.getHeight();
        float sunYEnd = mSkyView.getHeight() / 3;

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd);
        heightAnimator.setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator(2));

        ObjectAnimator nightskyAnimator = ObjectAnimator.ofInt(
                mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor
        );
        nightskyAnimator.setEvaluator(new ArgbEvaluator());
        nightskyAnimator.setDuration(1500);

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator.ofInt(
                mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor
        ).setDuration(1500);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());


        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(nightskyAnimator).before(heightAnimator).before(sunsetSkyAnimator);
//        animationSet.start();
        return animationSet;
    }
    private void startSunsetAndSunrise(){
        AnimatorSet sunsetAnimation = getSunsetAnimation();
        AnimatorSet sunriseAnimation = getSunriseAnimation();

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.playSequentially(sunsetAnimation, sunriseAnimation);
        animationSet.start();
    }

}
