package org.b1n.informer.core.domain;

import org.b1n.framework.persistence.EntityNotFoundException;
import org.b1n.framework.persistence.SimpleEntityDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * DAO de projeto.
 * @author Marcio Ribeiro
 * @date Jan 21, 2008
 */
public class ProjectDao extends SimpleEntityDao<Project> {

    /**
     * Devolve um projeto pela chave groupId, artifactId, version.
     * @param groupId o id do grupo do artefato.
     * @param artifactId o id do artefato.
     * @param version a versao do artefato.
     * @return o projeto.
     * @throws EntityNotFoundException caso nao encontre um projeto para a chave passada.
     */
    public Project findByKey(final String groupId, final String artifactId, final String version) throws EntityNotFoundException {
        final Criteria crit = createCriteria();
        crit.add(Restrictions.eq("groupId", groupId));
        crit.add(Restrictions.eq("artifactId", artifactId));
        crit.add(Restrictions.eq("version", version));

        final Project project = (Project) crit.uniqueResult();
        if (project == null) {
            throw new EntityNotFoundException(Project.class);
        }
        return project;
    }
}
