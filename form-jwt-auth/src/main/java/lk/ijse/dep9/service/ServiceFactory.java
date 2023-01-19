package lk.ijse.dep9.service;

import lk.ijse.dep9.service.custom.impl.UserServiceImpl;

public class ServiceFactory {

    private static ServiceFactory serviceFactory;

    private ServiceFactory(){}

    public static ServiceFactory getInstance() {
        return (serviceFactory == null) ? (serviceFactory = new ServiceFactory()): serviceFactory;
    }

    public <T extends SuperService> T getService(ServiceTypes serviceType, Class<T> serviceClz){
        if (serviceType == ServiceTypes.USER) {
            return serviceClz.cast(new UserServiceImpl());
        }
        return null;
    }
}
