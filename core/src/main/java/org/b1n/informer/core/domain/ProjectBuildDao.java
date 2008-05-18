package org.b1n.informer.core.domain;

import java.util.List;

import org.b1n.framework.persistence.HibernateEntityDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author Marcio Ribeiro
 * @date Jan 21, 2008
 */
public class ProjectBuildDao extends HibernateEntityDao<ProjectBuild> {
    /**
     * Devolve lista de ultimos builds.
     * @param userId id do usuario.
     * @param hostId id do host.
     * @param projectId id do projeto.
     * @param withTests <code>true</code> se build executou tests, <code>false</code> se nao.
     * @param deploy <code>true</code> se build foi um deploy, <code>false</code> se nao.
     * @param maxResults maximo de resultados.
     * @param offset offset para paginacao.
     * @return lista dos ultimos builds.
     */
    @SuppressWarnings("unchecked")
    public List<ProjectBuild> findLastBuilds(final Long userId, final Long hostId, final Long projectId, final Boolean withTests, final Boolean deploy, final int maxResults, final int offset) {
        final Criteria crit = createCriteria();
        addRestrictions(userId, hostId, projectId, withTests, deploy, crit);
        crit.addOrder(Order.desc("startTime"));
        crit.setMaxResults(maxResults);
        crit.setFirstResult(offset);
        return crit.list();
    }

    /**
     * Devolve contagem de registros com os dados passados.
     * @param userId id do usuario.
     * @param hostId id do host.
     * @param projectId id do projeto.
     * @param withTests <code>true</code> se build executou tests, <code>false</code> se nao.
     * @param deploy <code>true</code> se build foi um deploy, <code>false</code> se nao.
     * @return contagem de registros com os dados passados.
     */
    public Integer getCount(final Long userId, final Long hostId, final Long projectId, final Boolean withTests, final Boolean deploy) {
        final Criteria crit = createCriteria();
        crit.setProjection(Projections.rowCount());
        addRestrictions(userId, hostId, projectId, withTests, deploy, crit);
        return (Integer) crit.uniqueResult();
    }

    /**
     * Metodo auxiliar que adiciona restricoes comuns ao criteria passado.
     * @param userId id do usuario.
     * @param hostId id do host.
     * @param projectId id do projeto.
     * @param withTests <code>true</code> se build executou tests, <code>false</code> se nao.
     * @param deploy <code>true</code> se build foi um deploy, <code>false</code> se nao.
     * @param crit criteria.
     */
    private void addRestrictions(final Long userId, final Long hostId, final Long projectId, final Boolean withTests, final Boolean deploy, final Criteria crit) {
        if (userId != null) {
            crit.add(Restrictions.eq("user.id", userId));
        }
        if (hostId != null) {
            crit.add(Restrictions.eq("host.id", hostId));
        }
        if (projectId != null) {
            crit.add(Restrictions.eq("project.id", projectId));
        }
        if (withTests != null) {
            crit.add(Restrictions.eq("withTests", withTests));
        }
        if (deploy != null) {
            crit.add(Restrictions.eq("deploy", deploy));
        }
    }

}
