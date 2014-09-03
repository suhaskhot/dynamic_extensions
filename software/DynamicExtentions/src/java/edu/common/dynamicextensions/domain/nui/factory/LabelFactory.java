package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.Label;
import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class LabelFactory extends AbstractControlFactory {
	
	public static LabelFactory getInstance() {
		return new LabelFactory();
	}

	@Override
	public String getType() {
		return "label";
	}
	
	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		Label label = new Label();
		setControlProps(label, ele, row, xPos);
				
		String heading = getTextValue(ele, "heading");
		String note = getTextValue(ele, "note");
		if (heading != null && !heading.trim().isEmpty()) {
			label.setCaption(heading);
			label.setHeading(true);
		} else if (note != null && !note.trim().isEmpty()) {
			label.setCaption(note);
			label.setHeading(false);
			label.setNote(true);			
		}
		
		return label;
	}
}
