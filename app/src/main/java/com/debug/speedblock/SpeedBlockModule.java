package com.debug.speedblock;

import android.location.Location;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SpeedBlockModule implements IXposedHookLoadPackage {

    private static final String TAG = "SpeedBlockDebug";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if ("com.debug.speedblock".equals(lpparam.packageName)) return;
        XposedBridge.log(TAG + ": hook -> " + lpparam.packageName);
        try {
            XposedHelpers.findAndHookMethod(Location.class, "getSpeed", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    param.setResult(0.0f);
                }
            });
            XposedHelpers.findAndHookMethod(Location.class, "hasSpeed", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    param.setResult(true);
                }
            });
            XposedHelpers.findAndHookMethod(Location.class, "setSpeed", float.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    param.args[0] = 0.0f;
                }
            });
            XposedBridge.log(TAG + ": hook 成功");
        } catch (Throwable e) {
            XposedBridge.log(TAG + ": hook 失败 " + e);
        }
    }
}
