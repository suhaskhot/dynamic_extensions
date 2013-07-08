
package edu.common.dynamicextensions.nvalidator;

import java.util.Map;

import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.util.logger.Logger;

public class DateValidator implements RuleValidator {

	private static Map<String, String> datePatternVsRegexMap;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(DateValidator.class);

	static {

		try {
			datePatternVsRegexMap = DynamicExtensionsUtility.getAllValidDatePatterns();
		} catch (DynamicExtensionsSystemException e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * @see edu.common.dynamicextensions.validation.ValidatorRuleInterface#validate(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.lang.Object, java.util.Map)
	 * @throws DynamicExtensionsValidationException
	 */
	public void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException {
		DatePicker control = (DatePicker) controlValue.getControl();

		try {
			control.fromString((String) controlValue.getValue());
		} catch (Exception parseException) {
			ValidatorUtil.reportInvalidInput(control.getCaption(), control.getFormat(), "dynExtn.validation.Date");
		}

	}

	/**
	 * It will read the ValidDatePatterns.XML & verify that the date patterns
	 * specified in the ApplicationResources.properties Files is in the given patterns.
	 * else will throw the Exception. It will also read the regex defined for each pattern
	 * & keep it for future date validations using this class.
	 */
	public static void validateGivenDatePatterns() {

		if (!(datePatternVsRegexMap.containsKey(ProcessorConstants.DATE_ONLY_FORMAT)
				&& datePatternVsRegexMap.containsKey(ProcessorConstants.DATE_TIME_FORMAT)
				&& datePatternVsRegexMap.containsKey(ProcessorConstants.MONTH_YEAR_FORMAT) && datePatternVsRegexMap
					.containsKey(ProcessorConstants.YEAR_ONLY_FORMAT))) {
			throw new IllegalArgumentException("Invalid date pattern specified in the Application resource file");
		}

	}
}