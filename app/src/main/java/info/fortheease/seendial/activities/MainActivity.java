package info.fortheease.seendial.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import info.fortheease.seendial.R;
import info.fortheease.seendial.User;
import info.fortheease.seendial.UserDatabase;
import info.fortheease.seendial.UserRepository;
import info.fortheease.seendial.adapters.MyBaseAdapter;
import info.fortheease.seendial.databinding.ActivityMainBinding;
import info.fortheease.seendial.dialogs.ViewDialog;
import io.github.muddz.styleabletoast.StyleableToast;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {

    // NOTE: Activity is Complete
    ActivityMainBinding binding;
    ArrayList<Drawable> drawableArrayList = new ArrayList<>();
    ArrayList<String> phoneNumber = new ArrayList<>();
    ArrayList<Long> imgAddress = new ArrayList<>();
    int txtCode = 1;
    Handler handler;
    Handler handler2;
    List<User> allUsers23;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TXTCODE1 = "txtcode";
    Thread myTrd;
    Thread myThread;
    private ConsentInformation consentInformation;
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        if (intent != null){
                            Bundle bundle;
                            String phoneNum;
                            long imgAdd;
                            bundle = intent.getBundleExtra("bundle");
                            if (bundle != null) {
                                phoneNum = bundle.getString("phoneNo");
                                imgAdd = bundle.getLong("imgAdd");
                                File dir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "savedImage");
                                File dir2 = new File(dir1, imgAdd + ".jpg");
                                Bitmap myImg = BitmapFactory.decodeFile(dir2.getAbsolutePath());
                                Drawable drawable = new BitmapDrawable(getResources(), myImg);
                                drawableArrayList.add(drawable);
                                Runnable runnable = () -> {
                                    Message message = Message.obtain();
                                    User user = new User(phoneNum, String.valueOf(imgAdd));
                                    UserRepository userRepository = new UserRepository(getApplication());
                                    userRepository.insert(user);
                                    Log.e("hey", "Done");
                                    UserDatabase userDatabase = UserDatabase.getInstance(MainActivity.this);
                                    if (userDatabase != null){
                                        Log.e("hey", "Did it guy");
                                        List<User> allUser = new UserRepository(getApplication()).getAllUsers();
                                        if (allUser != null){
                                            for (int i = 0; i<allUser.size();i++){
                                                String phone = allUser.get(i).getPhoneNum();
                                                String img = allUser.get(i).getImgAddress();
                                                phoneNumber.add(phone);
                                                imgAddress.add(Long.parseLong(img));
                                            }
                                        }
                                        handler.sendMessage(message);
                                    }
                                };
                                handler = new Handler(Looper.getMainLooper()){
                                    @Override
                                    public void handleMessage(@NonNull Message msg) {
                                        Drawable[] image = drawableArrayList.toArray(new Drawable[0]);
                                        MyBaseAdapter baseAdapter = new MyBaseAdapter(MainActivity.this, image);
                                        binding.grdView.setAdapter(baseAdapter);
                                        binding.nester.setNestedScrollingEnabled(true);
                                        myTrd.interrupt();
                                    }
                                };
                                myTrd = new Thread(runnable);
                                myTrd.setPriority(Thread.MAX_PRIORITY);
                                myTrd.start();
                            }

                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SeeNdial_MainActtivity);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().clear()
                    .putBoolean("isFirstRun", false).apply();
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String checker = bundle.getString("checker");
            if (Objects.equals(checker, "good")){
                new TapTargetSequence(MainActivity.this)
                        .targets(
                                TapTarget.forView(binding.button, "Add Contact", "Tap here to add contacts.")
                                        .outerCircleColor(R.color.color12)
                                        .outerCircleAlpha(0.96f)
                                        .targetCircleColor(R.color.white)
                                        .titleTextSize(20)
                                        .titleTextColor(R.color.white)
                                        .descriptionTextSize(15)
                                        .descriptionTextColor(R.color.black)
                                        .textColor(R.color.black)
                                        .textTypeface(Typeface.SANS_SERIF)
                                        .dimColor(R.color.black)
                                        .drawShadow(true)
                                        .cancelable(false)
                                        .tintTarget(true)
                                        .transparentTarget(true)
                                        .targetRadius(40),
                                TapTarget.forToolbarMenuItem(binding.toolbarMy, R.id.backey, "Change BG", "Tap here to change the Background.")
                                        .outerCircleColor(R.color.teal_200)
                                        .outerCircleAlpha(0.96f)
                                        .targetCircleColor(R.color.white)
                                        .titleTextSize(20)
                                        .titleTextColor(R.color.white)
                                        .descriptionTextSize(15)
                                        .descriptionTextColor(R.color.black)
                                        .textColor(R.color.black)
                                        .textTypeface(Typeface.SANS_SERIF)
                                        .dimColor(R.color.black)
                                        .drawShadow(true)
                                        .cancelable(false)
                                        .tintTarget(true)
                                        .transparentTarget(true)
                                        .targetRadius(20),
                                TapTarget.forToolbarOverflow(binding.toolbarMy, "Options Menu", "Explore various options here.")
                                        .outerCircleColor(R.color.teal_200)
                                        .outerCircleAlpha(0.96f)
                                        .targetCircleColor(R.color.white)
                                        .titleTextSize(20)
                                        .titleTextColor(R.color.white)
                                        .descriptionTextSize(15)
                                        .descriptionTextColor(R.color.black)
                                        .textColor(R.color.black)
                                        .textTypeface(Typeface.SANS_SERIF)
                                        .dimColor(R.color.black)
                                        .drawShadow(true)
                                        .cancelable(true)
                                        .tintTarget(true)
                                        .transparentTarget(true)
                                        .targetRadius(20)
                        ).listener(new TapTargetSequence.Listener() {
                            @Override
                            public void onSequenceFinish() {

                            }

                            @Override
                            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                            }

                            @Override
                            public void onSequenceCanceled(TapTarget lastTarget) {

                            }
                        }).start();
            }
        }


        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_more_vert_24);
        binding.toolbarMy.setOverflowIcon(drawable);
        setSupportActionBar(binding.toolbarMy);
        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        binding.circleImageView12.post(() -> revealLogo(binding.circleImageView12));
        binding.button.post(() -> revealLogo(binding.button));
        loadData();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
            } else {
                Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
            }
        // Create a ConsentRequestParameters object.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    // TODO: Load and show the consent form.
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w(TAG, String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });

        if (phoneNumber.isEmpty()) {
            binding.nester.setNestedScrollingEnabled(false);
        }

        binding.button.setOnClickListener(view -> {
            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.cliccck);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            Intent goodBoy = new Intent(MainActivity.this, AddContactActivity.class);
//          startActivityForResult(goodBoy, ActionCode);
            resultLauncher.launch(goodBoy);
            CustomIntent.customType(MainActivity.this, "left-to-right");
        });
        binding.grdView.setOnItemClickListener((adapterView, view, i, l) -> {
            Runnable runnable = () -> {
                Intent calling = new Intent(Intent.ACTION_CALL);
                UserDatabase userDatabase = UserDatabase.getInstance(MainActivity.this);
                if (userDatabase != null) {
                    List<User> allUsers = userDatabase.userDAO().getAllUsers();
                    String required = allUsers.get(i).getPhoneNum();
                    calling.setData(Uri.parse("tel:" + required));
                    startActivity(calling);
                } else {
                    StyleableToast.makeText(MainActivity.this, "Database got nothing", Toast.LENGTH_SHORT, R.style.MyToastStyleIwant).show();
                }
            };
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
        });
        binding.grdView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Log.e("user", "User Deleted successfully");
            ViewDialog viewDialog = new ViewDialog();
            viewDialog.showDialog(MainActivity.this, i, drawableArrayList, phoneNumber, imgAddress, binding);
            return true;
        });
        binding.backey.setOnClickListener(view -> {
            if (txtCode != 7) {
                txtCode++;
                loadBG(txtCode);
            } else {
                txtCode = 1;
                loadBG(txtCode);
            }
        });
    }

    private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }
        MobileAds.initialize(this, initializationStatus -> {
            AdRequest adRequest = new AdRequest.Builder().build();
            binding.adView.loadAd(adRequest);

            binding.adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    super.onAdFailedToLoad(adError);
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    StyleableToast.makeText(MainActivity.this, "Please avoid mistakenly clicking on ads", Toast.LENGTH_SHORT, R.style.MyToastStyleIwant).show();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }
            });
        });
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TXTCODE1, txtCode);
        editor.apply();

    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences.getInt(TXTCODE1, 15) != 15){
            Runnable runnable = () -> {
                allUsers23 = new UserRepository(getApplication()).getAllUsers();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String putchka = gson.toJson(allUsers23);
                bundle.putString("bun", putchka);
                Message message = Message.obtain();
                message.setData(bundle);
                handler2.sendMessage(message);
            };

//            if (allUsers23 != null){
//                for (User user1 : allUsers23){
//                    String phone = user1.getPhoneNum();
//                    String img = user1.getImgAddress();
//                    phoneNumber.add(phone);
//                    imgAddress.add(Long.parseLong(img));
//
//                }
//                drawableArrayList = new ArrayList<>();
//                for (int i = 0;i < imgAddress.size();i++){
//                    File dir23 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "savedImage");
//                    File dir24 = new File(dir23, imgAddress.get(i) + ".jpg");
//                    Bitmap myImg = BitmapFactory.decodeFile(dir24.getAbsolutePath());
//                    Drawable drawable = new BitmapDrawable(getResources(), myImg);
//                    drawableArrayList.add(drawable);
//                }
//                Drawable[] image = drawableArrayList.toArray(new Drawable[0]);
//                MyBaseAdapter baseAdapter = new MyBaseAdapter(MainActivity.this, image);
//                binding.grdView.setAdapter(baseAdapter);
//                binding.nester.setNestedScrollingEnabled(true);
////                myThread.interrupt();
//                Log.e("thread", "interruption successful");
            myThread = new Thread(runnable);
            myThread.setPriority(Thread.MAX_PRIORITY);
            myThread.start();

            handler2 = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if (allUsers23 != null){
                        for (User user1 : allUsers23){
                            String phone = user1.getPhoneNum();
                            String img = user1.getImgAddress();
                            phoneNumber.add(phone);
                            imgAddress.add(Long.parseLong(img));

                        }
                        drawableArrayList = new ArrayList<>();
                        for (int i = 0;i < imgAddress.size();i++){
                            File dir23 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "savedImage");
                            File dir24 = new File(dir23, imgAddress.get(i) + ".jpg");
                            Bitmap myImg = BitmapFactory.decodeFile(dir24.getAbsolutePath());
                            Drawable drawable = new BitmapDrawable(getResources(), myImg);
                            drawableArrayList.add(drawable);
                        }
                        Drawable[] image = drawableArrayList.toArray(new Drawable[0]);
                        MyBaseAdapter baseAdapter = new MyBaseAdapter(MainActivity.this, image);
                        binding.grdView.setAdapter(baseAdapter);
                        binding.nester.setNestedScrollingEnabled(true);
                        myThread.interrupt();
                        Log.e("thread", "interruption successful");
                    }
                }
            };
            txtCode = sharedPreferences.getInt(TXTCODE1, 0);
            loadBG(txtCode);
        }
    }
    public void loadBG(int texture){
        switch (texture){
            case 1:
                binding.cntLayout.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.txt7));
                break;
            case 2:
                binding.cntLayout.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.txt2));
                break;
            case 3:
                binding.cntLayout.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.txt3));
                break;
            case 4:
                binding.cntLayout.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.txt4));
                break;
            case 5:
                binding.cntLayout.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.txt5));
                break;
            case 6:
                binding.cntLayout.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.txt6));
                break;
            case 7:
                binding.cntLayout.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.txt1));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menusir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        if (itemId == R.id.about){
            Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent1);
            CustomIntent.customType(MainActivity.this, "left-to-right");
        }
        else if (itemId == R.id.helpus){
            startActivity(new Intent(MainActivity.this, HelpActivity.class));
            CustomIntent.customType(MainActivity.this, "left-to-right");
        }
        else if (itemId == R.id.feedback){
            Intent mail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "fortheease@gmail.com", null));
            mail.putExtra(Intent.EXTRA_SUBJECT, "feedback");
            startActivity(Intent.createChooser(mail, null));
        }
        else if (itemId == R.id.htu){
            Intent intent = new Intent(MainActivity.this, HowtouseActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, binding.button, Objects.requireNonNull(ViewCompat.getTransitionName(binding.button)));
            startActivity(intent, options.toBundle());
        }
        else if (itemId == R.id.settings12){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            CustomIntent.customType(MainActivity.this, "left-to-right");
            finish();
        }
        return true;
    }

    private void revealLogo(View view) {
        int wid = view.getWidth() / 2;
        int high = view.getHeight() / 2;
        float finalRadius = (float) Math.hypot(wid, high);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, wid, high, 0, finalRadius);
        anim.setStartDelay(300);
        anim.start();
    }
}