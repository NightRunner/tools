package org.nightrunner.tools.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * Created by NightRunner on 2015-12-10.
 */
public class BaseEntityUtil {

    public static <T> List<T> getShouldBeDeletes(Collection<T> oldEntities, Collection<T> newEntities) {
        //TODO
        throw new UnsupportedOperationException();
    }

    public static <T> List<T> getShouldBeSaves(Collection<T> oldEntities, Collection<T> newEntities) {
        //TODO
        throw new UnsupportedOperationException();
    }

    public static <KEY, VALUE> Map<KEY, VALUE> getEntityMap(Collection<VALUE> entities,
                                                            ValueGetter<VALUE, KEY> valueGetter) {

        Map<KEY, VALUE> entityMap = new HashMap<KEY, VALUE>();

        if (CollectionUtils.isEmpty(entities)) {
            return entityMap;
        }

        for (VALUE data : entities) {
            try {
                KEY key = valueGetter.getValue(data);
                entityMap.put(key, data);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return entityMap;
    }

    public static <VALUE, RESULT> List<RESULT> getList(Collection<VALUE> entities,
                                                       ValueGetter<VALUE, RESULT> valueGetter) {
        List<RESULT> result = new ArrayList<RESULT>();
        if (CollectionUtils.isEmpty(entities)) {
            return result;
        }

        for (VALUE entity : entities) {
            try {
                RESULT value = valueGetter.getValue(entity);
                result.add(value);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return result;
    }

    public static <VALUE, RESULT> Set<RESULT> getSet(Collection<VALUE> entities,
                                                     ValueGetter<VALUE, RESULT> valueGetter) {
        Set<RESULT> result = new HashSet<RESULT>();
        if (CollectionUtils.isEmpty(entities)) {
            return result;
        }

        for (VALUE entity : entities) {
            try {
                RESULT value = valueGetter.getValue(entity);
                result.add(value);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return result;
    }

}
