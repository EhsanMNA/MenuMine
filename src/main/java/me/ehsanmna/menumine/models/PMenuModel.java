package me.ehsanmna.menumine.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PMenuModel {

    UUID owner;
    Map<String,MenuModel> models = new HashMap<>();


    public PMenuModel(UUID owner){
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public MenuModel getModel(String id){
        return models.get(id);
    }

    public void addModel(MenuModel model){
        model.setCopy(false);
        models.put(model.getId(),model);
    }

    public void removeModel(String id){
        models.remove(id);
    }

    public Map<String, MenuModel> getModels() {
        return models;
    }

    @Override
    public String toString() {
        return "PMenuModel{" +
                "owner=" + owner +
                ", models=" + models +
                '}';
    }
}
