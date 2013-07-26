package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class Path
{
    private Long startForm;
    
    private Long endForm;
    
    private String startField;
    
    private List<PathLink> links = new ArrayList<PathLink>();
	

    public Path() {
    }

    public void addLink(String key, String refTabKey) {
        links.add(new PathLink(key, refTabKey));
    }

    public void addLink(String key, String refTab, String refTabKey) {
        links.add(new PathLink(key, refTab, refTabKey));
    }

    public Long getStartForm() {
        return startForm;
    }

    public void setStartForm(Long startForm) {
        this.startForm = startForm;
    }

    public Long getEndForm() {
        return endForm;
    }

    public void setEndForm(Long endForm) {
        this.endForm = endForm;
    }

    public String getStartField() {
        return startField;
    }

    public void setStartField(String startField) {
        this.startField = startField;
    }

    public List<PathLink> getLinks() {
        return links;
    }

    public void setLinks(List<PathLink> links) {
        this.links = links;
    }
}