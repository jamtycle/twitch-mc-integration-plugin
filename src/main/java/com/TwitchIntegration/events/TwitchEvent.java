package com.TwitchIntegration.events;

//If you need documentation for reading this class then you maybe are too noob.
public class TwitchEvent {
    private String user_name;
    private String user_input;
    private TwitchReward reward;

    public TwitchEvent(String _user_name, String _user_input, TwitchReward _reward){
        this.user_name = _user_name;
        this.user_input = _user_input;
        this.reward = _reward;
    }

    //#region Getters
    public String getUser_name() {
        return user_name;
    }
    public String getUser_input() {
        return user_input;
    }
    public TwitchReward getReward() {
        return reward;
    }
    //#endregion

    //#region Setters
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public void setUser_input(String user_input) {
        this.user_input = user_input;
    }
    public void setReward(TwitchReward reward) {
        this.reward = reward;
    }
    //#endregion
}
