package edu.wustl.cab2b.server.path;

import java.sql.Connection;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.cab2b.server.util.SQLQueryUtil;

/**
 * Collection of methods relation to database operations on the table INTER_MODEL_ASSOCIATION.
 * @author srinath_k
 */
public class InterModelConnectionBizLogic {

	/**
	 * Saves intermodel connection
	 * @param attribute1
	 * @param attribute2
	 */
    public void saveInterModelConnection(AttributeInterface attribute1, AttributeInterface attribute2) {
        saveInterModelConnection(new InterModelConnection(attribute1, attribute2));
    }

    /**
     * Utility method. Saves intermodel connection
     * @param imc
     */
    public void saveInterModelConnection(InterModelConnection imc) {
        if (PathFinder.getInstance().isInterModelConnectionExist(imc)) {
            return;
        }
        Connection conn = ConnectionUtil.getConnection();
        long nextId = PathBuilder.getNextAssociationId(2, conn);
        saveInterModelConnection(imc, nextId, conn);
        saveInterModelConnection(imc.mirror(), nextId + 1, conn);
        ConnectionUtil.close(conn);
    }

    private void saveInterModelConnection(InterModelConnection imc, long identifier, Connection conn) {
        StringBuffer sql = new StringBuffer("insert into inter_model_association(association_id, left_entity_id, left_attribute_id, right_entity_id, right_attribute_id) values (");
        sql.append(identifier).append(',').append(imc.getLeftEntityId());
        sql.append(',').append(imc.getLeftAttributeId()).append(',');
        sql.append(imc.getRightEntityId()).append(',');
        sql.append(imc.getRightAttributeId()).append(");");
        SQLQueryUtil.executeUpdate(sql.toString(), conn);

        StringBuffer sqlQuery = new StringBuffer("insert into association(association_id, association_type) values (");
        sqlQuery.append(identifier).append(',');
        sqlQuery.append(AssociationType.INTER_MODEL_ASSOCIATION.getValue()).append(");");
        SQLQueryUtil.executeUpdate(sqlQuery.toString(), conn);
        // TODO transaction??

        PathFinder.getInstance().addInterModelConnection(imc);
    }
}
