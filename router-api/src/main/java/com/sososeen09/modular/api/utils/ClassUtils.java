package com.sososeen09.modular.api.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.sososeen09.modular.api.thead.DefaultPoolExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import dalvik.system.DexFile;

/**
 * Created by yunlong on 2018/3/24.
 */

public class ClassUtils {

    /**
     * 获得程序所有的apk(instant run会产生很多split apk)
     */
    public static List<String> getSourcePaths(Context context) throws PackageManager
            .NameNotFoundException, IOException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        List<String> sourcePaths = new ArrayList<>();
        String sourceDir = applicationInfo.sourceDir;
        sourcePaths.add(sourceDir);

        //instant run
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (applicationInfo.splitSourceDirs != null) {
                sourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
            }
        }
        return sourcePaths;
    }

    /**
     * 路由表
     *
     * @param context
     * @param packageName
     * @return
     * @throws PackageManager.NameNotFoundException
     * @throws IOException
     * @throws InterruptedException
     */
    public static Set<String> getFileNameByPackageName(Application context, final String
            packageName) throws PackageManager.NameNotFoundException, IOException,
            InterruptedException {
        final Set<String> classNames = new HashSet<>();
        List<String> paths = getSourcePaths(context);

        //使用同步计数器判断均处理完成
        final CountDownLatch parserCtl = new CountDownLatch(paths.size());
        ThreadPoolExecutor threadPoolExecutor = DefaultPoolExecutor.newDefaultPoolExecutor(paths.size());
        for (final String path : paths) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    DexFile dexfile = null;

                    try {
                        dexfile = new DexFile(path);
                        Enumeration<String> entries = dexfile.entries();
                        while (entries.hasMoreElements()) {
                            String element = entries.nextElement();
                            if (element.startsWith(packageName)) {
                                classNames.add(element);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if (dexfile != null) {
                            try {
                                dexfile.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //释放一个
                    parserCtl.countDown();
                }
            });
        }

        parserCtl.await();
        return classNames;
    }
}
