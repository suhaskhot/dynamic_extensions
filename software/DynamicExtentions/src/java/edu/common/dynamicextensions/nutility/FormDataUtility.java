package edu.common.dynamicextensions.nutility;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;

public class FormDataUtility {
	public static int getEmptyControlCount(FormData formData) {
		int countrolCount = 0;
		evaluateSkipLogic(formData);

		for (Control control : formData.getContainer().getControls()) {
			ControlValue fieldValue = formData.getFieldValue(control.getName());
			if (control instanceof Label || control instanceof PageBreak) {
				continue;
			}
			if (!fieldValue.isHidden() && !fieldValue.isReadOnly() && fieldValue.isEmpty()) {
				countrolCount++;
			}
		}
		return countrolCount;
	}

	public static int getFilledControlCount(FormData formData) {
		int countrolCount = 0;
		evaluateSkipLogic(formData);

		for (Control control : formData.getContainer().getControls()) {
			if (control instanceof Label || control instanceof PageBreak) {
				continue;
			}
			ControlValue fieldValue = formData.getFieldValue(control.getName());

			if (!fieldValue.isHidden() && !fieldValue.isReadOnly()) {
				countrolCount++;
			}
		}
		return countrolCount;
	}
	
	public static int getCompletionStatus(FormData formData) {
		int controlsCount = getFilledControlCount(formData);
		int emptyCtrlCount = getEmptyControlCount(formData);
		int percentage = Math.round(100 * (controlsCount - emptyCtrlCount)/ controlsCount);
		return percentage;
	}
	
	public static void evaluateSkipLogic(FormData formData) {
		SkipRulesEvaluator evaluator = new SkipRulesEvaluator();
		evaluator.evaluate(formData);
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
