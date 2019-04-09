package coleo.com.abjo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import coleo.com.abjo.R;

public class ReportDialog extends Dialog {


    AnimationDrawable watchAnimationDrawable;

    public ReportDialog(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.report_dialog_dialog_layout);

        View view = findViewById(R.id.ok_report_button_id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
