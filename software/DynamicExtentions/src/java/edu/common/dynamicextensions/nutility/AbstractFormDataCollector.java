
package edu.common.dynamicextensions.nutility;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;

/**
 * @author Kunal Kamble
 *
 */
public abstract class AbstractFormDataCollector implements FormDataCollector {

	/**
	 * @param container
	 * @param rowNumber
	 * @param collectsubformData flag for whether to collect sub form data or not
	 * @return
	 */
	protected void collectFormData(FormData formData, Integer rowId, boolean collectsubformData) {

		for (Control control : formData.getContainer().getControls()) {
			if (control instanceof SubFormControl) {
				
				if (!collectsubformData) {
					continue;
				}
				SubFormControl subformControl = (SubFormControl) control;

				Object controlValue = formData.getFieldValue(subformControl.getName()).getValue();
				List<FormData> subformDataList = (controlValue == null
						? new ArrayList<FormData>()
						: (List<FormData>) controlValue);
				if (subformControl.isCardinalityOneToMany()) {

					for (int i = 1; i <= getRowCount(subformControl.getSubContainer().getId()); i++) {
						FormData subformData = null;
						if (subformDataList.size() < i) {
							subformData = new FormData(subformControl.getSubContainer());
							subformDataList.add(subformData);
						} else {
							subformData = subformDataList.get(i - 1);
						}

						collectSubformData(subformData, subformControl, i);
					}
				} else {
					FormData subformData = null;
					if (subformDataList.size() > 0) {
						subformData = subformDataList.get(0);
					} else {

						subformData = new FormData(subformControl.getSubContainer());
						subformDataList.add(subformData);
					}

					collectSubformData(subformData, subformControl, null);
				}

				formData.addFieldValue(new ControlValue(subformControl, subformDataList));

			} else if (control instanceof NumberField && ((NumberField) control).isCalculated()) {
				evaluateFormula(control, formData, rowId);
			} else {
				collectControlValue(formData, control, rowId);
			}
		}
	}

	protected abstract void evaluateFormula(Control control,FormData formData, Integer rowId);

	protected void collectSubformData(FormData subformData, SubFormControl subFormControl, Integer rowId) {
		collectFormData(subformData, rowId, false);
	}

	/**
	 * @param containerId
	 * @return total number of rows for a given containerId of a sub form
	 */
	abstract int getRowCount(Long containerId);

	protected abstract void collectControlValue(FormData formData, Control control, Integer rowId);
}
