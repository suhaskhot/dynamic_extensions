package edu.common.dynamicextensions.query;

public class PathLink {
    private String refTab;
    
    private String refTabKey;
    
    private String key;	

    public PathLink() {
    }

    public PathLink(String key, String refTabKey) {
        this.key = key;
        this.refTabKey = refTabKey;
    }

    public PathLink(String key, String refTab, String refTabKey) {
        this.key = key;
        this.refTab = refTab;
        this.refTabKey = refTabKey;
    }

    public String getRefTab() {
        return refTab;
    }

    public void setRefTab(String refTab) {
        this.refTab = refTab;
    }

    public String getRefTabKey() {
        return refTabKey;
    }

    public void setRefTabKey(String refTabKey) {
        this.refTabKey = refTabKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}