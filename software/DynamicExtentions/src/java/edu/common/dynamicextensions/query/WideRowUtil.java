package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.ArithExpressionNode;
import edu.common.dynamicextensions.query.ast.CurrentDateNode;
import edu.common.dynamicextensions.query.ast.DateDiffFuncNode;
import edu.common.dynamicextensions.query.ast.DateIntervalNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.LiteralValueListNode;
import edu.common.dynamicextensions.query.ast.LiteralValueNode;

public class WideRowUtil {
	// returns {tabAlias, pk}
    public static String[] getTabAliasPk(JoinTree rootNode, ExpressionNode exprNode) {    	
    	if (exprNode instanceof LiteralValueNode || 
    		exprNode instanceof LiteralValueListNode ||	
    		exprNode instanceof DateIntervalNode ||
    		exprNode instanceof CurrentDateNode) {
    		return null;
    	} else if (exprNode instanceof FieldNode) {
    		FieldNode fieldNode = (FieldNode)exprNode;
    		return new String[] {fieldNode.getTabAlias(), fieldNode.getCtrl().getContainer().getPrimaryKey()};
    	} else if (exprNode instanceof DateDiffFuncNode) {
    		DateDiffFuncNode dateDiffNode = (DateDiffFuncNode)exprNode;
    		return getTabAliasPk(rootNode, dateDiffNode.getLeftOperand(), dateDiffNode.getRightOperand());
    	} else if (exprNode instanceof ArithExpressionNode) {
    		ArithExpressionNode arithExprNode = (ArithExpressionNode)exprNode;
    		return getTabAliasPk(rootNode, arithExprNode.getLeftOperand(), arithExprNode.getRightOperand());    		
    	}
    	
    	throw new RuntimeException("Unknown expression node type: " + exprNode);
    }
    
    private static String[] getTabAliasPk(JoinTree rootNode, ExpressionNode leftOperand, ExpressionNode rightOperand) {
		String[] leftTabAlias = getTabAliasPk(rootNode, leftOperand);
		String[] rightTabAlias = getTabAliasPk(rootNode, rightOperand);
		
		if (leftTabAlias == null && rightTabAlias == null) {
			return new String[] {rootNode.getAlias(), rootNode.getForm().getPrimaryKey()};
		} else if (leftTabAlias == null && rightTabAlias != null) {
			return rightTabAlias;
		} else if (leftTabAlias != null && rightTabAlias == null) {
			return leftTabAlias;
		} else if (leftTabAlias[0].equals(rightTabAlias[0])) {
			return leftTabAlias;    			
		} else {
			JoinTree leftNode = rootNode.getNode(leftTabAlias[0]);
			JoinTree rightNode = rootNode.getNode(rightTabAlias[0]);
			
			if (leftNode.isAncestorOf(rightNode)) {
				return rightTabAlias;
			} else if (rightNode.isAncestorOf(leftNode)) {
				return leftTabAlias;
			} else {
				JoinTree commonAncestor = rootNode.getCommonAncestor(leftNode, rightNode);
				return new String[] {commonAncestor.getAlias(), commonAncestor.getForm().getPrimaryKey()};
			}    			
		}    	
    }
}
