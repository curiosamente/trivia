package curiosamente.com.app.model;

/**
 * Created by Manu on 7/8/16.
 */
public class Answer {

    private Player player;
    private String idQuestion;
    private String answer;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

}
