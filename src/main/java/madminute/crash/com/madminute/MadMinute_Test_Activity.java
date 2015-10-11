package madminute.crash.com.madminute;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MadMinute_Test_Activity extends Activity{


    private TextView tv_countdown;
    private ScrollView scroll;

    private static final String FORMAT = "%02d";

    private CountDownTimer countDownTimer;

    int seconds = 15;

    private int integer_to_practice;
    private ArrayList<String> operators = new ArrayList<>();

    private ArrayList<String> answers = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<String> userAnswers = new ArrayList<>();

    private int user_answer_index = 0;

    public LinearLayout test_holder;

    private Button retry, menu;

    public Main activity;
    private boolean paused = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mad_minute_test);


//        operators = getIntent().getStringArrayListExtra("Operators");
        operators =  (ArrayList<String>)getIntent().getExtras().get("Operators");
//        integer_to_practice = getIntent().getIntExtra("User_Int", 0);

        integer_to_practice = getIntent().getExtras().getInt("User_Int");



        scroll = (ScrollView) findViewById(R.id.scrollview);

        tv_countdown = (TextView) findViewById(R.id.tv_timer);

        test_holder = (LinearLayout) findViewById(R.id.minute_test_holder);

        retry = (Button) findViewById(R.id.btn_retry);
        menu = (Button) findViewById(R.id.btn_menu);

        generateQuestions(test_holder);




        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reset();

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    @Override
    public void onPause() {
        super.onPause();

        paused = true;
        System.out.println("Paused.... Seconds: " + seconds);

        String str = tv_countdown.getText().subSequence(0, 2).toString().replaceAll("\\D+","");
        seconds = Integer.parseInt(str);

        countDownTimer.cancel();

        paused = false;
    }

    @Override
    public void onResume() {
        super.onResume();


        System.out.println("On Resume");

        // Start the timer when fragment is resumed
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                tv_countdown.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

//                seconds = Integer.parseInt(String.format(FORMAT, TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
//                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                System.out.println("Seconds: " + seconds);


                if(paused == true){
                    System.out.println("Seconds not equal to 0");

                }
                else{

                    answers.clear();

                    System.out.println("Calling On Finish!");
                    tv_countdown.setText("0:00");
                    getAnswers(test_holder);
                    checkAnswers();
                    markAnswers(test_holder);

                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    menu.requestFocus();

                    tv_countdown.setVisibility(View.GONE);

                    menu.setVisibility(View.VISIBLE);
                    retry.setVisibility(View.VISIBLE);

                    scroll.fullScroll(View.FOCUS_DOWN);
                }
            }
        }.start();
    }

    public void clearEditTexts(){

        clearForm(test_holder);

    }

    public void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }


    private void generateQuestions(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {

            View view = group.getChildAt(i);

            if (view instanceof TextView && !(view instanceof EditText) && !(view instanceof Button)) {


                Random generator = new Random();
                int random_number = generator.nextInt(10) + 1;
                int randomOperator;

                randomOperator = generator.nextInt(operators.size());


                questions.add(new Question(operators.get(randomOperator), random_number, integer_to_practice));
                ((TextView) view).setText(questions.get(questions.size() - 1).getFormatted_question());

            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0)) {

                generateQuestions((ViewGroup) view);
            }
        }

    }

    private void getAnswers(ViewGroup group){
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {

            View view = group.getChildAt(i);

            if ( view instanceof EditText) {
                System.out.println("Adding answer!");
                answers.add(((EditText) view).getText().toString());
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                getAnswers((ViewGroup) view);
        }
    }

    public void checkAnswers(){


        for(int i = 0; i < answers.size(); i++){

            if( answers.get(i).equalsIgnoreCase( Integer.toString(questions.get(i).getActual_answer()) ) ){
                System.out.println(questions.get(i).getFormatted_question() + ": User's answer: " + answers.get(i) + " Correct Answer: " + questions.get(i).getActual_answer() + " Correct!");
                userAnswers.add("Correct");
            }
            else{
                System.out.println(questions.get(i).getFormatted_question() + ": User's answer: " + answers.get(i) + " Correct Answer: " + questions.get(i).getActual_answer() + " Wrong!");
                userAnswers.add("Wrong");

            }
        }
    }

    private void markAnswers(ViewGroup group){

        for (int i = 0, count = group.getChildCount(); i < count; ++i) {

            View view = group.getChildAt(i);


            if(user_answer_index >= userAnswers.size()){
                return;
            }

            if ( view instanceof ImageView) {
                System.out.println("Adding answer!");

                System.out.println("User Answer Index: " + user_answer_index);


                if(userAnswers.get(user_answer_index).equalsIgnoreCase("Correct") ){
                    ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.check_mark));
                    view.setVisibility(View.VISIBLE);
                }else{
                    ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.wrong_mark));
                    view.setVisibility(View.VISIBLE);

                }

                user_answer_index++;

            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                markAnswers((ViewGroup) view);
        }
    }

    private void unMarkAnswers(ViewGroup group){

        for (int i = 0, count = group.getChildCount(); i < count; ++i) {

            View view = group.getChildAt(i);


//            if(user_answer_index >= userAnswers.size()){
//                return;
//            }

            if ( view instanceof ImageView) {
                view.setVisibility(View.INVISIBLE);

            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                unMarkAnswers((ViewGroup) view);
        }
    }


    public int getInteger_to_practice() {
        return integer_to_practice;
    }

    public void setInteger_to_practice(int integer_to_practice) {
        this.integer_to_practice = integer_to_practice;
    }

    public ArrayList<String> getOperator() {
        return operators;
    }

    public void setOperator(ArrayList<String> operator) {
        this.operators = operator;
    }

    public void setUserInt(int integer_to_practice) {
        this.integer_to_practice = integer_to_practice;
    }

    public void reset(){
        answers.clear();
        userAnswers.clear();
        questions.clear();

        clearEditTexts();
        unMarkAnswers(test_holder);
        generateQuestions(test_holder);

        tv_countdown.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);

        seconds = 60;

        countDownTimer = new CountDownTimer(seconds * 1000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                tv_countdown.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

//                seconds = Integer.parseInt(String.format(FORMAT, TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
//                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                System.out.println("Seconds: " + seconds);


                if(paused == true){
                    System.out.println("Seconds not equal to 0");

                }
                else{

                    answers.clear();

                    System.out.println("Calling On Finish!");
                    tv_countdown.setText("0:00");
                    getAnswers(test_holder);
                    checkAnswers();
                    markAnswers(test_holder);

                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    menu.requestFocus();

                    tv_countdown.setVisibility(View.GONE);

                    menu.setVisibility(View.VISIBLE);
                    retry.setVisibility(View.VISIBLE);

                    scroll.fullScroll(View.FOCUS_DOWN);
                }
            }
        }.start();





//        activity.restartTest();
    }




}
