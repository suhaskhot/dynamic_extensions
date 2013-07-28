
package edu.common.dynamicextensions.nutility;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.SkipCondition;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;

public class FormDataUtility {
	public static int getEmptyControlCount(FormData formData) {
		int countrolCount = 0;
		evaluateSkipLogic(formData);

		for (Control control : formData.getContainer().getControls()) {
			ControlValue fieldValue = formData.getFieldValue(control.getName());
			if (control instanceof Label) {
				continue;
			}
			if (!fieldValue.isHidden() && !fieldValue.isReadOnly() && fieldValue.isEmpty() == true) {
				countrolCount++;
			}
		}
		return countrolCount;
	}

	public static int getFilledControlCount(FormData formData) {
		int countrolCount = 0;
		evaluateSkipLogic(formData);

		for (Control control : formData.getContainer().getControls()) {
			ControlValue fieldValue = formData.getFieldValue(control.getName());

			if (!fieldValue.isHidden() && !fieldValue.isReadOnly()) {
				countrolCount++;
			}
		}
		return countrolCount;
	}
	
	
	//
	// TODO: Optimization
	//
	public static void evaluateSkipLogic(FormData formData) {
		Container form = formData.getContainer();
				
		evaluateSkipLogic(formData, getFormRules(form, form.getSkipRules()));
		
		for (ControlValue cv : formData.getFieldValues()) {
			if (cv.getControl() instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)cv.getControl();
				List<SkipRule> formRules = getFormRules(sfCtrl.getSubContainer(), form.getSkipRules());
				
				List<FormData> subFormDataList = (List<FormData>)cv.getValue();
				for (FormData subFormData : subFormDataList) {
					evaluateSkipLogic(subFormData, formRules);
				}
			}
		}
	}
	
	private static void evaluateSkipLogic(FormData formData, List<SkipRule> rules) {
		for (SkipRule rule : rules) {
			rule.evaluate(formData);
		}		
	}
	
	private static List<SkipRule> getFormRules(Container form, List<SkipRule> rules) {
		List<SkipRule> currentFormRules = new ArrayList<SkipRule>();
		
		for (SkipRule rule : rules) {
			boolean add = true;
			for (SkipCondition cond : rule.getConditions()) {
				if (form != cond.getSourceControl().getContainer()) {					
					add = false;
					break;
				} 
			}
			
			if (add) {
				currentFormRules.add(rule);
			} 
		}
		
		return currentFormRules;		
	}
	
	
//	public static void evaluateSkipLogic(FormData formData) {
//		evaluateSkipLogic(formData, formData, null, true);
//	}
//
//	/**
//	 * Evaluates skip logic for all the controls 
//	 * @param tgtCtlFormData containing target control value
//	 * @param srcCtlFormData containing source control value
//	 * @param evaulateSubform to decide whether next level subform skip logic is to be evaluated or not
//	 */
//	private static void evaluateSkipLogic(FormData tgtCtlFormData, FormData srcCtlFormData, Integer rowNumber,
//			boolean evaulateSubform) {
//		for (Control control : tgtCtlFormData.getContainer().getControls()) {
//			ControlValue controlValue = tgtCtlFormData.getFieldValue(control.getName());
//
//			if (control.isSkipLogicTargetControl()) {
//				control.evaluateSkipLogic(controlValue, srcCtlFormData, rowNumber);
//			}
//
//			if (control instanceof SubFormControl && evaulateSubform) {
//				evaluateSubformSkipLogic((SubFormControl) control, controlValue, srcCtlFormData);
//			}
//		}
//
//	}
//
//	private static void evaluateSubformSkipLogic(SubFormControl subFormControl, ControlValue controlValue,
//			FormData srcCtlFormData) {
//		if (controlValue.getValue() != null && ((List<FormData>) controlValue.getValue()).size() > 0) {
//			List<FormData> formDatas = (List<FormData>) controlValue.getValue();
//
//			if (subFormControl.isCardinalityOneToMany()) {
//				int i = 0;
//				for (FormData data : formDatas) {
//					evaluateSkipLogic(data, srcCtlFormData, i++, false);
//				}
//			} else {
//				evaluateSkipLogic(formDatas.get(0), srcCtlFormData, null, false);
//			}
//
//		}
//	}
}
