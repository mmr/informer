package org.b1n.informer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

/**
 * @author Marcio Ribeiro
 * @date Jan 24, 2008
 */
public class MasterProjectInfo extends BuildInfo {

    /**
     * Construtor.
     * @param project projeto.
     * @param session sessao do maven.
     */
    public MasterProjectInfo(final MavenProject project, final MavenSession session) {
        super(project, session, session.getStartTime());
    }

    /**
     * @return o hostname da máquina.
     */
    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown-host";
        }
    }

    /**
     * @return o ip da maquina.
     */
    public String getHostIp() {
        try {
            return InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            return "unknown-host";
        }
    }

    /**
     * @return o usuario.
     */
    public String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * @return dados sobre o sistema operacional.
     */
    public String getOperatingSystem() {
        final StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("os.name")).append(" ");
        sb.append(System.getProperty("os.version")).append(" ");
        sb.append(System.getProperty("os.arch"));
        return sb.toString();
    }

    /**
     * @return o encoding usado na maquina.
     */
    public String getEncoding() {
        return System.getProperty("file.encoding");
    }

    /**
     * @return o nome e versão da JVM.
     */
    public String getJvm() {
        final StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("java.vm.vendor")).append(" ");
        sb.append(System.getProperty("java.vm.name")).append(" ");
        sb.append(System.getProperty("java.vm.version"));
        return sb.toString();
    }
}
