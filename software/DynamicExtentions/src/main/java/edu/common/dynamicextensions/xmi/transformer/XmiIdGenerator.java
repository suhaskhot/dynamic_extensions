
package edu.common.dynamicextensions.xmi.transformer;

public class XmiIdGenerator
{

	private static int counter;
	private static final String prefix = "x";
	private static XmiIdGenerator generator;

	private XmiIdGenerator()
	{
		counter = 0;
	}

	public static String getNextId()
	{
		if (generator == null)
		{
			generator = new XmiIdGenerator();
		}
		return prefix + (++counter);
	}
}
