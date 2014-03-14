package org.nahual.utils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.internal.CriteriaImpl;
import org.apache.commons.collections.CollectionUtils;

import java.util.Iterator;

public abstract class StsContainerFilter implements Container.Filter {
    private final Object propertyId;

    public StsContainerFilter(Object propertyId) {
        this.propertyId = propertyId;
    }

    public Object getPropertyId() {
        return propertyId;
    }

    public Criterion getFieldCriterion(String fullPropertyName) {
        return null;
    }

    /**
     * Aca se pueden agregar alias
     *
     * @param criteria
     */
    protected Criteria customizeCriteria(Criteria criteria) {
        return criteria;
    }

    public Criterion getCriterion(final String idName) {
        return idName == null
                ? getFieldCriterion(getPropertyId().toString())
                : getFieldCriterion(idName + "." + getPropertyId());
    }

    protected boolean containsSubcriteria(final String path, final Criteria criteria) {
        if (criteria instanceof CriteriaImpl) {
            final Iterator subcriterias = ((CriteriaImpl) criteria).iterateSubcriteria();
            return CollectionUtils.exists(IteratorUtils.toList(subcriterias), new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    return ((CriteriaImpl.Subcriteria) object).getPath().equals(path);
                }
            });
        } else {
            return false;
        }
    }

    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        return false;
    }
}