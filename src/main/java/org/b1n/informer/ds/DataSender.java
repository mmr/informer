package org.b1n.informer.ds;

import java.util.Map;

/**
 * Envia dados para servidor.
 * @author Marcio Ribeiro
 * @date Jan 19, 2008
 */
public interface DataSender {
    /** End action. */
    public static final String END_ACTION = "end";

    /** Start action. */
    public static final String START_ACTION = "start";

    /** Start build. */
    public static final String START_BUILD_ACTION = "startBuild";

    /** Start module. */
    public static final String START_MODULE_ACTION = "startModule";

    /** End module. */
    public static final String END_MODULE_ACTION = "endModule";

    /** End build. */
    public static final String END_BUILD_ACTION = "endBuild";

    /**
     * Envia evento de inicio de build para servidor.
     * @param data dados.
     * @return identificador de build.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     */
    long sendStartBuild(Map<String, String> data) throws CouldNotSendDataException;

    /**
     * Envia evento de inicio de build para servidor.
     * @param data dados.
     * @return identificador de modulo.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     */
    long sendStartModule(Map<String, String> data) throws CouldNotSendDataException;

    /**
     * Envia evento de fim de build de modulo.
     * @param data dados.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     */
    void sendEndModule(Map<String, String> data) throws CouldNotSendDataException;

    /**
     * Envia evento de fim de build para servidor.
     * @param data dados.
     * @throws CouldNotSendDataException caso nao consiga enviar dados.
     */
    void sendEndBuild(Map<String, String> data) throws CouldNotSendDataException;
}
