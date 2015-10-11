package madminute.crash.com.madminute;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;

import java.util.ArrayList;

public class MainActivity extends Activity {

    FragmentManager fm = getFragmentManager();
    MadMinuteTest fragment_madminute_test;



    NumberPicker numberPicker_1;
    CheckBox cb_addition, cb_subtraction, cb_multiplication, cb_division;
    CheckBox cb_positive, cb_negative;

    Button btn_start;

    MainActivity mainActivity = this;


    ArrayList<String> operators = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_main);

        System.out.println("OnCreate!");

        numberPicker_1 = (NumberPicker) findViewById(R.id.numberPicker);

        cb_addition = (CheckBox) findViewById(R.id.cb_addition);
        cb_subtraction = (CheckBox)  findViewById(R.id.cb_subtraction);
        cb_multiplication = (CheckBox) findViewById(R.id.cb_multiplication);
        cb_division = (CheckBox) findViewById(R.id.cb_division);

        cb_positive = (CheckBox) findViewById(R.id.cb_positive);
        cb_negative = (CheckBox) findViewById(R.id.cb_negative);

        btn_start = (Button) findViewById(R.id.btn_start);

        numberPicker_1.setMinValue(1);
        numberPicker_1.setMaxValue(25);
        numberPicker_1.setWrapSelectorWheel(false);



        // Listens to the numberpicker value change
        numberPicker_1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });


        // User presses Start
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the mad minute! Use intent or swap fragment

                MadMinute_Test_Activity fragment_madminute_test = new MadMinute_Test_Activity();

                fragment_madminute_test.setInteger_to_practice(numberPicker_1.getValue());
//                fragment_madminute_test.setActivity_Fragment(mainActivityFragment);
                fragment_madminute_test.seconds = 15;

//                activity.fragment_madminute_test.setInteger_to_practice(numberPicker_1.getValue());

                if(cb_positive.isChecked()) {
                    operators.add("+");
                }
                if(cb_subtraction.isChecked()){
                    operators.add("-");
                }
                if(cb_multiplication.isChecked()) {
                    operators.add("x");
                }
                if(cb_division.isChecked()){
                    operators.add("/");
                }

                fragment_madminute_test.setOperator(operators);
                fragment_madminute_test.setUserInt(numberPicker_1.getValue());

                Intent intent = new Intent(mainActivity, fragment_madminute_test.getClass());
                startActivity(intent);



            }
        });

        cb_addition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If all check box operations not checked, then prevent cb_addition from being unchecked
                if (!cb_subtraction.isChecked() && !cb_multiplication.isChecked() && !cb_division.isChecked()) {
                    cb_addition.setChecked(true);
                }
            }
        });

        cb_subtraction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If all other check box operations not checked, then prevent cb_subtraction from being unchecked
                if( !cb_addition.isChecked() && !cb_multiplication.isChecked() && !cb_division.isChecked()){
                    cb_subtraction.setChecked(true);
                }
            }
        });

        cb_multiplication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If all other check box operations not checked, then prevent cb_multiplication from being unchecked
                if( !cb_subtraction.isChecked() && !cb_addition.isChecked() && !cb_division.isChecked()){
                    cb_multiplication.setChecked(true);
                }
            }
        });

        cb_division.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If all other check box operations not checked, then prevent cb_division from being unchecked
                if( !cb_subtraction.isChecked() && !cb_addition.isChecked() && !cb_multiplication.isChecked()){
                    cb_division.setChecked(true);
                }
            }
        });


        cb_positive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If cb_negative not checked, then prevent cb_positive from being unchecked
                if( !cb_negative.isChecked()){
                    cb_positive.setChecked(true);
                }
            }
        });


        cb_negative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If cb_positive not checked, then prevent cb_negative from being unchecked
                if( !cb_positive.isChecked()){
                    cb_negative.setChecked(true);
                }
            }
        });



    }


    public void restartTest(){
//        MadMinuteTest fragment_madminute_test = new MadMinuteTest();
        fragment_madminute_test.setInteger_to_practice(numberPicker_1.getValue());
        fragment_madminute_test.seconds = 15;

        if(cb_positive.isChecked()) {
            operators.add("+");
        }
        if(cb_subtraction.isChecked()){
            operators.add("-");
        }
        if(cb_multiplication.isChecked()) {
            operators.add("x");
        }
        if(cb_division.isChecked()){
            operators.add("/");
        }

        fragment_madminute_test.setOperator(operators);
        fragment_madminute_test.setUserInt(numberPicker_1.getValue());

    }
}
