package coleo.com.abjo.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import coleo.com.abjo.R;
import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_class.NewUserForServer;
import coleo.com.abjo.server_class.ServerClass;

public class SignUpActivity extends AppCompatActivity {

    private EditText name;
    private EditText lastName;
    private EditText studentNumber;
    private EditText introduceInput;

    private ImageView manImage;
    private TextView manText;
    private ImageView womanImage;
    private TextView womanText;

    private boolean isMen;
    private boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name_input_id);
        lastName = findViewById(R.id.family_input_id);
        studentNumber = findViewById(R.id.student_number_input_id);
        introduceInput = findViewById(R.id.introducer_input_id);

        manImage = findViewById(R.id.man_image_view);
        womanImage = findViewById(R.id.woman_image_view);
        manText = findViewById(R.id.man_text_view);
        womanText = findViewById(R.id.woman_text_view);

        manImage.setOnClickListener(genderListener);
        manText.setOnClickListener(genderListener);
        womanImage.setOnClickListener(genderListener);
        womanText.setOnClickListener(genderListener);

        ImageView back = findViewById(R.id.top_image_id);
        back.getLayoutParams().width = Constants.getScreenWidth(this);
        back.getLayoutParams().height = (int) (Constants.getScreenWidth(this) * Constants.fuckingRatioTop);
        back.requestLayout();

        ConstraintLayout.LayoutParams parameter = (ConstraintLayout.LayoutParams) name.getLayoutParams();
        parameter.setMargins(parameter.leftMargin, (int) Constants.pxToDp(this, (int) (Constants.getScreenWidth(this) * Constants.fuckingRatioTop)) + 10
                , parameter.rightMargin, parameter.bottomMargin); // left, top, right, bottom
        name.setLayoutParams(parameter);
        Log.i("SIGN up ACTIVITY", "onCreate: ");
    }

    View.OnClickListener genderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.man_text_view:
                case R.id.man_image_view: {
                    if (!isSelected) {
                        isSelected = true;
                    }
                    selectMan();
                    break;
                }
                case R.id.woman_image_view:
                case R.id.woman_text_view: {
                    if (!isSelected) {
                        isSelected = true;
                    }
                    selectWoman();
                    break;
                }
            }
        }
    };

    private void selectMan() {
        isMen = true;
        manImage.setImageResource(R.mipmap.sign_up_man_selected);
        manText.setTextColor(getResources().getColor(R.color.selected_gender_color));
        womanImage.setImageResource(R.mipmap.sign_up_woman);
        womanText.setTextColor(getResources().getColor(R.color.colorHintEditText));
    }

    private void selectWoman() {
        isMen = false;
        manImage.setImageResource(R.mipmap.sign_up_man);
        manText.setTextColor(getResources().getColor(R.color.colorHintEditText));
        womanImage.setImageResource(R.mipmap.sign_up_woman_selected);
        womanText.setTextColor(getResources().getColor(R.color.selected_gender_color));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_sign_up_id: {
                findViewById(R.id.submit_sign_up_id).setEnabled(false);
                String firstName = name.getText().toString().trim();
                String family = lastName.getText().toString().trim();
                String student = studentNumber.getText().toString().trim();
                String introduceCode = introduceInput.getText().toString().trim();
                if (firstName.isEmpty() || family.isEmpty() || student.isEmpty()) {
                    Toast.makeText(this, "check input", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isSelected) {
                    NewUserForServer user = new NewUserForServer(firstName, family, ""
                            , isMen, introduceCode, student);
                    ServerClass.makeNewUser(this, user);
                } else {
                    findViewById(R.id.submit_sign_up_id).setEnabled(true);
                    Toast.makeText(this, "select gender", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.FROM_NOTIFICATION, false);
        startActivity(intent);
        finish();
    }

    public void enable() {
        findViewById(R.id.submit_sign_up_id).setEnabled(true);
    }

}
