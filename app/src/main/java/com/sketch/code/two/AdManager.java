package com.sketch.code.two;

import android.app.Activity;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AdManager implements RewardedVideoAdListener {
    Activity activity;
    AdRequest adRequest;
    private RewardedVideoAd mRewardedVideoAd;
    public AdManager(Activity _activity) {
        activity = _activity;
        MobileAds.initialize(activity, "ca-app-pub-8103339798944735/8048232754");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
    }

    public void show() {
        mRewardedVideoAd.loadAd("ca-app-pub-8103339798944735/8048232754",
                new AdRequest.Builder().addTestDevice("").build());
    }
    @Override
    public void onRewarded(RewardItem reward) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        mRewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(activity, "Thanks for watching :)", Toast.LENGTH_LONG).show();
    }
}
