package coleo.com.abjo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import coleo.com.abjo.R;
import coleo.com.abjo.activity.MainActivity;

public class SendJsonDialog extends Dialog {


    AnimationDrawable watchAnimationDrawable;

    public SendJsonDialog(@NonNull final Context context, String length) {
        super(context);
        setContentView(R.layout.upload_json_layout);

        TextView temp = findViewById(R.id.description_upload_text_view_id);
        temp.setText("length is : " + length.length() + " \n" + length);
        ImageView watch = findViewById(R.id.sand_watch_image_view_id);
        watch.setBackgroundResource(R.drawable.sand_watch_animation);
        watchAnimationDrawable = (AnimationDrawable) watch.getBackground();
        watchAnimationDrawable.start();

        ImageView watchBack = findViewById(R.id.sand_watch_back_image_view_id);
        RotateAnimation anim = new RotateAnimation(0f, 359f
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(2000);
        watchBack.startAnimation(anim);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dismiss();
                ((MainActivity) context).backToMain();
            }
        };
        new Handler().postDelayed(runnable, 5000);
        View view = findViewById(R.id.dialog_layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
