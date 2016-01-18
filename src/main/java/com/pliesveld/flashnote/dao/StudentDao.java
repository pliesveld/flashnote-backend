package com.pliesveld.flashnote.dao;

import com.pliesveld.flashnote.domain.Student;
import org.hibernate.Query;
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
        session.save(student);
        return student;
    }

    @Transactional(readOnly = true)
    public Student getStudent(int id)
    {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.getNamedQuery("Student.findByStudentId");
        query.setInteger("id",id);
        return (Student) query.uniqueResult();
    }

    @Transactional(readOnly = true)
    public Student getStudent(String email)
    {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.getNamedQuery("Student.findByStudentEmail");
        query.setString("email",email);
        return (Student) query.uniqueResult();
    }

    @Transactional(readOnly = true)
    public int getCount()
    {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.getNamedQuery("Student.count");
        return query.uniqueResult().hashCode();
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

    public Iterable<Student> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(persistentClass).list();
    }
}