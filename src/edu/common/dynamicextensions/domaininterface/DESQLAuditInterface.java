package edu.common.dynamicextensions.domaininterface;

import java.util.Date;

/**
 * @author kunal_kamble
 *
 */
public interface DESQLAuditInterface
{
	/**
	 * @return audit date
	 */
	Date getAuditDate();
	
	/**
	 * @param auditDate
	 */
	void setAuditDate(Date auditDate);

	/**
	 * @return sql query executed
	 */
	String getQueryExecuted();
	
	/**
	 * @param queryExecuted
	 */
	void setQueryExecuted(String queryExecuted);

	/**
	 * @return user id
	 */
	Long getUserId();

	/**
	 * @param userId
	 */
	void setUserId(Long userId);
}
