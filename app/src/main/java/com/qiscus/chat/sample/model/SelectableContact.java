package com.qiscus.chat.sample.model;

/**
 * Created by omayib on 05/11/17.
 */

public class SelectableContact extends User {
    private boolean isSelected = false;

    public SelectableContact(User user, boolean isSelected){
        super(user.getId(), user.getName(), user.getEmail(), user.getJob());
        this.setAvatarUrl(user.getAvatarUrl());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
