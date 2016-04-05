package com.pliesveld.flashnote.unit.dao.config;

import com.pliesveld.flashnote.spring.db.H2DataSource;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManagerFactory;


@Configuration
@Import(value = {H2DataSource.class,PersistenceContext.class})
public class SpringTestCustomEntityManagerConfig {

    @Autowired
    EntityManagerFactory entityManagerFactory;

}
