package com.danodic.jao.renderer;

import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.extractor.IExtractor;
import com.danodic.jao.model.DataTypeModel;

public interface IRenderer {
	public void setDataType(DataTypeModel dataType, IExtractor extractor);
	public void render(JaoLayer layer, Object ... args);
	public void initialize(Object ... args);
        public void dispose();
        public IRenderer clone();
        public void debug();
        public boolean isText();
        public IText getTextRenderable();
}
