package org.mybatis.utils;

import java.util.Objects;

public interface Component {
    static String getComponentName(Class<?> component) {
        Objects.requireNonNull(component);
        String name = component.getSimpleName();
        if (name == null || name.length() == 0) {
            return name;
        }
        // 如果发现类的前两个字符都是大写，则直接返回类名
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        // 将类名的第一个字母转成小写，然后返回
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
