package com.pliesveld.flashnote.dao;

import com.pliesveld.flashnote.domain.Student;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class StudentDao extends GenericDao<Student> {
    public StudentDao() {
        setPersistentClass(Student.class);
    }

    public Student createStudent(String name)
    {
        Session session = sessionFactory.getCurrentSession();
        Student student = new Student(name);
        session.saveOrUpdate(student);
        return student;
    }

    public List<Student> findUserByName(String name)
    {
        Session session = sessionFactory.getCurrentSession();

        @SuppressWarnings("unchecked")
        List<Student> list = (List<Student>) session.createCriteria(persistentClass)
                .add(Restrictions.eq("name", name))
                .list();

        return list;
    }

}
