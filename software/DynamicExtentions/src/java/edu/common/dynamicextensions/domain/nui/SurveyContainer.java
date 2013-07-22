
package edu.common.dynamicextensions.domain.nui;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;
import edu.common.dynamicextensions.nutility.FormDataUtility;

public class SurveyContainer extends Container {

	private static final String CONTAINER_ID = "<input type='hidden' id='containerIdentifier' name='containerIdentifier'  value='%d'></input>";

	private static final String FORM_CAPTION = "<input type='hidden' id='categoryName' value='%s'></input>";

	private static final String CONTROL_COUNT = "<input type='hidden' id='controlsCount' value='%d'></input>";

	private static final String EMPTY_CONTROL_COUNT = "<input type='hidden' id='emptyControlsCount' value='%d'></input>";

	private static final String DISPLAY_PAGE = "<input type='hidden' id='displayPage' name='displayPage'  value='%d'></input>";

	private static final long serialVersionUID = -3385903137752632946L;

	private final LinkedHashMap<String, Page> pages = new LinkedHashMap<String, Page>();

	/**
	 * 
	 */
	private long pageId = 0;

	public Collection<Page> getPages() {
		return pages.values();
	}

	public void setPages(Collection<Page> pages) {
		pages.clear();

		for (Page page : pages) {
			addPage(page);
		}
	}

	public void addPage(Page page) {
		if (pages.containsKey(page.getName())) {
			throw new RuntimeException("Page with same name already exists: " + page.getName());
		}
		page.setId(pageId++);
		pages.put(page.getName(), page);
		page.setContainer(this);

	}

	public Page getPage(String name) {
		Page page = pages.get(name);
		return page;
	}

	public void deletePage(String pageName) {

		Page page = pages.remove(pageName);

		if (page == null) {
			throw new RuntimeException("Page with name doesn't exist: " + pageName);
		}

		for (Control control : page.getControls()) {
			page.deleteControl(control.getName());
		}

	}

	@Override
	public String render(Map<ContextParameter, String> contextParameter, FormData formData) {
		String categorydiv = "<div><div id='sm-pages'>%s</div></div>";
		String pagediv = "<div class='sm-page' id='%d' style='display:none'>%s</div>";
		StringBuilder pages = new StringBuilder();
		StringBuilder categoryHtml = new StringBuilder(renderHiddenInputs(formData));

		for (Page p : getPages()) {
			pages.append(String.format(pagediv, p.getId().longValue(), p.render(formData, contextParameter)));
		}

		categoryHtml.append(String.format(categorydiv, pages.toString()));
		return categoryHtml.toString();
	}

	private String renderHiddenInputs(FormData formData) throws NumberFormatException {
		String containerIdentifier = String.format(CONTAINER_ID, getId());
		String formCaption = String.format(FORM_CAPTION, getCaption());
		String controlsCount = String.format(CONTROL_COUNT, FormDataUtility.getFilledControlCount(formData));
		String emptyControlsCount = String.format(EMPTY_CONTROL_COUNT, FormDataUtility.getEmptyControlCount(formData));
		String displayPage = String.format(DISPLAY_PAGE, getFirstEmptyPage(formData));

		StringBuilder results = new StringBuilder();
		results.append(containerIdentifier);
		results.append(formCaption);
		results.append(controlsCount);
		results.append(emptyControlsCount);
		results.append(displayPage);
		return results.toString();
	}

	public void editContainer(SurveyContainer newContainer) {
		super.editContainer(newContainer);

		for (Page page : newContainer.getPages()) {
			Page existingPage = getPage(page.getName());

			if (existingPage == null) {
				page.setContainer(this);
				addPage(page);
			} else {
				existingPage.editPage(page);
			}
		}

		for (Page page : pages.values()) {

			if (newContainer.getPage(page.getName()) == null) {
				pages.remove(page.getName());
			}
		}
	}

	private long getFirstEmptyPage(FormData data) {
		for (Page page : getPages()) {

			for (Control control : page.getControls()) {

				if (data.getFieldValue(control.getName()).isEmpty()) {
					return page.getId();
				}
			}
		}
		return -1;
	}

	public Page createPage(String name, String caption) {
		if (pages.containsKey(name)) {
			throw new RuntimeException("Page with same name already exists: " + name);
		}
		Page page = new Page(name, caption);
		addPage(page);
		return page;

	}

	public void moveControl(String srcPage, String tgtPage, String ctrlName) {
		Page sourcePage = pages.get(srcPage);
		if (sourcePage == null) {
			throw new RuntimeException("Page with name doesn't exist: " + srcPage);
		}

		Page targetPage = pages.get(tgtPage);
		if (targetPage == null) {
			throw new RuntimeException("Page with name doesn't exist");
		}

		sourcePage.moveTo(ctrlName, targetPage);

	}

}
