
package edu.common.dynamicextensions.domain.nui;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.common.exception.RuntimeException;

public class ListBox extends SelectControl {

	private static final long serialVersionUID = 9190611588889454148L;

	private int noOfRows;
	
	private boolean autoCompleteDropdownEnabled;

	private int minQueryChar = 3;

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}
	
	public boolean isAutoCompleteDropdownEnabled() {
		return autoCompleteDropdownEnabled;
	}
	
	public void setAutoCompleteDropdownEnabled(boolean autoCompleteDropdownEnabled) {
		this.autoCompleteDropdownEnabled = autoCompleteDropdownEnabled;
	}
	
	public int getMinQueryChars() {
		return minQueryChar;
	}
	
	public void setMinQueryChars(int minQueryChars) {
		this.minQueryChar = minQueryChars;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + noOfRows;
		result = prime * result + (autoCompleteDropdownEnabled ? 1231 : 1237);
		result = prime * result + minQueryChar;		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		ListBox other = (ListBox) obj;
		if (noOfRows != other.noOfRows ||
			autoCompleteDropdownEnabled != other.autoCompleteDropdownEnabled ||
			minQueryChar != other.minQueryChar) {		
			return false;
		}

		return true;
	}

	@Override
	protected String render(String controlName, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter) {
		StringBuilder htmlString = new StringBuilder(193);
		
		try {
			List<PermissibleValue> pvList = getPVList(
					ControlsUtility.getFormattedDate(contextParameter.get(ContextParameter.ACTIVATION_DATE)),
					controlValue);

			String identifier = "";
			if (getId() != null) {
				identifier = getId().toString();
			}

			String strMultiSelect = getMultiselectString();

			htmlString.append("<SELECT ");
			htmlString.append(strMultiSelect).append(" size=").append(noOfRows).append(" name='").append(controlName)
					.append("' onchange=\"");

			htmlString.append(getOnchangeServerCall(controlName));

			if (controlValue.isReadOnly()) {
				htmlString.append(" \" disabled='").append(ProcessorConstants.TRUE).append("'style='background:white'");
			}
			htmlString.append("\">");

			if (pvList != null && !pvList.isEmpty()) {
				for (PermissibleValue pv : pvList) {
					String pvValue = pv.getValue();
					
					if (isPVSelected(controlValue, pv)) {
						htmlString.append("<OPTION title='").append(pvValue).append("' value='").append(pvValue)
								.append("' selected>")
								.append(DynamicExtensionsUtility.replaceHTMLSpecialCharacters(pvValue));
					} else {
						htmlString.append("<OPTION value='").append(pvValue).append("'>")
								.append(DynamicExtensionsUtility.replaceHTMLSpecialCharacters(pvValue));
					}
				}
			}
			htmlString.append("</SELECT>");

			// generate this type of html if multiselect is to be implemented
			// using an auto complete drop down and list box together.
			if (autoCompleteDropdownEnabled) {
				String coordId = "coord_" + controlName;
				String protocolCoordId = "protocolCoordId_" + controlName;

				StringBuilder multSelWithAutoCmpltHTML = new StringBuilder(42);

				String comboInnerScript = generateScriptTagForAutoComplete(identifier, coordId, controlName,
						contextParameter);
				multSelWithAutoCmpltHTML
						.append("<script defer='defer'>Ext.onReady(function(){")
						.append(comboInnerScript)
						.append("combo.on(\"expand\", function() {if(Ext.isIE || Ext.isIE7 || Ext.isSafari){combo.list.setStyle(\"width\", \"240\");combo.innerList.setStyle(\"width\", \"240\");}else{combo.list.setStyle(\"width\", \"auto\");combo.innerList.setStyle(\"width\", \"auto\");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});</script>\n")
						.append("<br><table border=\"0\" width=\"400\">\n")
						.append("\t<tr>\n")
						.append("\t\t<td width=\"35%\" class=\"black_ar_new1\" valign=\"TOP\">\n")
						.append("\t\t\t<input type='text' id='" + coordId + "' name='" + coordId
								+ "' value =' ' size='20'/>\n")
						.append("\t\t</td>\n\n")
						.append("\t\t<td class=\"black_ar_new1\" width=\"20%\" align=\"center\" valign=\"TOP\">\n")
						.append("\t\t\t<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n")
						.append("\t\t\t\t<tr>\n")
						.append("\t\t\t\t\t<td height=\"22\" align=\"center\" valign=\"TOP\">\n")
						.append("\t\t\t\t\t\t<div id=\"addLink\">\n")
						.append("\t\t\t\t\t\t\t<a href=\"#\" onclick=\"isDataChanged();moveOptions('")
						.append(coordId)
						.append("','")
						.append(protocolCoordId)
						.append("', 'add');")
						.append(getOnchangeServerCall(controlName) + "\">\n")
						.append("\t\t\t\t\t\t\t\t<img src=\"images/b_add_inact.gif\" alt=\"Add\" height=\"18\" border=\"0\" align=\"absmiddle\"/>\n")
						.append("\t\t\t\t\t\t\t</a>\n")
						.append("\t\t\t\t\t\t</div>\n")
						.append("\t\t\t\t\t</td>\n")
						.append("\t\t\t\t</tr>\n\n")
						.append("\t\t\t\t<tr>\n")
						.append("\t\t\t\t\t<td height=\"22\" align=\"center\">\n")
						.append("\t\t\t\t\t\t<div id=\"removeLink\">\n")
						.append("\t\t\t\t\t\t\t<a href=\"#\" onclick=\"isDataChanged();moveOptions('")
						.append(protocolCoordId)
						.append("','")
						.append(coordId)
						.append("', 'edit');")
						.append(getOnchangeServerCall(controlName) + "\">\n")
						.append("\t\t\t\t\t\t\t\t<img src=\"images/b_remove_inact.gif\" alt=\"Remove\" height=\"18\" border=\"0\" align=\"absmiddle\"/>\n")
						.append("\t\t\t\t\t\t\t</a>\n").append("\t\t\t\t\t\t</div>\n").append("\t\t\t\t\t</td>\n")
						.append("\t\t\t\t</tr>\n").append("\t\t\t</table>\n").append("\t\t</td>\n\n")
						.append("\t\t<td width=\"50%\" align=\"center\" class=\"black_ar_new1\">\n")
						.append("\t\t\t<SELECT id=\"").append(protocolCoordId).append("\" name=\"").append(controlName)
						.append("\" size=\"4\" multiple=\"true\" style=\"width:170px\">");

				if (controlValue.getValue() != null) {
					
					for (String pv : (String[]) controlValue.getValue()) {
						String encodedPvString = DynamicExtensionsUtility.getEscapedStringValue(pv);
						multSelWithAutoCmpltHTML.append("<OPTION title='").append(encodedPvString).append("' value='")
								.append(encodedPvString).append("' selected>").append(pv);
					}
				} else if (getDefaultValue() != null) {
					String pv = getDefaultValue().getValue();
					String encodedPvString = DynamicExtensionsUtility.getEscapedStringValue(pv);
					multSelWithAutoCmpltHTML.append("<OPTION title='").append(encodedPvString).append("' value='")
							.append(encodedPvString).append("' selected>").append(encodedPvString);
				}

				multSelWithAutoCmpltHTML.append("</SELECT>\n \t\t</td>\n \t</tr>\n </table>");
				htmlString = multSelWithAutoCmpltHTML;
			}

		} catch (EnumConstantNotPresentException e) {
			throw new RuntimeException("Error while encoding url.", e);
		}
		return htmlString.toString();
	}

	protected String getMultiselectString() {
		return "";
	}

	protected boolean isPVSelected(ControlValue controlValue, PermissibleValue pv) {
		String value = (String) controlValue.getValue();

		return value != null && pv.getValue().equals(value);

	}

	/**
	 * Generate script tag for auto complete.
	 *
	 * @param controlId the control id
	 * @param sourceHtmlComponentValues the source html component values
	 * @param applyTo the apply to
	 * @param rowNumber 
	 *
	 * @return the string
	 *
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws UnsupportedEncodingException 
	 */
	protected String generateScriptTagForAutoComplete(String controlId,
			String applyTo, String controlName, Map<ContextParameter, String> contextParameter) {
		String parentContainerId = "";
		if (getContainer().getId() != null) {
			parentContainerId = getContainer().getId().toString();
		}
		String Url = "var myUrl= \"%s?%s=%s";
		StringBuilder comboStringBuilder = new StringBuilder(700);
		String DE_AJAX_HANDLER = getAjaxHandler(contextParameter);
		Date encounterDate = ControlsUtility.getFormattedDate(contextParameter.get(ContextParameter.ACTIVATION_DATE));

		String baseParams = "{controlId:'%s',containerIdentifier:'%s',controlName:'%s',encounterDate:'%s'}";

		comboStringBuilder
				.append(String.format(Url, DE_AJAX_HANDLER, WebUIManagerConstants.AJAX_OPERATION,
						WebUIManagerConstants.NEW_DE_COMBO_DATA_ACTION))
				.append("\";var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),baseParams:")
				.append(String.format(baseParams, getId(), parentContainerId, controlName,
						ControlsUtility.convertDateToString(encounterDate, "yyyy-MM-dd")))
				.append(",reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,width:140,tpl: getTpl(),listWidth:240,hiddenName: 'CB_coord_")
				.append(controlName)
				.append("',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : ")
				.append(minQueryChar)
				.append(",queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: '")
				.append(applyTo).append("'});");
		return comboStringBuilder.toString();
	}
}
