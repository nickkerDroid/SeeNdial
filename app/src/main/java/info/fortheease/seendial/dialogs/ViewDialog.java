package info.fortheease.seendial.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import info.fortheease.seendial.User;
import info.fortheease.seendial.UserRepository;
import info.fortheease.seendial.adapters.MyBaseAdapter;
import info.fortheease.seendial.R;
import info.fortheease.seendial.databinding.ActivityMainBinding;

public class ViewDialog {
    Thread thread;
    public void showDialog(Activity activity, int position, ArrayList<Drawable> drawableArrayList, ArrayList<String> phoneNumber, ArrayList<Long> imgAddress, ActivityMainBinding binding){
        Handler handler;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.customdialogbox);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimatiton;

        Button delete = dialog.findViewById(R.id.btn_dialog);
        Button cancel = dialog.findViewById(R.id.btn_friend);
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                drawableArrayList.remove(position);
                phoneNumber.remove(position);
                imgAddress.remove(position);
                Drawable[] image = drawableArrayList.toArray(new Drawable[0]);
                MyBaseAdapter baseAdapter = new MyBaseAdapter(activity.getBaseContext(), image);
                binding.grdView.setAdapter(baseAdapter);
                dialog.dismiss();
                if (thread != null){
                    thread.interrupt();
                }
            }
        };
        delete.setOnClickListener(v -> {
            Runnable runnable = () -> {
                Message message = Message.obtain();
                User user = new User(phoneNumber.get(position), String.valueOf(imgAddress.get(position)));
                UserRepository userRepository = new UserRepository(activity.getApplication());
                userRepository.delete(user);
                handler.sendMessage(message);
            };

            thread = new Thread(runnable);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();

        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}
