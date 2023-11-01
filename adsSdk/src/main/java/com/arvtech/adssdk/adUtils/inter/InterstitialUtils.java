package com.arvtech.adssdk.adUtils.inter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.arvtech.adssdk.aditerface.Interstitial;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.arvtech.adssdk.AdProgressDialog;
import com.arvtech.adssdk.AdsAccountProvider;
import com.arvtech.adssdk.Constants;
import com.arvtech.adssdk.ArvatAds;

public class InterstitialUtils {
    Context mContext;
    String mUnitId;
    private Dialog dialog;
    AdsAccountProvider myPref;
    Interstitial listener;
    int adMobId;
//    private int failedCount = 0;
    private static int failedPreLoad = 0;
    public static InterstitialAd mInterstitialAd = null;

    public InterstitialUtils(Context mContext,Interstitial listener,int adMobId) {
        this.mContext = mContext;
        this.listener = listener;
        this.adMobId = adMobId;
        myPref = new AdsAccountProvider(mContext);
        if (adMobId == 1) {
            mUnitId = myPref.getInterAds1();
        } else if (adMobId == 2) {
            mUnitId = myPref.getInterAds2();
        } else {
            mUnitId = myPref.getInterAds3();
        }
    }

     public void loadInterstitial() {

         dialog = AdProgressDialog.show(mContext);

         AdRequest adRequest = new AdRequest.Builder().build();
         InterstitialAd.load(mContext, mUnitId, adRequest, new InterstitialAdLoadCallback() {
             @Override
             public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                 super.onAdFailedToLoad(loadAdError);
                 Log.e("INTER_TAG-->", "onAdFailedToLoad: Failed ads");
                 InterstitialUtilsFb.loadInterstitial(mContext,listener,dialog);

             }

             @Override
             public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                 super.onAdLoaded(interstitialAd);
                 show_interstitial(interstitialAd);
             }
         });
    }

    static void setCountDown() {
        Constants.mCountTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                try {
                    Constants.mCountTimer.cancel();
                    Constants.mCountTimer = null;
                    mInterstitialAd = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Constants.mCountTimer.start();
    }

     static void dismissCount() {
         try {
             if (Constants.mCountTimer != null) {
                 Constants.mCountTimer.cancel();
                 Constants.mCountTimer = null;
                 mInterstitialAd = null;
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    public void show_interstitial(InterstitialAd mInterstitialAd) {
        mInterstitialAd.show((Activity) mContext);
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Constants.isAdShowing = true;

            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                Constants.isAdShowing = false;
                Constants.isTimeFinish = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Constants.isTimeFinish = true;
                    }
                }, myPref.getAdsTime() * 1000);
                listener.onAdClose(true);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                listener.onAdClose(true);

            }
        });
    }


    public static void loadPreInterstitialAd(Context mContext,String mUnitId,AdRequest adRequest) {
        if (mInterstitialAd == null) {
            InterstitialAdLoadCallback loadCallback = new InterstitialAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    if (mInterstitialAd != null) {
                        mInterstitialAd = null;
                    }
                    dismissCount();
                    if (ArvatAds.isConnectingToInternet(mContext)) {
                        if (failedPreLoad == 3) {
                            failedPreLoad = 0;
                            Log.e("I_TAG", "onAdFailedToLoad: "+failedPreLoad );
                        } else {
                            failedPreLoad++;
                            loadPreInterstitialAd(mContext, mUnitId,adRequest);
                        }
                    } else {
                        Toast.makeText(mContext, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    Log.e("I_TAG", "onAdLoaded: " );
                    failedPreLoad = 0;
                    setCountDown();
                    mInterstitialAd = interstitialAd;
                }
            };

            InterstitialAd.load(mContext, mUnitId,adRequest,loadCallback);
        }
    }

    public void showPreInterstitial() {

        if ( mInterstitialAd != null) {
            dialog = AdProgressDialog.show(mContext);

            mInterstitialAd.show((Activity) mContext);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.e("I_TAG", "onAdShowedFullScreenContent: " );
                    dismissCount();
                    Constants.isAdShowing = true;
                    mInterstitialAd = null;
//                    dismissCount();
//                    load_interstitial(false);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Constants.isAdShowing = false;
                    Constants.isTimeFinish = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Constants.isTimeFinish = true;
                        }
                    }, myPref.getAdsTime() * 1000);
                    listener.onAdClose(true);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Log.e("I_TAG", "onAdFailedToShowFullScreenContent: " );
                    mInterstitialAd = null;
                    dismissCount();
                    Constants.isAdShowing = false;
                    Constants.isTimeFinish = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Constants.isTimeFinish = true;
                        }
                    }, myPref.getAdsTime() * 1000);
                    listener.onAdClose(true);

                }
            });
        } else {
            loadFailedInterstitialAd(mContext,mUnitId,listener);
        }

    }

    public  void loadFailedInterstitialAd(Context mContext,String mUnitId,Interstitial listener) {
        Dialog dialog1 = AdProgressDialog.show(mContext);
        AdRequest adRequest = getAdRequest();
        myPref = new AdsAccountProvider(mContext);
        InterstitialAd.load(mContext, mUnitId,adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("I_TAG", "onAdFailedToLoad: " );
                if (dialog1.isShowing()) {
                    dialog1.dismiss();
                }
                Constants.isAdShowing = false;
                Constants.isTimeFinish = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Constants.isTimeFinish = true;
                    }
                }, myPref.getAdsTime() * 1000);
                listener.onAdClose(true);
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Log.e("I_TAG", "onAdLoaded: " );
                interstitialAd.show((Activity) mContext);
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        Log.e("I_TAG", "onAdShowedFullScreenContent: " );
                        if (dialog1.isShowing()) {
                            dialog1.dismiss();
                        }
                        Constants.isAdShowing = true;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        Log.e("I_TAG", "onAdDismissedFullScreenContent: " );
                        Constants.isAdShowing = false;
                        Constants.isTimeFinish = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Constants.isTimeFinish = true;
                            }
                        }, myPref.getAdsTime() * 1000);
                        listener.onAdClose(true);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Log.e("I_TAG", "onAdFailedToShowFullScreenContent: " );
                        if (dialog1.isShowing()) {
                            dialog1.dismiss();
                        }
                        Constants.isAdShowing = false;
                        Constants.isTimeFinish = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Constants.isTimeFinish = true;
                            }
                        }, myPref.getAdsTime() * 1000);
                        listener.onAdClose(true);
                    }
                });
            }
        });
    }


    public static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }


}