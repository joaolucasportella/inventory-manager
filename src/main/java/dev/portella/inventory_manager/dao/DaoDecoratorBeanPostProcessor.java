package dev.portella.inventory_manager.dao;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class DaoDecoratorBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IDAO) {
            ProxyFactory pf = new ProxyFactory(bean);
            pf.addAdvice(new DaoLoggingInterceptor());
            pf.setProxyTargetClass(true);
            return pf.getProxy();
        }

        return bean;
    }

    private static class DaoLoggingInterceptor implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String method = invocation.getMethod().getName();
            String className = invocation.getThis().getClass().getSimpleName();
            long startTime = System.currentTimeMillis();

            try {
                System.out.printf("[DAO] %s.%s - Starting execution with %d parameters: %s%n",
                        className, method, invocation.getArguments().length,
                        java.util.Arrays.toString(invocation.getArguments()));

                Object result = invocation.proceed();

                long duration = System.currentTimeMillis() - startTime;
                System.out.printf("[DAO] %s.%s - Completed in %dms - Result: %s%n",
                        className, method, duration,
                        result != null ? result.getClass().getSimpleName() : "null");

                return result;
            } catch (Throwable t) {
                long duration = System.currentTimeMillis() - startTime;
                System.err.printf("[DAO] Error in %s.%s after %dms - %s: %s%n Stack trace: %s%n",
                        className, method, duration,
                        t.getClass().getSimpleName(), t.getMessage(),
                        java.util.Arrays.toString(t.getStackTrace()));
                throw t;
            }
        }
    }
}
