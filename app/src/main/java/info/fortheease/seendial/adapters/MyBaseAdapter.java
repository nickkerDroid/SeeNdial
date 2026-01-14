package info.fortheease.seendial.adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

import info.fortheease.seendial.R;


public class MyBaseAdapter extends BaseAdapter {
    Context c;
    Drawable[] items;
    public MyBaseAdapter(Context c, Drawable[] arr)
    {
        this.c = c;
        items = arr;
    }

    @Override
    public int getCount()
    {
        return items.length;
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gridlayout, null);
        }
        SharedPreferences preferences = c.getSharedPreferences("SHARED_STUFF", Context.MODE_PRIVATE);
        String shape = preferences.getString("shape_new", "rectangle_round_corners");
        String borderColor = preferences.getString("border_color_new", "black");
        int cornerRadius = preferences.getInt("corner_radius_new", 100);
        ImageView imageView = view.findViewById(R.id.imageView);
        MaterialCardView cardView = view.findViewById(R.id.cardViewItemImage);
        setCardViewRadius(shape, cardView, cornerRadius);
        setCardViewBorderColor(borderColor, cardView);
        imageView.setBackground(items[i]);
        return view;
    }
    public void setCardViewRadius(String shape, MaterialCardView  cardView, int radius){
        if (Objects.equals(shape, "rectangle_round_corners")){
            cardView.setRadius(radius);
        }
        else if (Objects.equals(shape, "oval")){
            cardView.setRadius(600);
        }
        else if (Objects.equals(shape, "rectangle")){
            cardView.setRadius(0);
        }
    }
    public void setCardViewBorderColor(String borderColor, MaterialCardView cardView){
        if (Objects.equals(borderColor, "black")){
            cardView.setStrokeColor(Color.BLACK);
        }else if (Objects.equals(borderColor, "white")){
            cardView.setStrokeColor(Color.WHITE);
        }else if (Objects.equals(borderColor, "green")){
            cardView.setStrokeColor(Color.GREEN);
        }else if (Objects.equals(borderColor, "blue")){
            cardView.setStrokeColor(Color.BLUE);
        }else if (Objects.equals(borderColor, "cyan")){
            cardView.setStrokeColor(Color.CYAN);
        }else if (Objects.equals(borderColor, "purple")){
            cardView.setStrokeColor(Color.parseColor("#A020F0"));
        }
    }
}
