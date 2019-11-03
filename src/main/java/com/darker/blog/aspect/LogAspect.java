package com.darker.blog.aspect;

import cn.hutool.json.JSONUtil;
import com.darker.blog.annotation.SysLog;
import com.darker.blog.orm.entity.Log;
import com.darker.blog.orm.entity.User;
import com.darker.blog.service.LogService;
import com.darker.blog.utils.AddressUtil;
import com.darker.blog.utils.HttpContextUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
public class LogAspect extends AspectSupport {

    private ThreadLocal<Log> sysLogThreadLocal = new ThreadLocal<>();
    @Autowired
    LogService logService;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.darker.blog.annotation.SysLog)")
    public void sysLogAspect() {

    }


    /**
     * 处理日志
     *
     * @param point
     * @throws Throwable
     */
    @Before(value = "sysLogAspect()")
    public void handleLog(JoinPoint point) throws Throwable {
        Method targetMethod = resolveMethod(point);
        SysLog annotation = targetMethod.getAnnotation(SysLog.class);
        Log log = new Log();
        sysLogThreadLocal.set(log);
        //组装日志信息
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip =getIpAddr(request);
        log.setIp(ip);
        log.setOperation(annotation.value());
        log.setCreateTime(new Date());
        // 请求的类名
        String className = point.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = targetMethod.getName();
        log.setMethod(className + "." + methodName + "()");

        // 请求的方法参数值
//        Object[] args = point.getArgs();
//        // 请求的方法参数名称
//        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
//        String[] paramNames = u.getParameterNames(targetMethod);
//        if (args != null && paramNames != null) {
//            StringBuilder params = new StringBuilder();
//            params = handleParams(params, args, Arrays.asList(paramNames));
//            log.setParams(params.toString());
//        }
       String params=JSONUtil.toJsonStr(point.getArgs());
        log.setParams(params);
        log.setLocation(AddressUtil.getCityInfo(ip));
        //从当前登录用户获取用户名
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        log.setUsername(user.getUsername());
    }

    /**
     * 正常日志记录
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "sysLogAspect()")
    public void doAfterReturning(Object ret) {
        //得到当前线程的log对象
        Log log = sysLogThreadLocal.get();
        //数据入库
        logService.save(log);
    }

    /**
     * 异常日志记录
     * @param point
     * @param e
     */
    @AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
    public void doAfterThrowable(JoinPoint point,Throwable e) {
        Method targetMethod = resolveMethod(point);
        SysLog annotation = targetMethod.getAnnotation(SysLog.class);
       String error= annotation.exceptionMessage()+getStackTrace(e);
        Log log = sysLogThreadLocal.get();
        log.setErrorMessage(error);
        logService.save(log);
    }

    /**
     * 获取堆栈信息
     *
     * @param throwable
     * @return
     */
    public String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 ) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Map) {
                    Set set = ((Map) args[i]).keySet();
                    List<Object> list = new ArrayList<>();
                    List<Object> paramList = new ArrayList<>();
                    for (Object key : set) {
                        list.add(((Map) args[i]).get(key));
                        paramList.add(key);
                    }
                    return handleParams(params, list.toArray(), paramList);
                } else {
                    if (args[i] instanceof Serializable) {
                        Class<?> aClass = args[i].getClass();
                        try {
                            aClass.getDeclaredMethod("toString", new Class[]{null});
                            // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                            params.append(" ").append(paramNames.get(i)).append(": ");
                        } catch (NoSuchMethodException e) {
                            params.append(" ").append(paramNames.get(i)).append(": ");
                        }
                    } else if (args[i] instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) args[i];
                        params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                    } else {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                    }
                }
            }
        } catch (Exception ignore) {
            params.append("参数解析失败");
        }
        return params;
    }

}
