package info.fortheease.seendial.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import info.fortheease.seendial.R;
import info.fortheease.seendial.adapters.ViewPagerAdapter;
import info.fortheease.seendial.databinding.ActivityWelcomeBinding;
import info.fortheease.seendial.dialogs.ProgressDialog;
import maes.tech.intentanim.CustomIntent;

public class WelcomeActivity extends AppCompatActivity {

    ActivityWelcomeBinding binding;
    ViewPagerAdapter viewPagerAdapter;
    TextView[] dots;

    // NOTE: Activity is complete
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SeeNdial_MainActtivity);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.wlcBack.setOnClickListener(view -> {
            if (getItem(0) > 0){
                binding.viewPager.setCurrentItem(getItem(-1),true);
            }
        });
        binding.wlcmNext.setOnClickListener(view -> {
            if (getItem(0) < 2){
                binding.viewPager.setCurrentItem(getItem(1),true);
            }
            else {
                binding.wlcmSkip.performClick();
            }
        });
        binding.wlcmSkip.setOnClickListener(view -> {
            ProgressDialog dialog = new ProgressDialog(WelcomeActivity.this);
            dialog.show();
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.putExtra("checker", "good");
            startActivity(intent);
            CustomIntent.customType(WelcomeActivity.this, "left-to-right");
            finish();
        });

        viewPagerAdapter = new ViewPagerAdapter(WelcomeActivity.this);
        setUpIndicator(0);
        binding.wlcBack.setVisibility(View.INVISIBLE);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                setUpIndicator(position);
                if (position > 0){
                    binding.wlcBack.setVisibility(View.VISIBLE);
                }else {
                    binding.wlcBack.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void setUpIndicator(int position){

        dots = new TextView[3];
        binding.linear.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(HtmlCompat.fromHtml("&#8226", HtmlCompat.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#2A79E0"));
            binding.linear.addView(dots[i]);

        }

        dots[position].setTextColor(Color.parseColor("#1565CD"));

    }
    private int getItem(int i){
        return binding.viewPager.getCurrentItem() + i;
    }

}