package com.example.jiefly.multiparametermonitor.util;

import android.os.Build;

/**
 * Created by chgao on 17-5-18.
 */

public class ApiLevelHelper {

    private ApiLevelHelper() {
        //no instance
    }

    /**
     * Checks if the current api level is at least the provided value.
     *
     * @param apiLevel One of the values within {@link Build.VERSION_CODES}.
     * @return <code>true</code> if the calling version is at least <code>apiLevel</code>.
     * Else <code>false</code> is returned.
     */
    public static boolean isAtLeast(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    /**
     * Checks if the current api level is at lower than the provided value.
     *
     * @param apiLevel One of the values within {@link Build.VERSION_CODES}.
     * @return <code>true</code> if the calling version is lower than <code>apiLevel</code>.
     * Else <code>false</code> is returned.
     */
    public static boolean isLowerThan(int apiLevel) {
        return Build.VERSION.SDK_INT < apiLevel;
    }
}
