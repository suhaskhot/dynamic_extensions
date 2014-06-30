package edu.common.dynamicextensions.nutility;


public class BOUtil {

    private static BOUtil instance = null;

    private BOTemplateGenerator generator;

    protected BOUtil() {
        // Exists only to defeat instantiation.
    }

    public static BOUtil getInstance() {
        if (instance == null) {
            instance = new BOUtil();
        }
        return instance;
    }

    public BOTemplateGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(BOTemplateGenerator generator) {
        this.generator = generator;
    }

}
