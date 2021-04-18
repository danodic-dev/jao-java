package com.danodic.jao.renderer;

/**
 * This interface can be applied to renderers that support writing text on them.
 * It has to provide an ID/name for the writable element so it can be referred
 * to later from the jao file.
 *
 * @author danodic
 */
public interface IText {

    /**
     * Returns the ID of this text renderer. This id does not have to be unique.
     * If the same ID is used at the same time, text will be applied to all
     * using the same ID.
     *
     * @return The id of the text renderer.
     */
    public String getTextId();

    /**
     * Sets the text of the renderer.
     *
     * @param value Value to be set in the renderer.
     */
    public void setText(String value);

}
