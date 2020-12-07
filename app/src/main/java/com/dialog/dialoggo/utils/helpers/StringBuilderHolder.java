package com.dialog.dialoggo.utils.helpers;

public class StringBuilderHolder implements Cloneable {

    private static StringBuilderHolder stringBuilderHolder;
    private final StringBuilder stringBuilder;

    private StringBuilderHolder() {
        stringBuilder = new StringBuilder();
    }

    public static synchronized StringBuilderHolder getInstance() {

        if (stringBuilderHolder == null) {
            stringBuilderHolder = new StringBuilderHolder();
        }
        return stringBuilderHolder;
    }

    public void append(String s) {
        stringBuilder.append(s);
    }

    public StringBuilder getText() {
        return stringBuilder;
    }

    public void clear() {
        stringBuilder.setLength(0);
    }

    public void deleteCharAt(int index) {
        stringBuilder.deleteCharAt(index);
    }

    public void delete(int start, int end) {
        stringBuilder.delete(start, end);

    }

    public void subString(int start, int end) {
        stringBuilder.substring(start, end);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
//        return super.clone();

        throw new CloneNotSupportedException();
    }

}
