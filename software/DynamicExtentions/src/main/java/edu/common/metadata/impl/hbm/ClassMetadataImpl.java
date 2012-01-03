package edu.common.metadata.impl.hbm;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.tuple.EntityMetamodel;

import edu.common.metadata.ClassMetadata;
import edu.common.metadata.PropertyMetadata;
import edu.common.metadata.util.ReflectionUtil;


/**
 *
 * @author Vinayak Pawar (vinayak.pawar@krishagni.com)
 */
public class ClassMetadataImpl implements ClassMetadata {
    private AbstractEntityPersister persister;
           
    private boolean isAbstract;
    
    private EntityMetamodel entityMetamodel;
        
    private PropertyMetadata idMetadata;
    
    private Map<String, PropertyMetadata> propertiesMetadata = new HashMap<String, PropertyMetadata>();
    
    public ClassMetadataImpl(AbstractEntityPersister persister) {
        try {
            this.persister = persister;   
            
            isAbstract = ReflectionUtil.isAbstract(persister.getEntityName());
            entityMetamodel = getEntityMetamodel(persister);
            initialiazePropertiesMetadata();            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }        
    }
    
    public String getClassName() {
        return persister.getEntityName();
    }
    
    public Set<String> getSubClassNames() {
        return entityMetamodel.getSubclassEntityNames();
    }
    
    public String getTableName() {
        return persister.getTableName();
    }
    
    public PropertyMetadata getIdMetadata() {
        return idMetadata;
    }
    
    public boolean isAbstractClass() {
        return isAbstract;
    }
    
    public String getParentClassName() {
        return entityMetamodel.getSuperclass();
    }
    
    public Collection<PropertyMetadata> getPropertiesMetadata() {
        return propertiesMetadata.values();
    }
        
    private void initialiazePropertiesMetadata() {
        idMetadata = new PropertyMetadataImpl(this, persister.getIdentifierPropertyName(), persister, true);
        for (String propertyName : persister.getPropertyNames()) {
            propertiesMetadata.put(propertyName,new PropertyMetadataImpl(this, propertyName, persister));
        }
    }
    
    private EntityMetamodel getEntityMetamodel(AbstractEntityPersister persister) {
        Class klass = persister.getClass();
        while (klass != null) {
            try {
                Method method = klass.getDeclaredMethod("getEntityMetamodel", null);
                method.setAccessible(true);
                return (EntityMetamodel) method.invoke(persister, null);
            } catch (NoSuchMethodException nsme) {
                klass = klass.getSuperclass();
            } catch (Exception e) {
                throw new RuntimeException("Error obtaining entity metamodel", e);
            }
        }
        
        throw new RuntimeException("Entity metamodel information not present for " + persister.getEntityName());
    }

	@Override
	public PropertyMetadata getProperty(String name)
	{
		return propertiesMetadata.get(name);
	}
}