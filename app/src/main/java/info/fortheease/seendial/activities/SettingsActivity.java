package info.fortheease.seendial.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;

import java.util.Objects;

import info.fortheease.seendial.R;
import info.fortheease.seendial.databinding.SettingsActivityBinding;
import info.fortheease.seendial.dialogs.ProgressDialog;
import maes.tech.intentanim.CustomIntent;

public class SettingsActivity extends AppCompatActivity {

    SettingsActivityBinding binding;
    static SettingsFragment fragment = new SettingsFragment();

    // NOTE: Activity is complete although UI update can be accommodated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_more_vert_24);
        binding.toolbarSettings.setOverflowIcon(drawable);
        setSupportActionBar(binding.toolbarSettings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, fragment)
                    .commit();
        }
        binding.backOne.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ProgressDialog dialog = new ProgressDialog(SettingsActivity.this);
                dialog.show();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                CustomIntent.customType(SettingsActivity.this, "right-to-left");
                finish();
                dialog.dismiss();
            }
        });
    }
    public static void loadSettings(SharedPreferences preFs, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String countryCode = preferences.getString("code", "91");
        String itemShape = preferences.getString("shape", "rectangle_round_corners");
        String borderColor = preferences.getString("border_color", "black");
        int cornerRadius = preferences.getInt("corner_radius", 100);
        SharedPreferences.Editor prefsEditor = preFs.edit();
        prefsEditor.clear();
        prefsEditor.putString("shape_new", itemShape);
        prefsEditor.putString("code_new", countryCode);
        prefsEditor.putString("border_color_new", borderColor);
        prefsEditor.putInt("corner_radius_new", cornerRadius);
        prefsEditor.apply();
        EditTextPreference countryCodeEditText = fragment.findPreference("code");
        assert countryCodeEditText != null;
        ListPreference contactShapePreference = fragment.findPreference("shape");
        assert contactShapePreference != null;
        ListPreference contactBorderColorPreference = fragment.findPreference("border_color");
        assert contactBorderColorPreference != null;
        SeekBarPreference cornerRadiusSeekBarPreference = fragment.findPreference("corner_radius");
        assert cornerRadiusSeekBarPreference != null;
        if (!Objects.equals(contactShapePreference.getValue(), "rectangle_round_corners")){
            cornerRadiusSeekBarPreference.setEnabled(false);
        }
        countryCodeEditText.setOnPreferenceChangeListener((preference, newValue) -> {
            String coDe = (String) newValue;
            SharedPreferences.Editor prefsEditor1 = preFs.edit();
            prefsEditor1.remove("code_new").apply();
            prefsEditor1.putString("code_new", coDe);
            prefsEditor1.apply();
            return true;
        });
        contactShapePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String shaPe = (String) newValue;
            cornerRadiusSeekBarPreference.setEnabled(Objects.equals(shaPe, "rectangle_round_corners"));
            SharedPreferences.Editor prefsEditor12 = preFs.edit();
            prefsEditor12.remove("shape_new").apply();
            prefsEditor12.putString("shape_new", shaPe);
            prefsEditor12.apply();
            return true;
        });
        contactBorderColorPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String borderColOr = (String) newValue;
            SharedPreferences.Editor prefsEditor13 = preFs.edit();
            prefsEditor13.remove("border_color_new").apply();
            prefsEditor13.putString("border_color_new", borderColOr);
            prefsEditor13.apply();
            return true;
        });
        cornerRadiusSeekBarPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            int cornerRadiUs = (int) newValue;
            SharedPreferences.Editor prefsEditor14 = preFs.edit();
            prefsEditor14.remove("corner_radius_new").apply();
            prefsEditor14.putInt("corner_radius_new", cornerRadiUs);
            prefsEditor14.apply();
            return true;
        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            loadSettings(getActivity().getSharedPreferences("SHARED_STUFF", MODE_PRIVATE), getActivity().getApplicationContext());
        }
    }
}