package org.b1n.informer.core.domain;

import org.b1n.framework.persistence.EntityNotFoundException;
import org.b1n.framework.persistence.SimpleEntityDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * DAO de Host.
 * @author Marcio Ribeiro
 * @date Jan 21, 2008
 */
public class HostDao extends SimpleEntityDao<Host> {
    /**
     * Devolve o host com o nome passado.
     * @param hostName o nome do host.
     * @return o host com o nome passado.
     * @throws EntityNotFoundException caso nao encontre.
     */
    public Host findByHostName(final String hostName) throws EntityNotFoundException {
        final Criteria crit = createCriteria();
        crit.add(Restrictions.eq("hostName", hostName));
        final Host host = (Host) crit.uniqueResult();
        if (host == null) {
            throw new EntityNotFoundException(Host.class);
        }
        return host;
    }
}
