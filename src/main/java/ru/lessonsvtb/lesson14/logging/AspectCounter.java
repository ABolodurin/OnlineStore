package ru.lessonsvtb.lesson14.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.lessonsvtb.lesson14.services.ProductDetailsService;

@Aspect
@Component
public class AspectCounter {
    private ProductDetailsService productDetailsService;

    @Autowired
    public void setProductDetailsService(ProductDetailsService productDetailsService) {
        this.productDetailsService = productDetailsService;
    }

    @After("execution(public String ru.lessonsvtb.lesson14.controllers.ProductsController.showOneProduct(..))")
    public void countView(JoinPoint joinPoint){
        productDetailsService.countView((Long) joinPoint.getArgs()[1]);
    }

}
