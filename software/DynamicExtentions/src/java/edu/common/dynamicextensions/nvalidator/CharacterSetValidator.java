/**
 *
 */

package edu.common.dynamicextensions.nvalidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;

public class CharacterSetValidator implements RuleValidator {

	public void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException {
		String valueString = controlValue.toString();

		if (valueString == null || valueString.isEmpty()) {
			return;
		}
		ArrayList<String> placeHolders = new ArrayList<String>();
		Control control = controlValue.getControl();

		BufferedReader reader = new BufferedReader(new StringReader(valueString));

		try {
			int character = reader.read();

			while (character != -1) {
				boolean isInvalid = ((character < 32 && character != 9 && character != 10 && character != 13)
						|| (character > 126 && character < 160) || character > 255);

				if (isInvalid) {
					placeHolders.add(control.getCaption());
					placeHolders.add(String.valueOf((char) character));
					throw new DynamicExtensionsValidationException("Validation failed", null,
							"dynExtn.validation.characterSet", placeHolders);
				}
				character = reader.read();
			}
		} catch (IOException e) {
			throw new DynamicExtensionsValidationException("Validation failed", e);
		}
	}
}
