package info.fortheease.seendial.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import info.fortheease.seendial.databinding.ActivityAboutBinding;
import maes.tech.intentanim.CustomIntent;

public class AboutActivity extends AppCompatActivity {
    // TODO: use animatoo instead of intentAnimation(postponed as 'not necessary')
    ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarEveryone);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.textView15.setOnClickListener(view -> {
            String url = "https://seendial.carrd.co/#privacy";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        binding.textView16.setOnClickListener(view -> {
            String url = "https://seendial.carrd.co/#terms";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        binding.cancellationPolicy.setOnClickListener(view -> {
            String url = "https://seendial.carrd.co/#refundpolicy";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        binding.contactUs.setOnClickListener(view -> {
            String url = "https://seendial.carrd.co/#contactus";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void finish(){
        super.finish();
        CustomIntent.customType(AboutActivity.this, "right-to-left");
    }
}