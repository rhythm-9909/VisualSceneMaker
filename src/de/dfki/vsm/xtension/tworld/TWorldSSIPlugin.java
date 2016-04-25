package de.dfki.vsm.xtension.tworld;

import de.dfki.vsm.xtension.ssi.event.SSIEventArray;
import de.dfki.vsm.model.project.PluginConfig;
import de.dfki.vsm.runtime.interpreter.value.AbstractValue;
import de.dfki.vsm.runtime.project.RunTimeProject;
import de.dfki.vsm.xtension.ssi.SSIRunTimePlugin;
import de.dfki.vsm.xtension.ssi.event.SSIEventObject;
import de.dfki.vsm.xtension.ssi.event.data.SSIEventData;
import de.dfki.vsm.xtension.ssi.event.data.SSIStringData;
import de.dfki.vsm.runtime.interpreter.value.StringValue;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Gregor Mehlmann, Patrick Gebhard
 */
public final class TWorldSSIPlugin extends SSIRunTimePlugin {

    // The map of processes
    private final HashMap<String, Process> mProcessMap = new HashMap();

    public TWorldSSIPlugin(
            final PluginConfig config,
            final RunTimeProject project) {
        super(config, project);
    }

    // Launch SSI plugin
    @Override
    public void launch() {
        final String ssidir = mConfig.getProperty("ssidir");
        final String ssibat = mConfig.getProperty("ssibat");

        // Create the plugin's processes
        try {
            mProcessMap.put(ssibat, Runtime.getRuntime().exec(
                    "cmd /c start " + ssibat + "" + "", null, new File(ssidir)));
        } catch (final Exception exc) {
            mLogger.failure(exc.toString());
        }
        super.launch();
    }

    // Unload SSI plugin
    @Override
    public void unload() {
        super.unload();

        // Wait for pawned processes
        for (final Map.Entry<String, Process> entry : mProcessMap.entrySet()) {
            // Get the process entry
            final String name = entry.getKey();
            final Process process = entry.getValue();
            try {
                // Kill the processes
                final Process killer = Runtime.getRuntime().exec("taskkill /F /IM " + name);
                // Wait for the killer
                killer.waitFor();
                // Print some information 
                mLogger.message("Joining killer " + name + "");
                // Wait for the process
                process.waitFor();
                // Print some information 
                mLogger.message("Joining process " + name + "");
            } catch (final Exception exc) {
                mLogger.failure(exc.toString());
            }
        }
    }

    @Override
    public void handle(final SSIEventArray array) {
        // Print some information 
        //mLogger.message("Handling SSI events " + array);
        for (final SSIEventObject event : array.getEventList()) {
            final SSIEventData obj = event.getData();
            //mLogger.message("Handling SSI event " + obj);
            if (obj instanceof SSIStringData) {
                final TWorldSSIData mSSIData = new TWorldSSIData(
                        ((SSIStringData) array.getEventList().get(0).getData()).toString());

                //mLogger.message("Handling SSI data " + mSSIData);
                final HashMap<String, AbstractValue> values = new HashMap<>();
                values.put("voice_activity", new StringValue(mSSIData.get("voice.activity")));
                values.put("voice_keyword", new StringValue(mSSIData.get("voice.keyword")));
                values.put("voice_praat_pitchmean", new StringValue(mSSIData.get("voice.praat.pitchmean")));
                values.put("voice_praat_pitchsd", new StringValue(mSSIData.get("voice.praat.pitchsd")));
                values.put("voice_praat_speechrate", new StringValue(mSSIData.get("voice.praat.speechrate")));
                values.put("voice_praat_intensity", new StringValue(mSSIData.get("voice.praat.intensity")));
                values.put("head_position_x", new StringValue(mSSIData.get("head.position.x")));
                values.put("head_position_y", new StringValue(mSSIData.get("head.position.y")));
                values.put("head_orientation_roll", new StringValue(mSSIData.get("head.orientation.roll")));
                values.put("head_orientation_pitch", new StringValue(mSSIData.get("head.orientation.pitch")));
                values.put("head_orientation_yaw", new StringValue(mSSIData.get("head.orientation.yaw")));
                values.put("head_movement_nod", new StringValue(mSSIData.get("head.movement.nod")));
                values.put("head_movement_shake", new StringValue(mSSIData.get("head.movement.shake")));
                values.put("body_activity", new StringValue(mSSIData.get("body.activity")));
                values.put("body_energy", new StringValue(mSSIData.get("body.energy")));
                values.put("body_posture_leanfront_detected", new StringValue(mSSIData.get("body.posture.leanfront.detected")));
                values.put("body_posture_leanfront_duration", new StringValue(mSSIData.get("body.posture.leanfront.duration")));
                values.put("body_posture_leanfront_intensity", new StringValue(mSSIData.get("body.posture.leanfront.intensity")));
                values.put("body_posture_leanback_detected", new StringValue(mSSIData.get("body.posture.leanback.detected")));
                values.put("body_posture_leanback_duration", new StringValue(mSSIData.get("body.posture.leanback.duration")));
                values.put("body_posture_leanback_intensity", new StringValue(mSSIData.get("body.posture.leanback.intensity")));
                values.put("body_gesture_armsopen_detected", new StringValue(mSSIData.get("body.gesture.armsopen.detected")));
                values.put("body_gesture_armsopen_duration", new StringValue(mSSIData.get("body.gesture.armsopen.duration")));
                values.put("body_gesture_armsopen_intensity", new StringValue(mSSIData.get("body.gesture.armsopen.intensity")));
                values.put("body_gesture_armscrossed_detected", new StringValue(mSSIData.get("body.gesture.armscrossed.detected")));
                values.put("body_gesture_armscrossed_duration", new StringValue(mSSIData.get("body.gesture.armscrossed.duration")));
                values.put("body_gesture_armscrossed_intensity", new StringValue(mSSIData.get("body.gesture.armscrossed.intensity")));
                values.put("body_gesture_lefthandheadtouch_detected", new StringValue(mSSIData.get("body.gesture.lefthandheadtouch.detected")));
                values.put("body_gesture_lefthandheadtouch_duration", new StringValue(mSSIData.get("body.gesture.lefthandheadtouch.duration")));
                values.put("body_gesture_righthandheadtouch_detected", new StringValue(mSSIData.get("body.gesture.righthandheadtouch.detected")));
                values.put("body_gesture_righthandheadtouch_duration", new StringValue(mSSIData.get("body.gesture.righthandheadtouch.duration")));
                values.put("face_expression_smile_detected", new StringValue(mSSIData.get("face.expression.smile.detected")));
                values.put("face_expression_smile_duration", new StringValue(mSSIData.get("face.expression.smile.duration")));
                values.put("face_expression_smile_intensity", new StringValue(mSSIData.get("face.expression.smile.intensity")));

                //mProject.setVariable("usercues", new StructValue(values));
                for (final Entry<String, AbstractValue> value : values.entrySet()) {
                    if (mProject.hasVariable(value.getKey())) {
                        mProject.setVariable(value.getKey(), value.getValue());
                        //mLogger.success("Setting Variable " + value.getKey() + " to " + value.getValue().getConcreteSyntax());
                    } else {
                        //mLogger.warning("Variable " + value.getKey() + " not defined!");
                    }
                }
            }
        }
    }
}
