/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.vsm.xtension.tricatworld.xml.command.object.action;

import de.dfki.vsm.util.ios.IOSIndentWriter;
import de.dfki.vsm.util.xml.XMLParseError;
import de.dfki.vsm.util.xml.XMLParseable;
import de.dfki.vsm.util.xml.XMLWriteError;
import de.dfki.vsm.util.xml.XMLWriteable;
import org.w3c.dom.Element;

/**
 *
 * @author Patrick Gebhard
 *
 */
public class FocalLength extends TriCatWorldActObject implements XMLParseable, XMLWriteable {

    String mValue = "";
    String mBlendTime = "";

    public FocalLength(String value, String blendtime) {
        mName = "setfieldofview";
        mValue = value;
        mBlendTime = blendtime;
    }

    public FocalLength() {
    }

    @Override
    public void writeXML(IOSIndentWriter out) throws XMLWriteError {
        out.push().println("<Action name=\"" + mName + "\" id=\"" + mId + "\" value=\"" + mValue + "\" blendtime=\"" + mBlendTime + "\"/>");
    }

    @Override
    public void parseXML(final Element element) throws XMLParseError {
        mName = element.getAttribute("name");
        mValue = element.getAttribute("value");
        mBlendTime = element.getAttribute("blendtime");
        mId = element.getAttribute("id");
    }
}
