package madminute.crash.com.madminute;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
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

// TODO: Add fragment transitions
// TODO: Fix crashing when rotation/closing the app

public class MadMinuteTest extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private TextView tv_countdown;
    private ScrollView scroll;

    private static final String FORMAT = "%02d";

    private CountDownTimer countDownTimer;

    int seconds = 15;

    private int integer_to_practice;
    private ArrayList<String> operators;

    private ArrayList<String> answers = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<String> userAnswers = new ArrayList<>();

    private int user_answer_index = 0;

    public LinearLayout test_holder;

    private Button retry, menu;

    private MainActivityFragment activity_Fragment;
    private boolean paused = false;




//    public static MadMinuteTest newInstance(String param1, String param2) {
//        MadMinuteTest fragment = new MadMinuteTest();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public MadMinuteTest() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            System.out.println("Getting Saved Instance State!");


        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setRetainInstance(true);

        if (savedInstanceState != null) {

            System.out.println("Getting Saved Instance State!");

            operators = savedInstanceState.getStringArrayList("Operators");
            integer_to_practice = savedInstanceState.getInt("Integer to Practice");
//            seconds = savedInstanceState.getInt("Seconds");

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mad_minute_test, container, false);
        scroll = (ScrollView) view.findViewById(R.id.scrollview);

        tv_countdown = (TextView) view.findViewById(R.id.tv_timer);

        test_holder = (LinearLayout) view.findViewById(R.id.minute_test_holder);

        retry = (Button) view.findViewById(R.id.btn_retry);
        menu = (Button) view.findViewById(R.id.btn_menu);

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
                getFragmentManager().popBackStack();
            }
        });


        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
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

                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        System.out.println("Fragment is saving Instance State!");
        System.out.println("Operators Size: " + operators.size());
        System.out.println("Is Integer to practice Null: " + integer_to_practice == null);
        System.out.println("Is Seconds Null: " + seconds == null);

        outState.putStringArrayList("Operators", operators);
        outState.putInt("Integer to practice", integer_to_practice);
        outState.putInt("Seconds", seconds);

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

            // Error Out of Bounds
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
        getFragmentManager().popBackStack();
        activity_Fragment.restartTest();
    }

    public MainActivityFragment getActivity_Fragment() {
        return activity_Fragment;
    }

    public void setActivity_Fragment(MainActivityFragment activity_Fragment) {
        this.activity_Fragment = activity_Fragment;
    }
}
