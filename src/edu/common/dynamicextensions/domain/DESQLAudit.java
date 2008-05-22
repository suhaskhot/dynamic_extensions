package edu.common.dynamicextensions.domain;

import java.util.Date;

import edu.common.dynamicextensions.domaininterface.DESQLAuditInterface;

/**
 * @author kunal_kamble
 * @hibernate.class table="DYEXTN_SQL_AUDIT"
 * @hibernate.cache  usage="read-write"
 */
 
public class DESQLAudit extends DynamicExtensionBaseDomainObject implements DESQLAuditInterface
{

	/**
	 * default serial version id 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * user id  
	 */
	private Long userId;
	
	/**
	 * audited on 
	 */
	private Date auditDate;
	
	/**
	 * sql query executed
	 */
	private String queryExecuted;
	
	public DESQLAudit()
	{
			
	}
    /**
     * @return
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_DE_AUDIT_SEQ"
     */
	public Long getId()
	{
		return this.id;
	}



    /**
     * @hibernate.property name="auiditDate" type="date" column="AUDIT_DATE"
     * @return Returns the caption.
     */
	public Date getAuditDate()
	{
		return auditDate;
	}


	public void setAuditDate(Date auditDate)
	{
		this.auditDate = auditDate;
	}



    /**
     * @hibernate.property name="queryExecuted" type="string" column="QUERY_EXECUTED"
     * @return Returns the last query excuted.
     */
	public String getQueryExecuted()
	{
		return queryExecuted;
	}


	public void setQueryExecuted(String queryExecuted)
	{
		this.queryExecuted = queryExecuted;
	}


    /**
     * @hibernate.property name="userId" type="long" column="USER_ID"
     * @return Returns the caption.
     */
	public Long getUserId()
	{
		return userId;
	}


	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
}
