package com.sososeen09.modular.compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by yunlong on 2018/3/24.
 */

public class EmptyUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
