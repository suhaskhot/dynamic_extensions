
package edu.wustl.dynamicextensions.formdesigner.mapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import au.com.bytecode.opencsv.CSVReader;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.PvDataSource;
import edu.common.dynamicextensions.domain.nui.PvDataSource.Ordering;
import edu.common.dynamicextensions.domain.nui.PvVersion;

public class PvMapper {

	public static PvDataSource propertiesToPvDataSource(Properties controlProperties) throws IOException {
		PvDataSource pvDataSource = new PvDataSource();

		DataType dataType = controlProperties.getDataType("dataType");
		if (dataType != null) {
			pvDataSource.setDataType(dataType);
		} else {
			pvDataSource.setDataType(DataType.STRING);
		}

		pvDataSource.setOrdering(Ordering.ASC);
		PvVersion pvVersion = new PvVersion();
		List<PermissibleValue> pvList = new ArrayList<PermissibleValue>();

		// process permissible values from grid.
		Collection<Map<String, Object>> pvs = controlProperties.getListOfMap("pvs");
		if (pvs != null) {

			for (Map<String, Object> pvProperties : pvs) {
				Properties properties = new Properties(pvProperties);
				pvList.add(propertiesToPv(properties));
			}
		}

		// process permissible values from uploaded file.
		String pvFile = controlProperties.getString("pvFile");
		if (pvFile != null) {
			CSVReader csvReader = new CSVReader(new FileReader(new File(pvFile)));
			String[] csvData;
			while ((csvData = csvReader.readNext()) != null) {
				pvList.add(getPvFromFileData(csvData));
			}
		}

		pvVersion.setActivationDate(new Date());
		pvVersion.setPermissibleValues(pvList);
		pvDataSource.setPvVersions(Arrays.asList(pvVersion));
		return pvDataSource;
	}

	/**
	 * @param csvData
	 * @return
	 * @throws NumberFormatException
	 */
	private static PermissibleValue getPvFromFileData(String[] csvData) throws NumberFormatException {
		PermissibleValue pv = new PermissibleValue();
		if (!csvData[0].isEmpty()) {
			pv.setValue(csvData[0]);
		}
		// add validation check for is a number
		if (!csvData[1].isEmpty()) {
			pv.setNumericCode(Long.parseLong(csvData[1]));
		}
		if (!csvData[2].isEmpty()) {
			pv.setOptionName(csvData[2]);
		}
		if (!csvData[3].isEmpty()) {
			pv.setDefinitionSource(csvData[3]);
		}
		if (!csvData[4].isEmpty()) {
			pv.setConceptCode(csvData[4]);
		}
		return pv;
	}

	/**
	 * @param properties
	 * @return
	 */
	private static PermissibleValue propertiesToPv(Properties properties) {
		PermissibleValue pv = new PermissibleValue();
		String conceptCode = properties.getString("conceptCode");
		if (conceptCode != null) {
			pv.setConceptCode(conceptCode);
		}
		String definitionSource = properties.getString("definitionSource");
		if (definitionSource != null) {
			pv.setDefinitionSource(definitionSource);
		}
		Long numericCode = properties.getLong("numericCode");
		if (numericCode != null) {
			pv.setNumericCode(numericCode);
		}
		String definition = properties.getString("definition");
		if (definition != null) {
			pv.setOptionName(definition);
		}
		String value = properties.getString("value");
		if (value != null) {
			pv.setValue(value);
		}
		return pv;
	}

	/**
	 * @param pvDataSource
	 * @return
	 */
	public static void pVDataSourcetoProperties(PvDataSource pvDataSource, Properties controlProps) {
		List<Map<String, Object>> pvList = new ArrayList<Map<String, Object>>();
		for (PvVersion pvVer : pvDataSource.getPvVersions()) {
			for (PermissibleValue pv : pvVer.getPermissibleValues()) {
				pvList.add(pvToProperties(pv));
			}
		}
		controlProps.setProperty("pvs", pvList);
		controlProps.setProperty("dataType", pvDataSource.getDataType().toString());
	}

	/**
	 * @param pv
	 * @return
	 */
	private static Map<String, Object> pvToProperties(PermissibleValue pv) {
		Map<String, Object> pvProperties = new HashMap<String, Object>();
		pvProperties.put("value", pv.getValue() == null ? "" : pv.getValue());
		pvProperties.put("numericCode", pv.getNumericCode() == null ? "" : pv.getNumericCode());
		pvProperties.put("conceptCode", pv.getConceptCode() == null ? "" : pv.getConceptCode());
		pvProperties.put("definitionSource", pv.getDefinitionSource() == null ? "" : pv.getDefinitionSource());
		pvProperties.put("definition", pv.getOptionName() == null ? "" : pv.getOptionName());
		return pvProperties;
	}

}
