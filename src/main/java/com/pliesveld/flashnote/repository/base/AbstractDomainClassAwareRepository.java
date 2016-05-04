package com.pliesveld.flashnote.repository.base;

import org.springframework.data.repository.NoRepositoryBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@NoRepositoryBean
public abstract class AbstractDomainClassAwareRepository<T> {
    public final Class<T> domainClass;

    @SuppressWarnings("unchecked")
    protected AbstractDomainClassAwareRepository() {

        Type genericSuperclass = this.getClass().getGenericSuperclass();

        while(!(genericSuperclass instanceof ParameterizedType))
        {
            if(genericSuperclass == null)
            {
                throw new IllegalStateException("Unable to determine type arguments because generic superclass has reached null.  Did I forget to inherit from DomainBaseEntity?");
            }

            if(!(genericSuperclass instanceof Class))
            {
                throw new IllegalStateException("Unable to determine type arguments because generic superclass neither paramterized type nor class");
            }

            if(genericSuperclass == AbstractDomainClassAwareRepository.class)
            {
                throw new IllegalStateException("Unable to determine type arguments because no parameterized generic superclass found.");
            }

            genericSuperclass = ((Class)genericSuperclass).getGenericSuperclass();
        }

        ParameterizedType type = (ParameterizedType)genericSuperclass;
        Type[] arguments = type.getActualTypeArguments();
        this.domainClass = (Class<T>) arguments[0];
    }
}
