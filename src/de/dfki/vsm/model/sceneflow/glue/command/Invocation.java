package de.dfki.vsm.model.sceneflow.glue.command;

import de.dfki.vsm.model.sceneflow.glue.command.invocation.UnblockSceneScript;
import de.dfki.vsm.model.sceneflow.glue.command.invocation.UnblockSceneGroup;
import de.dfki.vsm.model.sceneflow.glue.command.invocation.HistoryClearDeep;
import de.dfki.vsm.model.sceneflow.glue.command.invocation.HistoryClearFlat;
import de.dfki.vsm.model.sceneflow.glue.command.invocation.HistorySetDepth;
import de.dfki.vsm.model.sceneflow.glue.command.invocation.PlayActivity;
import de.dfki.vsm.model.sceneflow.glue.command.invocation.PlayDialogAction;
import de.dfki.vsm.model.sceneflow.glue.command.invocation.PlaySceneGroup;
import de.dfki.vsm.model.sceneflow.glue.command.invocation.StopActivity;
import de.dfki.vsm.util.xml.XMLParseError;
import org.w3c.dom.Element;

/**
 * @author Gregor Mehlmann
 */
public abstract class Invocation extends Command {

    @Override
    public abstract Invocation getCopy();

    public static Invocation parse(final Element element) throws XMLParseError {
        Invocation invocation;
        final String tag = element.getTagName();
        if (tag.equals("StopActivity")) {
            invocation = new StopActivity();
            invocation.parseXML(element);
        } else if (tag.equals("PlayActivity")) {
            invocation = new PlayActivity();
            invocation.parseXML(element);
        } else if (tag.equals("PlayDialogAct")) {
            invocation = new PlayDialogAction();
            invocation.parseXML(element);
        } else if (tag.equals("PlaySceneGroup")) {
            invocation = new PlaySceneGroup();
            invocation.parseXML(element);
        } else if (tag.equals("FreeOneSceneGroup")) {
            invocation = new UnblockSceneGroup();
            invocation.parseXML(element);
        } else if (tag.equals("FreeAllSceneGroups")) {
            invocation = new UnblockSceneScript();
            invocation.parseXML(element);
        } else if (tag.equals("HistoryClearFlat")) {
            invocation = new HistoryClearFlat();
            invocation.parseXML(element);
        } else if (tag.equals("HistoryClearDeep")) {
            invocation = new HistoryClearDeep();
            invocation.parseXML(element);
        } else if (tag.equals("HistorySetDepth")) {
            invocation = new HistorySetDepth();
            invocation.parseXML(element);
        } else {
            invocation = null;
        }
        return invocation;
    }
}
