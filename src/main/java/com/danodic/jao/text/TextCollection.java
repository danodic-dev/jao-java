package com.danodic.jao.text;

import com.danodic.jao.renderer.IText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps a list of IText instances added as layers in the main Jao instance.
 * It makes sure it keeps all instances with the same id for multiple-layer
 * text updates.
 * @author danodic
 */
public class TextCollection {
    
    private static final Map<String, List<IText>> instances = new HashMap<>();
    
    public static void addITextInstance(IText instance, String id) {
        if(!instances.containsKey(id)) {
            instances.put(id, new ArrayList<>());
        }
        instances.get(id).add(instance);
    }
    
    public static List<IText> getInstances(String id) {
        if(!instances.containsKey(id)) {
            throw new RuntimeException(String.format("Could not find a text layer for id '%s.", id));
        }
        return instances.get(id);
    }
    
}
