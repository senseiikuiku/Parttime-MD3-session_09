package com.example.model.dao.user;

import com.example.model.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImp implements UserDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return true;
        }catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public User findUserByEmail(String email) {
        Session session = sessionFactory.openSession();
        try {
            String sql = "from User as u where u.email = :_email";
            return session.createQuery(sql, User.class).setParameter("_email", email).uniqueResult();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }
}
