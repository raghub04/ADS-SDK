# Ads-sdk-New

To get a Git project into your build:
Step 1. Add the JitPack repository to your build file

========= Features ==========

          ==> For Banner, Interstitial, and Native ads, you can use multiple ad IDs; 
          ==> You can set interstitial ads to appear periodically.
          

========= Gradle ==========

1). Add it in your root build.gradle at the end of repositories:

          allprojects {
              repositories {
                     maven { url 'https://jitpack.io' }
            }
          }
          
2). Add the dependency

        
==> FOR SIMPLE ADS

       implementation 'com.github.raghub04:ADS-SDK:Tag'  
     
==> Pre Interstitital Ads

     // 1 = admob id
     
    @Override
    protected void onResume() {
        ArvatAds.loadPreInterstitial(1,this);
        super.onResume();
    }
         
       
===> FOR BANNER IN APPLICATION CLASS


    public class MyApplication extends Application implements ActivityLifecycleCallbacks {

            private Activity currentActivity;

           @Override
           public void onCreate() {
              super.onCreate();
              this.registerActivityLifecycleCallbacks(this);

           }

           @Override
           public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
               currentActivity = activity;
            }

            @Override
           public void onActivityStarted(@NonNull Activity activity) {}

            @Override
           public void onActivityResumed(@NonNull Activity activity) {
              AdBanner.resumeAdView();

          }

           @Override
            public void onActivityPaused(@NonNull Activity activity) {
                if (AdBanner.getCurrentActivity() != null && (AdBanner.getCurrentActivity() == currentActivity)) {
                  AdBanner.pauseAdView();
              }
           }

           @Override
           public void onActivityStopped(@NonNull Activity activity) {
          }

           @Override
          public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
           }

          @Override
           public void onActivityDestroyed(@NonNull Activity activity) {
             if (AdBanner.getCurrentActivity() != null && (AdBanner.getCurrentActivity() == currentActivity)) {
                      AdBanner.destroyAdView();
                     }
          }

      }
       
        
        
 ===== color guide ====== 
 
            <------------   set color in theme --------------->

                     Progressbar color ------> colorPrimary
                     Button and Space -------> tabSelectedTextColor

 3). use below code in activity 
 
 
              Button showAds;
              RelativeLayout rlBanner,rl_native;
              View tv_space;

             showAds = findViewById(R.id.btn_next);
                  rlBanner = findViewById(com.arvtech.adssdk.R.id.rl_banner);
                  rl_native = findViewById(com.arvtech.adssdk.R.id.rl_native);
                  tv_space = findViewById(com.arvtech.adssdk.R.id.tv_space);


                  ArvatAds.initializeAds(this);  // Once Application
                  ArvatAds.enableTestMode(this); // Once

                  ArvatAds.initDefaultValue(); // Once Splash
                  
                  ArvatAds.showBanner(this,rlBanner,1);
                  ArvatAds.showNative(this,rl_native,tv_space,1, ArvatAds.AdTemplate.NATIVE_300);

                  showAds.setOnClickListener(v -> {
                      ArvatAds.showInterstitial(1, this, new Interstitial() {
                          @Override
                          public void onAdClose(boolean isFail) {
                              startActivity(new Intent(MainActivity.this,Main2Activity.class));
                          }
                      });
                  });
        
   ======= Native Templates ===============
   
         ArvatAds.AdTemplate.NATIVE_350,
         ArvatAds.AdTemplate.NATIVE_300,
         ArvatAds.AdTemplate.NATIVE_150,
         ArvatAds.AdTemplate.NATIVE_100,
         ArvatAds.AdTemplate.NATIVE_50,
         ArvatAds.AdTemplate.NATIVE_40
