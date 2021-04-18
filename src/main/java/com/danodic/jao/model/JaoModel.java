package com.danodic.jao.model;

import java.util.List;

public class JaoModel {

    private List<LayerModel> layers;
    private Boolean loop = false;

    public List<LayerModel> getLayers() {
        return layers;
    }

    public void setLayers(List<LayerModel> layers) {
        this.layers = layers;
    }

    public Boolean getLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

}
