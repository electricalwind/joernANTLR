package cfg.C;

import cfg.CFG;
import cfg.CFGEdge;
import cfg.nodes.CFGExceptionNode;
import cfg.nodes.CFGNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CCFG extends CFG {
    private List<CFGNode> breakStatements;
    private List<CFGNode> continueStatements;
    private List<CFGNode> returnStatements;
    private HashMap<CFGNode, String> gotoStatements;
    private HashMap<String, CFGNode> labels;
    private CFGExceptionNode exceptionNode;

    public CCFG() {
        super();
        setBreakStatements(new LinkedList<CFGNode>());
        setContinueStatements(new LinkedList<CFGNode>());
        setReturnStatements(new LinkedList<CFGNode>());
        setGotoStatements(new HashMap<CFGNode, String>());
        setLabels(new HashMap<String, CFGNode>());
    }

    public void setExceptionNode(CFGExceptionNode node) {
        this.exceptionNode = node;
        addVertex(node);
    }

    public List<CFGNode> getBreakStatements() {
        return breakStatements;
    }

    public void setBreakStatements(List<CFGNode> breakStatements) {
        this.breakStatements = breakStatements;
    }

    public List<CFGNode> getContinueStatements() {
        return continueStatements;
    }

    public void setContinueStatements(List<CFGNode> continueStatements) {
        this.continueStatements = continueStatements;
    }

    public HashMap<String, CFGNode> getLabels() {
        return labels;
    }

    public void setLabels(HashMap<String, CFGNode> labels) {
        this.labels = labels;
    }

    public HashMap<CFGNode, String> getGotoStatements() {
        return gotoStatements;
    }

    public void setGotoStatements(HashMap<CFGNode, String> gotoStatements) {
        this.gotoStatements = gotoStatements;
    }

    public List<CFGNode> getReturnStatements() {
        return returnStatements;
    }

    public void setReturnStatements(List<CFGNode> returnStatements) {
        this.returnStatements = returnStatements;
    }

    public void addBlockLabel(String label, CFGNode block) {
        getLabels().put(label, block);
    }

    public void addBreakStatement(CFGNode statement) {
        getBreakStatements().add(statement);
    }

    public void addContinueStatement(CFGNode statement) {
        getContinueStatements().add(statement);
    }

    public void addGotoStatement(CFGNode gotoStatement, String gotoTarget) {
        getGotoStatements().put(gotoStatement, gotoTarget);
    }

    public void addReturnStatement(CFGNode returnStatement) {
        getReturnStatements().add(returnStatement);
    }

    public CFGNode getBlockByLabel(String label) {
        CFGNode block = getLabels().get(label);
        if (block == null) {
            System.err.println("warning : can not find block for label "
                    + label);
            return getErrorNode();
        }
        return block;
    }

    @Override
    public void addCFG(CFG o) {
        super.addCFG(o);

        if (!(o instanceof CCFG))
            return;

        CCFG otherCFG = (CCFG) o;

        getParameters().addAll(otherCFG.getParameters());
        getBreakStatements().addAll(otherCFG.getBreakStatements());
        getContinueStatements().addAll(otherCFG.getContinueStatements());
        getReturnStatements().addAll(otherCFG.getReturnStatements());
        getGotoStatements().putAll(otherCFG.getGotoStatements());
        getLabels().putAll(otherCFG.getLabels());
        if (this.hasExceptionNode() && otherCFG.hasExceptionNode()) {
            CFGExceptionNode oldExceptionNode = getExceptionNode();
            CFGExceptionNode newExceptionNode = new CFGExceptionNode();
            setExceptionNode(newExceptionNode);
            addEdge(oldExceptionNode, newExceptionNode,
                    CFGEdge.UNHANDLED_EXCEPT_LABEL);
            addEdge(otherCFG.getExceptionNode(), newExceptionNode,
                    CFGEdge.UNHANDLED_EXCEPT_LABEL);
        } else if (otherCFG.hasExceptionNode()) {
            setExceptionNode(otherCFG.getExceptionNode());
        }

    }

    public CFGExceptionNode getExceptionNode() {
        return this.exceptionNode;
    }

    public boolean hasExceptionNode() {
        return this.exceptionNode != null;
    }

}
