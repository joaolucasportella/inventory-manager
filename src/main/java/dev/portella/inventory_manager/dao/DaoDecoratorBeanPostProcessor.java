package dev.portella.inventory_manager.dao;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import dev.portella.inventory_manager.model.LogModel;
import dev.portella.inventory_manager.service.LogService;

@Component
public class DaoDecoratorBeanPostProcessor implements BeanPostProcessor {

    private final LogService logService;

    public DaoDecoratorBeanPostProcessor(LogService logService) {
        this.logService = logService;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ICrudDAO) {
            ProxyFactory pf = new ProxyFactory(bean);
            pf.addAdvice(new DaoLoggingInterceptor(logService));
            pf.setProxyTargetClass(true);
            return pf.getProxy();
        }

        return bean;
    }

    private static class DaoLoggingInterceptor implements MethodInterceptor {

        private final LogService logService;

        public DaoLoggingInterceptor(LogService logService) {
            this.logService = logService;
        }

        private void saveLog(String message) {
            LogModel logModel = new LogModel();
            logModel.setMessage(message);
            logService.save(logModel);
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {

            String message;

            String method = invocation.getMethod().getName();
            String className = invocation.getThis().getClass().getSimpleName();
            long startTime = System.currentTimeMillis();

            try {
                message = String.format(
                        "[DAO] %s.%s - Starting execution with %d parameters: %s%n",
                        className,
                        method,
                        invocation.getArguments().length,
                        java.util.Arrays.toString(invocation.getArguments()));

                System.out.printf(message);
                saveLog(message);

                Object result = invocation.proceed();

                long duration = System.currentTimeMillis() - startTime;
                message = String.format(
                        "[DAO] %s.%s - Completed in %dms - Result: %s%n",
                        className,
                        method,
                        duration,
                        result != null ? result.getClass().getSimpleName() : "null");

                System.out.printf(message);
                saveLog(message);

                return result;
            } catch (Throwable t) {
                long duration = System.currentTimeMillis() - startTime;
                message = String.format(
                        "[DAO] Error in %s.%s after %dms - %s: %s%n Stack trace: %s%n",
                        className,
                        method,
                        duration,
                        t.getClass().getSimpleName(),
                        t.getMessage(),
                        java.util.Arrays.toString(t.getStackTrace()));

                System.err.printf(message);
                saveLog(message);

                throw t;
            }
        }
    }
}
