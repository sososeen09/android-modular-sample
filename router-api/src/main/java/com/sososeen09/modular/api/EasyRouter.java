package com.sososeen09.modular.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.sososeen09.modular.annotation.model.RouteMeta;
import com.sososeen09.modular.api.callback.NavigationCallback;
import com.sososeen09.modular.api.exception.NoRouteFoundException;
import com.sososeen09.modular.api.template.IRouteGroup;
import com.sososeen09.modular.api.template.IRouteRoot;
import com.sososeen09.modular.api.template.IService;
import com.sososeen09.modular.api.utils.ClassUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * Created by yunlong on 2018/3/24.
 */

public class EasyRouter {
    private static final String ROUTE_ROOT_PAKCAGE = "com.sososeen09.easy.routes";
    private static final String SDK_NAME = "EasyRouter";
    private static final String SEPARATOR = "$$";
    private static final String SUFFIX_ROOT = "Root";
    private static Application mContext;

    private static EasyRouter instance;
    private Handler mHandler;

    private static final String TAG = "EasyRouter";

    private EasyRouter() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application) {
        mContext = application;
        try {
            loadInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "初始化失败!", e);
        }
    }

    public static EasyRouter getInstance() {
        if (instance == null) {
            synchronized (EasyRouter.class) {
                if (instance == null) {
                    instance = new EasyRouter();
                }
            }
        }
        return instance;
    }

    /**
     * 分组表制作
     * A分组-》 路由表
     * B分组-》 路由表
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws PackageManager            .NameNotFoundException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private static void loadInfo() throws InterruptedException, IOException, PackageManager
            .NameNotFoundException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        //获得所有 apt生成的路由类的全类名 (路由表)
        Set<String> routerMap = ClassUtils.getFileNameByPackageName(mContext, ROUTE_ROOT_PAKCAGE);

        for (String className : routerMap) {
            if (className.startsWith(ROUTE_ROOT_PAKCAGE + "." + SDK_NAME + SEPARATOR +
                    SUFFIX_ROOT)) {
                //说明查找到的是root表
                ((IRouteRoot) Class.forName(className).newInstance()).loadInto(WareHouse.groupsIndex);
            }
        }

        for (Map.Entry<String, Class<? extends IRouteGroup>> stringClassEntry : WareHouse.groupsIndex.entrySet()) {
            Log.e(TAG, "Root映射表[ " + stringClassEntry.getKey() + " : " + stringClassEntry
                    .getValue() + "]");
        }
    }

    public Postcard build(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路由地址无效!");
        } else {
            return build(path, extractGroup(path));
        }
    }

    public Postcard build(String path, String group) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            throw new RuntimeException("路由地址无效!");
        } else {
            return new Postcard(path, group);
        }
    }

    /**
     * 获得组别
     *
     * @param path
     * @return
     */
    private String extractGroup(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new RuntimeException(path + " : 不能提取group.");
        }
        try {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (TextUtils.isEmpty(defaultGroup)) {
                throw new RuntimeException(path + " : 不能提取group.");
            } else {
                return defaultGroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object navigation(Context context, Postcard postcard, int requestCode, NavigationCallback callback) {
        try {
            prepareCard(postcard);
        } catch (NoRouteFoundException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onLost(postcard);
            }
            return null;
        }

        if (null != callback) {
            callback.onFound(postcard);
        }

        switch (postcard.getType()) {
            case ACTIVITY:
                final Context currentContext = null == context ? mContext : context;
                Intent intent = new Intent(currentContext, postcard.getDestination());
                intent.putExtras(postcard.getExtras());
                int flags = postcard.getFlags();
                if (flags != -1) {
                    intent.setFlags(postcard.getFlags());
                }

                if (!(currentContext instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (requestCode > 0) {
                    if (currentContext instanceof Activity) {
                        ActivityCompat.startActivityForResult(((Activity) currentContext), intent, requestCode, postcard.getOptionsBundle());
                    } else {
                        throw new NoRouteFoundException("can not startActivityForResult not in activity");
                    }
                } else {
                    ActivityCompat.startActivity(currentContext, intent, postcard.getOptionsBundle());
                }

                if ((0 != postcard.getEnterAnim() || 0 != postcard.getExitAnim()) &&
                        currentContext instanceof Activity) {
                    //老版本
                    ((Activity) currentContext).overridePendingTransition(postcard
                                    .getEnterAnim()
                            , postcard.getExitAnim());
                }

                if (callback != null) {
                    callback.onArrival(postcard);
                }
                break;

            case ISERVICE:
                return postcard.getService();
            default:
                break;
        }
        return null;
    }

    private void prepareCard(Postcard card) {
        RouteMeta routeMeta = WareHouse.routes.get(card.getPath());
        //还没准备的
        if (routeMeta == null) {
            Class<? extends IRouteGroup> groupMetas = WareHouse.groupsIndex.get(card.getGroup());
            if (null == groupMetas) {
                throw new NoRouteFoundException("没找到对应路由: " + card.getGroup() + " " +
                        card.getPath());
            }
            IRouteGroup iRouteGroup;
            try {
                iRouteGroup = groupMetas.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("路由分组映射表记录失败.", e);
            }

            iRouteGroup.loadInto(WareHouse.routes);
            //已经准备过了就可以移除了 (不会一直存在内存中)
            WareHouse.groupsIndex.remove(card.getGroup());
            //再次进入 else
            prepareCard(card);
        } else {
            card.setDestination(routeMeta.getDestination());
            card.setType(routeMeta.getType());

            switch (routeMeta.getType()) {
                case ISERVICE:
                    Class<?> destination = routeMeta.getDestination();
                    IService service = WareHouse.services.get(destination);
                    if (null == service) {
                        try {
                            service = (IService) destination.getConstructor().newInstance();
                            WareHouse.services.put(destination, service);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    card.setService(service);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 注入数据
     *
     * @param target
     */
    public void inject(Activity target) {
        ExtraManager.getInstance().loadExtras(target);
    }
}
