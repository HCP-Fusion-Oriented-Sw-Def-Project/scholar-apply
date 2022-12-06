package com.example.gbdpbootcore.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.Map;
import java.util.Properties;


@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class }),
        //@Signature(type = Executor.class, method = "delete", args = {MappedStatement.class, Object.class }),
})

public class ArgsInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler handler = (StatementHandler)invocation.getTarget();
        //由于mappedStatement为protected的，所以要通过反射获取
        MetaObject statementHandler = SystemMetaObject.forObject(handler);
        //mappedStatement中有我们需要的方法id
        MappedStatement mappedStatement = (MappedStatement) statementHandler.getValue("delegate.mappedStatement");
        // 加工方法参数
        processMethodArgs(mappedStatement, invocation);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }


    private void processMethodArgs(MappedStatement mappedStatement, Invocation invocation) {
        String id = mappedStatement.getId();
        String methodName = id.substring(id.lastIndexOf(".") + 1, id.length());
        if (methodName.toLowerCase().contains("delete")) {
            Object[] args = invocation.getArgs();
            for (Object arg : args) {
                if (arg instanceof Map) {
                    String idOrIds = (String) ((Map) arg).get("ids");
                    if (idOrIds.contains(",")){
                        String[] split = idOrIds.split(",");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < split.length; i++) {
                            String s = split[i];
                            stringBuilder.append("\" "+ s + " \"");
                            if (i <  split.length - 1) {
                                stringBuilder.append(",");
                            }
                        }
                        String newArg = stringBuilder.toString();
                    }
                }
            }


        }
    }
}
