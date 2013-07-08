
package edu.wustl.dynamicextensions.formdesigner.mapper;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.PvDataSource;
import edu.common.dynamicextensions.domain.nui.PvDataSource.Ordering;
import edu.common.dynamicextensions.domain.nui.PvVersion;

public class PvMapper
{

	@SuppressWarnings("unchecked")
	public static PvDataSource propertiesToPvDataSource(Properties controlProperties)
			throws IOException
	{
		PvDataSource pvDataSource = new PvDataSource();
		if (controlProperties.contains("dataType"))
		{
			pvDataSource.setDataType(controlProperties.getDataType("dataType"));
		}
		else
		{
			pvDataSource.setDataType(DataType.STRING);
		}
		pvDataSource.setOrdering(Ordering.ASC);
		PvVersion pvVersion = new PvVersion();
		List<PermissibleValue> pvList = new ArrayList<PermissibleValue>();
		if (controlProperties.contains("pvs"))
		{
			CSVReader csvReader = new CSVReader(
					new StringReader(controlProperties.getString("pvs")));
			List<String[]> pvData = csvReader.readAll();
			for (String[] pv : pvData)
			{
				getPermissibleValueFromArray(pvList, pv);
			}
		}
		pvVersion.setActivationDate(new Date());
		pvVersion.setPermissibleValues(pvList);
		pvDataSource.setPvVersions(Arrays.asList(pvVersion));
		return pvDataSource;
	}

	/**
	 * @param pvDataSource
	 * @return
	 */
	public static void pVDataSourcetoProperties(PvDataSource pvDataSource, Properties controlProps)
	{

		StringBuilder pvData = new StringBuilder("");
		for (PvVersion pvVer : pvDataSource.getPvVersions())
		{
			for (PermissibleValue pv : pvVer.getPermissibleValues())
			{
				pvData.append(",").append(pv.getOptionName());
			}
		}
		pvData = new StringBuilder(pvData.toString().replaceFirst(",", ""));
		controlProps.setProperty("pvs", pvData.toString());
		controlProps.setProperty("dataType", pvDataSource.getDataType().toString());
	}

	/**
	 * @param pvList
	 * @param pv
	 * @throws NumberFormatException
	 */
	private static void getPermissibleValueFromArray(List<PermissibleValue> pvList, String[] pv)
			throws NumberFormatException
	{
		for (String value : pv)
		{
			value = value.trim();
			if (!value.equalsIgnoreCase(""))
			{
				PermissibleValue permissibleValue = new PermissibleValue();
				permissibleValue.setValue(value);
				permissibleValue.setOptionName(value);
				pvList.add(permissibleValue);
			}
		}
	}

}
