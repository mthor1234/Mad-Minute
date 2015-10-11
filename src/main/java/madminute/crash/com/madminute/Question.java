package madminute.crash.com.madminute;


public class Question {

    private int user_selected_integer, random_integer, user_answer, actual_answer;
    private String operator, formatted_question;



    public Question(String operator, int random_integer, int user_selected_integer) {
        this.operator = operator;
        this.random_integer = random_integer;
        this.user_selected_integer = user_selected_integer;

        solution_to_question();
        format_question();

    }


    private void format_question(){
        formatted_question = "   " + user_selected_integer + "\n" + operator + " " + random_integer;
    }



    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getRandom_integer() {
        return random_integer;
    }

    public void setRandom_integer(int random_integer) {
        this.random_integer = random_integer;
    }

    public int getUser_selected_integer() {
        return user_selected_integer;
    }

    public void setUser_selected_integer(int user_selected_integer) {
        this.user_selected_integer = user_selected_integer;
    }

    public String getFormatted_question() {
        return formatted_question;
    }

    // ToDo: Fix for negative numbers... and either divide to get integers or else tell the user which decimal to round to...Also watch out for dividing by 0!
    public void solution_to_question(){

        if(operator.equalsIgnoreCase("+")) {
            actual_answer = user_selected_integer + random_integer;
        }
        else if(operator.equalsIgnoreCase("-")){
            actual_answer = user_selected_integer - random_integer;
        }
        else if(operator.equalsIgnoreCase("x")){
            actual_answer = user_selected_integer * random_integer;
        }

        // Need to modify this so it creates integers only or else let the user know which decimal I am going to round off to
        else if(operator.equalsIgnoreCase("/")){
            actual_answer = user_selected_integer / random_integer;
        }

    }

    public void setFormatted_question(String formatted_question) {
        this.formatted_question = formatted_question;
    }

    public int getActual_answer() {
        return actual_answer;
    }

    public void setActual_answer(int actual_answer) {
        this.actual_answer = actual_answer;
    }

    public int getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(int user_answer) {
        this.user_answer = user_answer;
    }
}
