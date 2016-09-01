package curiosamente.com.app.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Manu on 7/8/16.
 */
public class Player {
    private String id;
    private String name;
    private String lastName;
    private boolean isPrizeClaimed;
    private boolean isWinner;

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isPrizeClaimed() {
        return isPrizeClaimed;
    }

    public void setPrizeClaimed(boolean prizeClaimed) {
        isPrizeClaimed = prizeClaimed;
    }

    @Override
    public String toString() {
        String jsonFormat = null;
        try {
            jsonFormat = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonFormat;
    }
}
