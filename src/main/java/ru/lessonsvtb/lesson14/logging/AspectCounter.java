package ru.lessonsvtb.lesson14.logging;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.lessonsvtb.lesson14.repositories.ProductDetailsRepository;

@Aspect
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AspectCounter {
    private ProductDetailsRepository productDetailsRepository;

    @After("execution(public String ru.lessonsvtb.lesson14.controllers.ProductsController.showOneProduct(..))")
    public void countView(JoinPoint joinPoint){
        productDetailsRepository.incrementView((Long) joinPoint.getArgs()[1]);
    }

}
