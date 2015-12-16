package de.dfki.vsm.model.sceneflow.language.command.expression;

//~--- non-JDK imports --------------------------------------------------------
import de.dfki.vsm.model.sceneflow.language.command.Expression;
import de.dfki.vsm.util.ios.IOSIndentWriter;
import de.dfki.vsm.util.xml.XMLParseAction;
import de.dfki.vsm.util.xml.XMLParseError;
import de.dfki.vsm.util.xml.XMLWriteError;

import org.w3c.dom.Element;

/**
 * @author Not me
 */
public class UnaryExpression extends Expression {

    private Expression mExp;
    private Operator mOperator;

    public enum Operator {

        Neg, Not,
        Random,
        RemoveFirst, RemoveLast, First, Last, Clear, Empty, Size
    }

    public UnaryExpression() {
        mExp = null;
        mOperator = null;
    }

    public UnaryExpression(Expression exp, Operator operator) {
        mExp = exp;
        mOperator = operator;
    }

    public void setExp(Expression value) {
        mExp = value;
    }

    public Expression getExp() {
        return mExp;
    }

    public void setOperator(Operator value) {
        mOperator = value;
    }

    public Operator getOperator() {
        return mOperator;
    }
//
//    @Override
//    public ExpType getExpType() {
//        return ExpType.UNARYEXP;
//    }

    @Override
    public String getAbstractSyntax() {
        return "UnaryExp( " + ((mOperator != null)
                ? mOperator.name()
                : "") + " , " + ((mExp != null)
                ? mExp.getAbstractSyntax()
                : "") + " )";
    }

    @Override
    public String getConcreteSyntax() {
        String opString = "";
        String exp = (mExp != null)
                ? mExp.getConcreteSyntax()
                : "";

        if (mOperator == null) {
            return "";
        }

        switch (mOperator) {
            case Random:
                opString = "Random( " + exp + " )";
                break;
            case RemoveFirst:
                opString = "RemoveFirst( " + exp + " )";
                break;
            case RemoveLast:
                opString = "RemoveLast( " + exp + " )";
                break;
            case First:
                opString = "First( " + exp + " )";
                break;
            case Last:
                opString = "Last( " + exp + " )";
                break;
            case Clear:
                opString = "Clear( " + exp + " )";
                break;
            case Empty:
                opString = "Empty( " + exp + " )";
                break;
            case Size:
                opString = "Size( " + exp + " )";
                break;
            case Neg:
                opString = "- " + exp;
                break;
            case Not:
                opString = "! " + exp;
                break;
        }

        return opString;
    }

    @Override
    public String getFormattedSyntax() {
        String opString = "";
        String exp = (mExp != null)
                ? mExp.getFormattedSyntax()
                : "";

        if (mOperator == null) {
            return "";
        }

        switch (mOperator) {
            case Random:
                opString = "#p#Random( " + exp + " )";
                break;
            case RemoveFirst:
                opString = "#p#RemoveFirst( " + exp + " )";
                break;
            case RemoveLast:
                opString = "#p#RemoveLast( " + exp + " )";
                break;
            case First:
                opString = "#p#First( " + exp + " )";
                break;
            case Last:
                opString = "#p#Last( " + exp + " )";
                break;
            case Clear:
                opString = "#p#Clear( " + exp + " )";
                break;
            case Empty:
                opString = "#p#Empty( " + exp + " )";
                break;
            case Size:
                opString = "#p#Size( " + exp + " )";
                break;
            case Neg:
                opString = "#p#- " + exp;
                break;
            case Not:
                opString = "#p#! " + exp;
                break;
        }

        return opString;
    }

    public UnaryExpression getCopy() {
        return new UnaryExpression(mExp.getCopy(), mOperator);
    }

    public void writeXML(IOSIndentWriter out) throws XMLWriteError {
        out.println("<" + mOperator.name() + ">").push();
        mExp.writeXML(out);
        out.pop().println("</" + mOperator.name() + ">");
    }

    public void parseXML(Element element) throws XMLParseError {
        mOperator = Operator.valueOf(element.getTagName());
        XMLParseAction.processChildNodes(element, new XMLParseAction() {
            public void run(Element element) throws XMLParseError {
                mExp = Expression.parse(element);
            }
        });
    }
}