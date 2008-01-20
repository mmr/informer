package org.b1n.informer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Info Retriever.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public class InfoRetriever {
    /**
     * Devolve o hostname da máquina.
     * @return o hostname da máquina.
     */
    public String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown-host";
        }
    }

    /**
     * Devolve a system property <code>user.name</code>.
     * @return a system property <code>user.name</code>.
     */
    public String getUsername() {
        return System.getProperty("user.name");
    }

    /**
     * Devolve a system property <code>file.encoding</code>.
     * @return a system property <code>file.encoding</code>.
     */
    public String getFileEncoding() {
        return System.getProperty("file.encoding");
    }

    /**
     * Devolve o nome e versão da JVM.
     * @return o nome e versão da JVM.
     */
    public String getJvm() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("java.vm.vendor")).append(" ");
        sb.append(System.getProperty("java.vm.name")).append(" ");
        sb.append(System.getProperty("java.vm.version"));
        return sb.toString();
    }
}
