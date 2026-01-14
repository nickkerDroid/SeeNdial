package info.fortheease.seendial.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import info.fortheease.seendial.R;

public class ProgressDialog extends Dialog {
    public Context c;
    public Dialog d;
    TextView textView;

    public ProgressDialog(@NonNull Context context) {
        super(context);
        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        textView = findViewById(R.id.iknow);
    }
}
