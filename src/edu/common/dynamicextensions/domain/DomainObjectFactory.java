
package edu.common.dynamicextensions.domain;

/**
 * @author sujay_narkar
 *
 */
public class DomainObjectFactory {
	
	/**
	 * Domain Object Factory Instance
	 */
	private static DomainObjectFactory domainObjectFactory;
	
	/**
	 * 
	 *
	 */
	protected DomainObjectFactory(){
		
	}
	
	/**
	 * Returns the instance of SegmentationDomainElementFactory
	 * @return SegmentationDomainElementFactory
	 */
	public static synchronized DomainObjectFactory getInstance () {
		if (domainObjectFactory == null) {
			domainObjectFactory= new  DomainObjectFactory();
		}
		return domainObjectFactory ;
	}
	
}
