/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package annotation;

import java.lang.annotation.*;
/**
 *
 * @author hoby
 */
@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.FIELD)
@Target(ElementType.METHOD)
public @interface Url{
    String path() default "url obligatoire";
}
