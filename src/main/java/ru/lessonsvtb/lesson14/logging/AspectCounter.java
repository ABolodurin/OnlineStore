package ru.lessonsvtb.lesson14.logging;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.lessonsvtb.lesson14.repositories.ProductDetailsRepository;

@Aspect
@Component
@RequiredArgsConstructor
public class AspectCounter {
    private final ProductDetailsRepository productDetailsRepository;

    @After("execution(public String ru.lessonsvtb.lesson14.controllers.ProductsController.showOneProduct(..))")
    public void countView(JoinPoint joinPoint) {
        productDetailsRepository.incrementView((Long) joinPoint.getArgs()[1]);
    }

}
