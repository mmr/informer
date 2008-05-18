package org.b1n.informer.receiver.logic;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.b1n.framework.persistence.DaoLocator;
import org.b1n.informer.core.domain.ProjectBuild;
import org.b1n.informer.core.domain.ProjectBuildDao;
import org.vraptor.annotations.Component;
import org.vraptor.annotations.Out;
import org.vraptor.annotations.Parameter;

/**
 * @author Marcio Ribeiro
 * @date Feb 3, 2008
 */
@Component("lastBuilds")
public class LastBuildsLogic {
    private static final int MAX = 100;

    @Parameter
    private Long userId;

    @Parameter
    private Long hostId;

    @Parameter
    private Long projectId;

    @Parameter
    private Boolean withTests;

    @Parameter
    private Boolean deploy;

    @Parameter
    private int max;

    @Parameter
    private int offset;

    //    @Parameter
    //    private Integer page;

    @Out
    private List<Map.Entry<String, List<ProjectBuild>>> buildsByHour;

    @SuppressWarnings("unused")
    @Out
    private int count;

    /**
     * Carrega lista de builds para ser mostrada.
     */
    public void show() {
        if (max == 0) {
            max = MAX;
        }
        if (buildsByHour == null) {
            organizeBuildsByHour();
        }
    }

    /**
     * Organiza builds por hora.
     */
    private void organizeBuildsByHour() {
        final Map<String, List<ProjectBuild>> buildsMap = new LinkedHashMap<String, List<ProjectBuild>>();
        final ProjectBuildDao buildDao = DaoLocator.getDao(ProjectBuild.class);
        final List<ProjectBuild> bs = buildDao.findLastBuilds(userId, hostId, projectId, withTests, deploy, max, offset);
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2);
        for (final ProjectBuild b : bs) {
            final DateFormat dateFormat = new SimpleDateFormat("MM/dd hh");
            final String key = dateFormat.format(b.getStartTime()) + "h";
            if (!buildsMap.containsKey(key)) {
                buildsMap.put(key, new ArrayList<ProjectBuild>());
            }
            buildsMap.get(key).add(b);
        }
        buildsByHour = new ArrayList<Map.Entry<String, List<ProjectBuild>>>(buildsMap.entrySet());
        count = buildDao.getCount(userId, hostId, projectId, withTests, deploy);
    }
}
