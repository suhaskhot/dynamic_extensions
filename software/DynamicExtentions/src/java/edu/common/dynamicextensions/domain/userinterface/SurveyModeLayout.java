package edu.common.dynamicextensions.domain.userinterface;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

import edu.common.dynamicextensions.domain.CategoryEntity;

public class SurveyModeLayout extends DynamicExtensionLayout {

	private static final long serialVersionUID = 6460804427946477366L;
	private Collection<Page> pageCollection = new LinkedHashSet<Page>();

	public SurveyModeLayout(SurveyModeLayout layout, Map<String, CategoryEntity> catEntNameMap)
	{

		for(Page page:layout.getPageCollection())
		{
			Page tempPage = new Page(page,catEntNameMap);
			tempPage.setLayout(this);
			pageCollection.add(tempPage);
		}
	}

	public SurveyModeLayout()
	{
		super();
	}

	public Collection<Page> getPageCollection() {
		return this.pageCollection;
	}

	public void setPageCollection(final Collection<Page> pageCollection) {
		this.pageCollection = pageCollection;
	}
	
}