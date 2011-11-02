/**
 *
 */
package edu.wustl.metadata.util;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsCacheException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.querysuite.querableobject.QueryableEntity;
import edu.wustl.common.querysuite.querableobject.QueryableEntityAttribute;
import edu.wustl.common.querysuite.querableobjectinterface.QueryableObjectInterface;

/**
 * @author gaurav_sawant
 *
 */
public class TestDyExtnObjectCloner extends DynamicExtensionsBaseTestCase
{
    @Override
    protected void setUp() throws DynamicExtensionsCacheException
    {
        super.setUp();
    }

    @Override
    protected void tearDown()
    {
        super.tearDown();
    }

    public void testDyExtnObjectCloner()
    {
         Object obj = null;
        DyExtnObjectCloner cloner = new DyExtnObjectCloner();

        AttributeInterface attribute = new Attribute();
        Long id = 100l;
        String name = "test";
        attribute.setId(id);
        attribute.setName(name);

        QueryableObjectInterface queryObj = new QueryableEntity();
        QueryableEntityAttribute source = new QueryableEntityAttribute(attribute ,queryObj);
        QueryableEntityAttribute clonedObj = cloner.clone(source);
        assertEquals("Comparing Ids",source.getId(), clonedObj.getId());
//        assertEquals("Comparing Names",source.getName(), clonedObj.getName());

    }


}
