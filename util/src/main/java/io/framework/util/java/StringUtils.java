package io.framework.util.java;

/**
 * java工具类
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     * @param s 需要判断的字符串
     * @return true：字符串为空  false：字符串不为空
     */
    public Boolean isBlank(final CharSequence s) {
        final int strLen = length(s);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;

    }

    private static Integer length(CharSequence s) {
        return s == null ? 0 : s.length();
    }


}
