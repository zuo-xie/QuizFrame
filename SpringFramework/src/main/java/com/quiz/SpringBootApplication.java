package com.quiz;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBootApplication {

    public static void main(String[] args) throws Exception {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("application.xml");
        CommodityServiceImpl commodityService = (CommodityServiceImpl) beanFactory
                .getBean("commodityServiceImpl");

    }
}
