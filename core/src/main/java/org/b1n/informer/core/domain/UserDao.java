package org.b1n.informer.core.domain;

import org.b1n.framework.persistence.EntityNotFoundException;
import org.b1n.framework.persistence.SimpleEntityDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * DAO de usuario.
 * @author Marcio Ribeiro
 * @date Jan 21, 2008
 */
public class UserDao extends SimpleEntityDao<User> {
    /**
     * Devolve o usuario com o nome passado.
     * @param userName o nome do usuario.
     * @return o usuario com o nome passado.
     * @throws EntityNotFoundException caso nao encontre.
     */
    @SuppressWarnings("unchecked")
    public User findByUserName(final String userName) throws EntityNotFoundException {
        final Criteria crit = createCriteria();
        crit.add(Restrictions.eq("userName", userName));
        final User user = (User) crit.uniqueResult();
        if (user == null) {
            throw new EntityNotFoundException(User.class);
        }
        return user;
    }
}
