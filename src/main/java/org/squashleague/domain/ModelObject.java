package org.squashleague.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * @author jamesdbloom
 */
@MappedSuperclass
public abstract class ModelObject<T> {

    static {
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.MULTI_LINE_STYLE);
    }

    protected transient Logger logger = LoggerFactory.getLogger(getClass());
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModelObject withId(Long id) {
        setId(id);
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ModelObject incrementVersion() {
        if (version != null) {
            version++;
        }
        return this;
    }

    public abstract T merge(T entity);
}
