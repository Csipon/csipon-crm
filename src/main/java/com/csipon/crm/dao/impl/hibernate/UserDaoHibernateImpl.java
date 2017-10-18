package com.csipon.crm.dao.impl.hibernate;

import com.csipon.crm.dao.AddressDao;
import com.csipon.crm.dao.OrganizationDao;
import com.csipon.crm.dao.UserDao;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.real.RealUser;
import com.csipon.crm.domain.request.UserRowRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory;
    private Session currentSession;
    private Transaction currentTransaction;



    public UserDaoHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findByEmail(String email) {
        openSession();
        User user = currentSession.createQuery("from Users WHERE email = :email", RealUser.class).setParameter("email", email).getSingleResult();

        return null;
    }

    @Override
    public long updatePassword(User user, String password) {
        return 0;
    }

    @Override
    public Long getUserRowsCount(UserRowRequest userRowRequest) {
        return null;
    }

    @Override
    public List<User> findUsers(UserRowRequest userRowRequest) {
        return null;
    }

    @Override
    public List<User> findUsersByPattern(String pattern) {
        return null;
    }

    @Override
    public List<User> findOrgUsersByPattern(String pattern, User user) {
        return null;
    }

    @Override
    public AddressDao getAddressDao() {
        return null;
    }

    @Override
    public OrganizationDao getOrganizationDao() {
        return null;
    }

    @Override
    public Long create(User user) {
        Long userId = user.getId();
        if (userId == null) {
            return null;
        }
        Long addressId = getAddressId(user.getAddress());
        Long orgId = getOrgId(user.getOrganization());


        return null;
    }

    @Override
    public Long update(User object) {
        return null;
    }

    @Override
    public Long delete(Long id) {
        return null;
    }

    @Override
    public Long delete(User object) {
        return null;
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    private Session openSession(){
        currentSession = sessionFactory.openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;

    }
}
