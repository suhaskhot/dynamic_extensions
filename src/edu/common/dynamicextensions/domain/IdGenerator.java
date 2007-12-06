package edu.common.dynamicextensions.domain;

/**
 * @author kunal_kamble
 * @version 1.0
 */
public class IdGenerator extends AbstractAttribute {

	/** Default serial version id */
	private static final long serialVersionUID = 1L;

	/** identifier field. */
	private Long id;

	/** nullable persistent field. */
	private String className;

	/** nullable persistent field. */
	private Long nextId;

	/** default constructor. */
	public IdGenerator() {
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return class name can be either sample or experiment
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * @param className
	 *            class name can be either sample or experiment
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return next available id
	 */
	public Long getNextId() {
		return this.nextId;
	}

	/**
	 * @param nextId
	 *            next available id
	 */
	public void setNextId(Long nextAvailableId) {
		this.nextId = nextAvailableId;
	}

}
