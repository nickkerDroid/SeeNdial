package info.fortheease.seendial.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import info.fortheease.seendial.R;
import info.fortheease.seendial.databinding.ActivityAddContactBinding;
import io.github.muddz.styleabletoast.StyleableToast;
import maes.tech.intentanim.CustomIntent;

public class AddContactActivity extends AppCompatActivity {

    ActivityAddContactBinding binding;
    long aloo = System.currentTimeMillis();
    int startCode = 0;
    int photoCode = 0;
    public static final String[] countryAreaCodes = {"91", "93", "355", "213",
            "376", "244", "672", "54", "374", "297", "61", "43", "994", "973",
            "880", "375", "32", "501", "229", "975", "591", "387", "267", "55",
            "673", "359", "226", "95", "257", "855", "237", "1", "238", "236",
            "235", "56", "86", "61", "61", "57", "269", "242", "682", "506",
            "385", "53", "357", "420", "45", "253", "670", "593", "20", "503",
            "240", "291", "372", "251", "500", "298", "679", "358", "33",
            "689", "241", "220", "995", "49", "233", "350", "30", "299", "502",
            "224", "245", "592", "509", "504", "852", "36",  "62", "98",
            "964", "353", "44", "972", "39", "225", "1876", "81", "962", "7",
            "254", "686", "965", "996", "856", "371", "961", "266", "231",
            "218", "423", "370", "352", "853", "389", "261", "265", "60",
            "960", "223", "356", "692", "222", "230", "262", "52", "691",
            "373", "377", "976", "382", "212", "258", "264", "674", "977",
            "31", "687", "64", "505", "227", "234", "683", "850", "47", "968",
            "92", "680", "507", "675", "595", "51", "63", "870", "48", "351",
            "1", "974", "40", "7", "250", "590", "685", "378", "239", "966",
            "221", "381", "248", "232", "65", "421", "386", "677", "252", "27",
            "82", "34", "94", "290", "508", "249", "597", "268", "46", "41",
            "963", "886", "992", "255", "66", "228", "690", "676", "216", "90",
            "993", "688", "971", "256", "44", "380", "598", "1", "998", "678",
            "39", "58", "84", "681", "967", "260", "263" };
    public static final String[] countryNames = {"India", "Afghanistan", "Albania",
            "Algeria", "Andorra", "Angola", "Antarctica", "Argentina",
            "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
            "Bahrain", "Bangladesh", "Belarus", "Belgium", "Belize", "Benin",
            "Bhutan", "Bolivia", "Bosnia And Herzegovina", "Botswana",
            "Brazil", "Brunei Darussalam", "Bulgaria", "Burkina Faso",
            "Myanmar", "Burundi", "Cambodia", "Cameroon", "Canada",
            "Cape Verde", "Central African Republic", "Chad", "Chile", "China",
            "Christmas Island", "Cocos (keeling) Islands", "Colombia",
            "Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia",
            "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti",
            "Timor-leste", "Ecuador", "Egypt", "El Salvador",
            "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia",
            "Falkland Islands (malvinas)", "Faroe Islands", "Fiji", "Finland",
            "France", "French Polynesia", "Gabon", "Gambia", "Georgia",
            "Germany", "Ghana", "Gibraltar", "Greece", "Greenland",
            "Guatemala", "Guinea", "Guinea-bissau", "Guyana", "Haiti",
            "Honduras", "Hong Kong", "Hungary",  "Indonesia", "Iran",
            "Iraq", "Ireland", "Isle Of Man", "Israel", "Italy", "Ivory Coast",
            "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
            "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho",
            "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
            "Macao", "Macedonia", "Madagascar", "Malawi", "Malaysia",
            "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania",
            "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",
            "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique",
            "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia",
            "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Korea",
            "Norway", "Oman", "Pakistan", "Palau", "Panama",
            "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn",
            "Poland", "Portugal", "Puerto Rico", "Qatar", "Romania",
            "Russian Federation", "Rwanda", "Saint Barthélemy", "Samoa",
            "San Marino", "Sao Tome And Principe", "Saudi Arabia", "Senegal",
            "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia",
            "Slovenia", "Solomon Islands", "Somalia", "South Africa",
            "Korea, Republic Of", "Spain", "Sri Lanka", "Saint Helena",
            "Saint Pierre And Miquelon", "Sudan", "Suriname", "Swaziland",
            "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan",
            "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga",
            "Tunisia", "Turkey", "Turkmenistan", "Tuvalu",
            "United Arab Emirates", "Uganda", "United Kingdom", "Ukraine",
            "Uruguay", "United States", "Uzbekistan", "Vanuatu",
            "Holy See (vatican City State)", "Venezuela", "Viet Nam",
            "Wallis And Futuna", "Yemen", "Zambia", "Zimbabwe" };
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        if (intent != null){
                            try {
                                Uri imageUri = intent.getData();
                                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                Drawable drawable2 = new BitmapDrawable(getResources(), selectedImage);
                                binding.profileImage.setImageDrawable(drawable2);
                            }catch (FileNotFoundException e){
                                e.printStackTrace();
                                StyleableToast.makeText(AddContactActivity.this, "Something went wrong", Toast.LENGTH_SHORT, R.style.MyToastStyleIwant).show();
                            }
                        }

                    }
                }
            });
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        assert intent != null;
                        Uri contactUri = intent.getData();
                        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                        Cursor cursor = AddContactActivity.this.getContentResolver().query(contactUri, projection,
                                null, null, null);

                        if (cursor != null && cursor.moveToFirst()) {
                            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String number = cursor.getString(numberIndex);
                            if (number.startsWith("+")){
                                for (String countryAreaCode : countryAreaCodes) {
                                    number = number.replace("+" + countryAreaCode, "");
                                }
                                number = number.replace(" ", "");
                            }
                            binding.editTextPhone.getEditText().setText(number);
                        }

                        assert cursor != null;
                        cursor.close();
                    }
                }
            });
    // NOTE: Activity is Complete
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarNone);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countryNames);
        binding.planetsSpinner.setAdapter(adapter);
        binding.planetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String imp = "+" + countryAreaCodes[i];
                binding.textView.setText(imp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String savedCode = getSharedPreferences("SHARED_STUFF", MODE_PRIVATE).getString("code_new", "91");
//        String important = "+" + savedCode;
//        binding.textView.setText(important);
        int indexTemp = Arrays.asList(countryAreaCodes).indexOf(savedCode);
        binding.planetsSpinner.setSelection(indexTemp);
        Drawable drawable = binding.profileImage.getDrawable();
        binding.profileImage.setOnClickListener(view -> {
            if (photoCode == 0){
                MediaPlayer mediaPlayer = MediaPlayer.create(AddContactActivity.this, R.raw.cliccck);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                Intent alooGobhi = new Intent(Intent.ACTION_PICK);
                alooGobhi.setType("image/*");
                photoCode++;
                startCode = 0;
//                startActivityForResult(alooGobhi, RequestCode);
                activityResult.launch(alooGobhi);
                CustomIntent.customType(AddContactActivity.this, "right-to-left");
            }
            else {
                StyleableToast.makeText(AddContactActivity.this, "", Toast.LENGTH_SHORT, R.style.MyToastStyleIwant).show();

            }
        });

        binding.imageView3.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startCode = 0;
//            startActivityForResult(intent, RequestCode2);
            activityResultLauncher.launch(intent);
        });
        binding.button2.setOnClickListener(view -> {
            if (checkPhone() && binding.profileImage.getDrawable() != drawable){
                MediaPlayer mediaPlayer = MediaPlayer.create(AddContactActivity.this, R.raw.cliccck);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                saveImage();
                String phoneNo = binding.textView.getText().toString().trim() + binding.editTextPhone.getEditText().getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putString("phoneNo", phoneNo);
                bundle.putLong("imgAdd", aloo);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("bundle", bundle);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            else {
                if (binding.profileImage.getDrawable() == drawable){
                    StyleableToast.makeText(AddContactActivity.this, "Please choose an Image by clicking on the avatar", Toast.LENGTH_LONG, R.style.MyToastStyleIwant).show();
                }else {
                    StyleableToast.makeText(AddContactActivity.this, "Please recheck your submissions", Toast.LENGTH_SHORT, R.style.MyToastStyleIwant).show();
                }
            }
        });
        binding.constraintLayout.addTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                boolean firstRun = getSharedPreferences("ADD_CONTACT_RUN", MODE_PRIVATE).getBoolean("addContact", true);
                if (firstRun){
                    getSharedPreferences("ADD_CONTACT_RUN", MODE_PRIVATE).edit().clear().putBoolean("addContact", false).apply();
                    new TapTargetSequence(AddContactActivity.this).targets(
                            TapTarget.forView(binding.profileImage, "Contact Image", "Tap here to choose a contact image.")
                                    .outerCircleColor(R.color.purmiray)
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
                                    .targetRadius(60),
                            TapTarget.forView(binding.planetsSpinner, "Country Code", "Tap here to choose the country of your contact.")
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
                                    .targetRadius(50),
                            TapTarget.forView(binding.imageView3, "Phone Number", "Tap here to choose the phone number for your contact.")
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
                                    .targetRadius(30)
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

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });

    }
    private void saveImage(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.profileImage.getDrawable();
        Bitmap finalBitmap = bitmapDrawable.getBitmap();
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "savedImage");
        if (!dir.exists()){
            dir.mkdir();
        }
        File file2 = new File(dir, aloo + ".jpg");
        try {
            OutputStream outputStream = new FileOutputStream(file2);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (startCode == 1){
            finish();
        }
        else {
            startCode = 1;
        }
    }
    @Override
    public void finish(){
        super.finish();
        CustomIntent.customType(AddContactActivity.this, "left-to-right");
    }

    private boolean checkPhone(){
        String text = binding.editTextPhone.getEditText().getText().toString().trim();
        if (text.length() == 0){
            binding.editTextPhone.setError("Please enter a phone number");
            return false;
        }
        else if (text.length() < 10){
            binding.editTextPhone.setError("Phone number not valid");
            return false;
        }
        else {
            return true;
        }
    }



}