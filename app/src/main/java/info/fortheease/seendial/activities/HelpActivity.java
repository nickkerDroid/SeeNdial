package info.fortheease.seendial.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import info.fortheease.seendial.BuildConfig;
import info.fortheease.seendial.R;
import info.fortheease.seendial.databinding.ActivityHelpBinding;
import io.github.muddz.styleabletoast.StyleableToast;
import maes.tech.intentanim.CustomIntent;

public class HelpActivity extends AppCompatActivity{
    private RewardedAd rewardedAd;
    ActivityHelpBinding binding;
    // TODO: Activity is yet to be tested
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarYour);
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, BuildConfig.ADMOB_REWARDED_ID,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                    }
                });
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.upiBtn.setOnClickListener(View -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("UPI Id", "nishkarsh5151@oksbi");
            clipboard.setPrimaryClip(clip);
        });
        binding.watchAdBtn.setOnClickListener(view -> {
            // TODO: remove the points based feedback system only
            if (rewardedAd != null) {
                rewardedAd.show(HelpActivity.this, rewardItem -> StyleableToast.makeText(HelpActivity.this, "Thanks for your support!", Toast.LENGTH_SHORT, R.style.MyToastStyleIwant).show());
            } else {
                StyleableToast.makeText(HelpActivity.this, "The Ad isn't ready yet!", Toast.LENGTH_SHORT, R.style.MyToastStyleIwant).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish(){
        super.finish();
        CustomIntent.customType(HelpActivity.this, "right-to-left");
    }
}
