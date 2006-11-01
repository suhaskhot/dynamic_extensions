/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
package edu.common.dynamicextensions.entitymanager;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;



public class TestEntityManager extends DynamicExtensionsBaseTestCase
{

    public TestEntityManager()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public TestEntityManager(String arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    
    protected void setUp()
    {
       super.setUp();       
    }

    protected void tearDown()
    {
       super.tearDown();       
    }

    
      
    public void testCreateEntity() {
        try
        {
            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            entity = (Entity) EntityManager.getInstance().createEntity(entity);
            Entity newEntity = (Entity) EntityManager.getInstance().getEntityByIdentifier(entity.getId().toString());
            //Checking whether metadata information is saved properly or not.
            assertEquals(entity.getName(),newEntity.getName());            
            String tableName = entity.getTableProperties().getName();
            String query = "Select * from " + tableName;
            executeQuery(query);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail("Exception occured");
        }
        
        
        
    }

 

}
