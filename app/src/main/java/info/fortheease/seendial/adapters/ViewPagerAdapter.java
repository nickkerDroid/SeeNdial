package info.fortheease.seendial.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;

import info.fortheease.seendial.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    String aboutHead = "Initiate a Call with Ease";
    String aboutDescription = "This app provides you to call any contact, any number just by a simple touch on their image or photo. If you know someone illiterate, why not be a part of a noble work by informing them about this app.";
    String privacyHead = "We care about your Privacy";
    String privacyDescription = "Privacy Policy &\n" + "Terms & Conditions";
    String permissionsHead = "Enable our service";
    String permissionsDescription  = "To use our service, allow us the permissions on the next page";
    int[] animArray = {
            R.raw.avatar,
            R.raw.paperwork,
            R.raw.agreement
    };
    String[] headingArray = {
            aboutHead,
            privacyHead,
            permissionsHead,
    };
    String[] descriptionArray = {
            aboutDescription,
            privacyDescription,
            permissionsDescription
    };

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return animArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        LottieAnimationView animationView = view.findViewById(R.id.anim1);
        TextView heading = view.findViewById(R.id.heading);
        TextView description  = view.findViewById(R.id.description);

        if (position == 1){
            description.setTextColor(Color.parseColor("#2A79E0"));
            description.setOnClickListener(view1 -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://seendial.carrd.co/#privacy"));
                context.startActivity(intent);
            });
        }
        else {
            description.setTextColor(Color.BLACK);
        }

        animationView.setAnimation(animArray[position]);
        heading.setText(headingArray[position]);
        description.setText(descriptionArray[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayoutCompat)object);
    }
}
