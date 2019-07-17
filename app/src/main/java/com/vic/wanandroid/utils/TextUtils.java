package com.vic.wanandroid.utils;

public class TextUtils {
    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        //&ldquo;&quot;&nbsp;&mdash;
        content = content.replaceAll("&.dquo;", "\"");
        content = content.replaceAll("&nbsp;", " ");
        content = content.replaceAll("&mdash;", "-");
        content = content.replaceAll("&pi;", "Pi");
        return content;
    }
}
