package edu.stanford.bmir.protege.web.shared.crud;


import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class EntityShortForm implements Serializable, IsSerializable {

    private String shortForm;

    private EntityShortForm() {
    }

    public EntityShortForm(String shortForm) {
        this.shortForm = checkNotNull(shortForm);
    }



    public static EntityShortForm get(String shortForm) {
        return new EntityShortForm(shortForm);
    }

    public String getShortForm() {
        return shortForm;
    }

    @Override
    public int hashCode() {
        return "EntityShortForm".hashCode() + shortForm.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EntityShortForm)) {
            return false;
        }
        EntityShortForm other = (EntityShortForm) obj;
        return this.shortForm.equals(other.shortForm);
    }
}
