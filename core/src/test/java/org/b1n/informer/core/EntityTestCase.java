package org.b1n.informer.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.iterators.LoopingIterator;
import org.b1n.framework.persistence.DaoLocator;
import org.b1n.framework.persistence.Entity;
import org.b1n.framework.persistence.EntityDao;
import org.b1n.framework.persistence.EntityNotFoundException;
import org.b1n.framework.persistence.JpaEntity;

/**
 * Entidade basica para teste de persistencia de entidades.
 * @author Marcio Ribeiro
 * @date Jan 25, 2008
 * @param <E> tipo de entidade.
 */
public abstract class EntityTestCase<E extends Entity> extends PersistenceTestCase {
    private Class<E> entityClass;

    private final EntityDao<E> entityDao = DaoLocator.getDao(getEntityClass());

    private RandomTestData randomTestData = new RandomTestData();

    /**
     * Inicia dados aleatorios para teste de persistencia.
     * @throws Exception caso algo de estranho ocorra.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        randomTestData.init();
    }

    /**
     * @return classe de entidade.
     */
    @SuppressWarnings("unchecked")
    protected Class<E> getEntityClass() {
        if (entityClass == null) {
            entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return entityClass;
    }

    /**
     * Compara dados das duas entidades passadas.
     * @param entity entidade criada.
     * @param loadedEntity entidade carregada.
     * @throws Exception caso algo de inesperado ocorra.
     */
    @SuppressWarnings("unchecked")
    protected void compareData(final E entity, final E loadedEntity) throws Exception {
        final Set<Method> getters = new HashSet<Method>();
        Class<E> superClass = getEntityClass();
        while (superClass != JpaEntity.class) {
            for (final Method method : superClass.getDeclaredMethods()) {
                if (Modifier.isPublic(method.getModifiers()) && (method.getName().startsWith("get") || method.getName().startsWith("is"))) {
                    getters.add(method);
                }
            }
            superClass = (Class<E>) superClass.getSuperclass();
        }
        for (final Method method : getters) {
            assertEquals(method.invoke(entity, new Object[] {}), method.invoke(loadedEntity, new Object[] {}));
        }
    }

    /**
     * Testa entidade (insere, altera, remove).
     * @throws Exception caso algo de inesperado ocorra.
     */
    public void testEntity() throws Exception {
        // Insert
        final E entity = entityClass.newInstance();
        fillEntity(entity);
        entity.save();

        E loadedEntity = entityDao.findById(entity.getId());
        compareData(entity, loadedEntity);

        // Update
        fillEntity(entity);
        entity.save();
        loadedEntity = entityDao.findById(entity.getId());
        compareData(entity, loadedEntity);

        // Remove
        entity.remove();
        try {
            entityDao.findById(entity.getId());
            fail("Could not remove entity");
        } catch (final EntityNotFoundException e) {
            // ok!
        }
    }

    /**
     * Cria e devolve instancia de entidade com valores aleatorios nao salva.
     * @param <T> tipo de entidade.
     * @param entityClass classe de entidade.
     * @return entidade nao salva.
     * @throws Exception caso algo de inesperado ocorra.
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> T createUnsavedEntity(final Class<T> entityClass) throws Exception {
        final T entity = entityClass.newInstance();
        fillEntity(entity);
        return entity;
    }

    /**
     * Cria e devolve instancia de entidade com valores aleatorios e salva.
     * @param <T> tipo de entidade.
     * @param entityClass classe de entidade.
     * @return entidade salva.
     * @throws Exception caso nao consiga criar entidade.
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> T createSavedEntity(final Class<T> entityClass) throws Exception {
        final T entity = createUnsavedEntity(entityClass);
        entity.save();
        return entity;
    }

    /**
     * Carrega dados de entidade passada com valores aletorios.
     * @param <T> tipo de entidade.
     * @param entity entidade a ser carregada.
     * @throws Exception caso algo de inesperado ocorra.
     */
    @SuppressWarnings("unchecked")
    protected <T extends Entity> void fillEntity(final T entity) throws Exception {
        Class<T> superClass = (Class<T>) entity.getClass();
        while (superClass != JpaEntity.class) {
            for (final Method method : superClass.getDeclaredMethods()) {
                if (method.getName().startsWith("set") && !method.getName().equals("setId")) {
                    this.invokeSetter(entity, method);
                }
            }
            superClass = (Class<T>) superClass.getSuperclass();
        }
    }

    /**
     * Chama metodo setter em entidade passada.
     * @param <T> tipo de entidade.
     * @param entity entidade.
     * @param method metodo setter.
     * @throws Exception caso algo de inesperado ocorra.
     */
    protected <T extends Entity> void invokeSetter(final T entity, final Method method) throws Exception {
        if (!Modifier.isPublic(method.getModifiers())) {
            return;
        }

        final Class<?> parameterType = method.getParameterTypes()[0];
        final Object parameter = instantiateParameter(parameterType);
        method.invoke(entity, parameter);
    }

    /**
     * Cria instancia de classe do tipo passado.
     * @param parameterType tipo de parametro.
     * @return instancia de classe do tipo passado.
     * @throws Exception caso algo de inesperado ocorra.
     */
    @SuppressWarnings("unchecked")
    protected Object instantiateParameter(final Class parameterType) throws Exception {
        Object randomValue = randomTestData.get(parameterType);

        if (randomValue != null) {
            return randomValue;
        }

        if (Entity.class.isAssignableFrom(parameterType)) {
            return createSavedEntity(parameterType);
        }

        return createInstance(parameterType);
    }

    /**
     * Cria instancia da classe passada.
     * @param clazz classe a ter instancia criada.
     * @return instancia de classe passada.
     * @throws Exception caso algo de inesperado ocorra.
     */
    @SuppressWarnings("unchecked")
    protected Object createInstance(final Class clazz) throws Exception {
        final Constructor constructor = clazz.getDeclaredConstructor((Class[]) null);
        if (!Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) {
            constructor.setAccessible(true);
        }
        return constructor.newInstance(new Object[0]);
    }

    /**
     * Classe com dados aleatorios para teste.
     * @author Marcio Ribeiro
     * @date May 18, 2008
     */
    private class RandomTestData {
        private static final int MAX_ITEMS = 100;

        private static final int MAX_RANDOM = 666;

        private Map<Class<?>, Iterator<?>> data = null;

        /**
         * Devolve um dado aleatorio do tipo passado.
         * @param parameterType tipo de dado.
         * @return dado aleatorio para teste.
         */
        Object get(final Class<?> parameterType) {
            if (parameterType == Long.TYPE || Long.class.isAssignableFrom(parameterType) || parameterType == Integer.TYPE || Integer.class.isAssignableFrom(parameterType)) {
                return data.get(Long.TYPE).next();
            }

            if (parameterType == Double.TYPE || parameterType == Float.TYPE || Double.class.isAssignableFrom(parameterType) || Float.class.isAssignableFrom(parameterType)) {
                return data.get(Double.TYPE).next();
            }

            if (String.class.isAssignableFrom(parameterType)) {
                return data.get(String.class).next();
            }

            if (parameterType == Boolean.TYPE || Boolean.class.isAssignableFrom(parameterType)) {
                if (((int) Math.random() * 2) == 1) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }

            return null;
        }

        /**
         * Popula dados aleatorios para teste.
         */
        void init() {
            data = new HashMap<Class<?>, Iterator<?>>();

            Set<Long> longs = new HashSet<Long>();
            Set<Double> doubles = new HashSet<Double>();
            Set<String> strings = new HashSet<String>();

            for (int i = 0; i < MAX_ITEMS; i++) {
                longs.add((long) (MAX_RANDOM * Math.random()));
                doubles.add(MAX_RANDOM * Math.random());
                strings.add(String.valueOf(MAX_RANDOM * Math.random()));
            }

            data.put(Long.TYPE, new LoopingIterator(longs));
            data.put(Double.TYPE, new LoopingIterator(doubles));
            data.put(String.class, new LoopingIterator(strings));
        }
    }
}
