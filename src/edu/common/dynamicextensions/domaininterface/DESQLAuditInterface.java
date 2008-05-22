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
	public Date getAuditDate();
	
	/**
	 * @param auditDate
	 */
	public void setAuditDate(Date auditDate);

	/**
	 * @return sql query executed
	 */
	public String getQueryExecuted();
	
	/**
	 * @param queryExecuted
	 */
	public void setQueryExecuted(String queryExecuted);

	/**
	 * @return user id
	 */
	public Long getUserId();

	/**
	 * @param userId
	 */
	public void setUserId(Long userId);
}
