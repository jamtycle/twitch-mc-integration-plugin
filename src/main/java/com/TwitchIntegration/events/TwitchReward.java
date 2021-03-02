package com.TwitchIntegration.events;

public class TwitchReward {
    private String id;
    private String title;
    private String prompt;
    private Integer cost;

    public TwitchReward(String _id, String _title, String _prompt, Integer _cost){
        this.id = _id;
        this.title = _title;
        this.prompt = _prompt;
        this.cost = _cost;
    }

    //#region Getters
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getPrompt() {
        return prompt;
    }
    public Integer getCost() {
        return cost;
    }
    //#endregion

    //#region Setters
    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }
    //#endregion
}