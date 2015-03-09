package de.dfki.vsm.api;

import de.dfki.vsm.runtime.Process;
import de.dfki.vsm.model.project.ProjectData;
import de.dfki.vsm.model.configs.PlayerConfig;
import de.dfki.vsm.model.sceneflow.SceneFlow;
import de.dfki.vsm.runtime.Environment;
import de.dfki.vsm.runtime.RunTime;
import de.dfki.vsm.runtime.player.SceneGroupPlayer;
import de.dfki.vsm.runtime.value.StringValue;
import de.dfki.vsm.util.jpl.JPLEngine;
import de.dfki.vsm.util.jpl.JPLResult;
import de.dfki.vsm.util.jpl.JPLUtility;
import de.dfki.vsm.util.log.LOGDefaultLogger;
import de.dfki.vsm.util.log.LOGNovaFileLogger;
import de.dfki.vsm.util.log.LOGSSISockLogger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gregor Mehlmann
 */
public abstract class VSMScenePlayer implements SceneGroupPlayer {

    // The VSM Runtime Environment
    protected final RunTime mVSM3RunTime
            = RunTime.getInstance();
    // The System File Logger
    protected final LOGDefaultLogger mVSM3Log
            = LOGDefaultLogger.getInstance();
    // A Nova Logger Instance
    protected final LOGNovaFileLogger mNovaLog
            = LOGNovaFileLogger.getInstance();
    // A Nova Logger Instance
    protected final LOGSSISockLogger mSockLog
            = LOGSSISockLogger.getInstance("127.0.0.1", 4000);
    // The ScenePlayer Config
    protected final PlayerConfig mPlayerConfig;
    // The SceneMaker Project
    protected final ProjectData mProjectData;
    // The SceneMaker Project
    protected final SceneFlow mSceneFlow;
    // The System Timer Thead  
    protected volatile long mStartupTime;
    protected volatile long mCurrentTime;
    protected VSMSystemTimer mSystemTimer;
    // The Query Handler
    protected VSMQueryHandler mQueryHandler;
    // The Waiting Tasks 
    protected final HashMap<String, Thread> mWaitingThreadQueue = new HashMap<>();
    // The Agent Clients    
    protected HashMap<String, VSMAgentClient> mAgentClientMap = new HashMap<>();

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    protected VSMScenePlayer(final ProjectData project) {
        // Init SceneMaker 3 Project 
        mProjectData = project;
        // Init The SceneMaker Sceneflow
        mSceneFlow = project.getSceneFlow();
        // Init Scene Player Config
        mPlayerConfig = project.getScenePlayerProperties();
        // Print Debug Information
        mVSM3Log.message("Creating Generic VSM Scenen Player");
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void launch() {
        // Print Debug Information
        mVSM3Log.message("Launching Generic VSM Scenen Player");
        // Initialize The Properties
        final String numagent = mPlayerConfig.property("vsm.agent.number");
        for (int i = 0; i < Integer.parseInt(numagent); i++) {
            // Get Agent's Name, Host And Port
            final String name = mPlayerConfig.property("vsm.agent." + i + ".name");
            final String uaid = mPlayerConfig.property("vsm.agent." + i + ".uaid");
            final String host = mPlayerConfig.property("vsm.agent." + i + ".host");
            final String port = mPlayerConfig.property("vsm.agent." + i + ".port");
            // Print Out The Properties
            mVSM3Log.message(""
                    + "Agent-Name:" + name + "\r\n"
                    + "Agent-Uaid:" + uaid + "\r\n"
                    + "Agent-Host:" + host + "\r\n"
                    + "Agent-Port:" + port);
            // Create A Client For This Agent
            VSMAgentClient client = new VSMAgentClient(this, name, uaid, host, Integer.parseInt(port));
            // Add The Client To Map
            mAgentClientMap.put(name, client);
            // Now Start The Client
            client.start();
            // Print Debug Information
            mVSM3Log.message("Starting Generic VSM Agent Client '" + name + "' With Id '" + uaid + "' On '" + host + ":" + port + "'");
        }
        // Initialize The Properties
        final String swilhost = mPlayerConfig.property("swi.socket.local.host");
        final String swilport = mPlayerConfig.property("swi.socket.local.port");
        final String swirhost = mPlayerConfig.property("swi.socket.remote.host");
        final String swirport = mPlayerConfig.property("swi.socket.remote.port");
        final String swirconn = mPlayerConfig.property("swi.socket.remote.flag");
        final String swilbase = mPlayerConfig.property("swi.socket.local.base");
        // Print Out The Properties
        mVSM3Log.message(""
                + "SWI-Socket-Local-Host :" + swilhost + "\r\n"
                + "SWI-Socket-Remote-Host :" + swirhost + "\r\n"
                + "SWI-Socket-Local-Port :" + swilport + "\r\n"
                + "SWI-Socket-Remote-Port :" + swirport + "\r\n"
                + "SWI-Socket-Remote-Flag :" + swirconn + "\r\n"
                + "SWI-Socket-Base-Files :" + swilbase + "");
        // Initialize the JPL Engine
        JPLEngine.init();
        // Load The Prolog Program
        JPLEngine.load(swilbase + "/*.pl");
        // Create The Query Handler
        mQueryHandler = new VSMQueryHandler(this);
        // Initialize The System Timer
        mSystemTimer = new VSMSystemTimer(this, 10);
        //Initialize Query Handler
        mQueryHandler.init(swilhost, Integer.parseInt(swilport),
                swirhost, Integer.parseInt(swirport),
                Boolean.parseBoolean(swirconn));
        // Now Start The System Timer
        mSystemTimer.start();
        // Now Start The Query Handler
        mQueryHandler.start();
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void unload() {
        // Shutdown Agent Clients
        for (VSMAgentClient client : mAgentClientMap.values()) {
            // Shutdown Agent Client
            client.abort();
            // Join With Agent Client
            try {
                // Join With Agent Client
                client.join();
                // Print Debug Information
                mVSM3Log.message("Joining Generic VSM Agent Client");
            } catch (Exception exc) {
                // Print Debug Information
                mVSM3Log.warning(exc.toString());
            }
        }
        // Shutdown Other Threads
        mQueryHandler.abort();
        mSystemTimer.abort();
        // Join With All Threads
        try {
            // Join With The Query Handler
            mQueryHandler.join();
            // Print Debug Information
            mVSM3Log.message("Joining Query Handler");
            // Join With The System Timer
            mSystemTimer.join();
            // Print Debug Information
            mVSM3Log.message("Joining System Timer");
        } catch (Exception exc) {
            // Print Debug Information
            mVSM3Log.warning(exc.toString());
        }
        // Clear The Task Map
        mWaitingThreadQueue.clear();
        // Clear The Agents Map
        mAgentClientMap.clear();
        // Print Debug Information
        mVSM3Log.message("Unloading Generic VSM Scenen Player");
    }
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    public final void setStartupTime(final long value) {
        mStartupTime = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final long getStartupTime() {
        return mStartupTime;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final void setCurrentTime(final long value) {
        mCurrentTime = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final long getCurrentTime() {
        return mCurrentTime;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final HashMap<String, Thread> getWaitingThreadQueue() {
        return mWaitingThreadQueue;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final VSMAgentClient getAgentClient(final String name) {
        return mAgentClientMap.get(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final synchronized boolean query(final String querystr) {
        // Make The Query To The KB
        JPLResult result = JPLEngine.query(querystr);
        // Check The Query Results 
        if (result.size() == 1) {
            // Get The First And Single Substitution 
            HashMap<String, String> subst = result.getFirst();
            // Try To Set The Variables Locally
            // Because A Local Thread Is Trying
            try {
                // Compute The Current Running Process
                Process thread = (Process) Thread.currentThread();
                // Get The Environment Of The Process
                Environment environment = thread.getEnvironment();
                // Set The Variables In The Environment
                for (Map.Entry<String, String> entry : subst.entrySet()) {
                    try {
                        environment.write(entry.getKey(),
                                new StringValue(JPLUtility.convert(entry.getValue())));
                        // Print Some Information
                        //System.err.println(entry.getKey() + "->" + entry.getValue());
                    } catch (Exception exc) {
                        // Print Debug Information
                        mVSM3Log.failure(exc.toString());
                    }
                }
            } catch (Exception exc) {
                // Try To Set The Variables Globally
                // Because An Extern Thread Is Trying
                for (Map.Entry<String, String> entry : subst.entrySet()) {
                    RunTime.getInstance().setVariable(
                            mProjectData.getSceneFlow(),
                            entry.getKey(),
                            entry.getValue());
                }
            }
            return true;
        } else {
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final void setVariable(final String name, final String value) {
        if (mVSM3RunTime.hasVariable(mSceneFlow, name)) {
            // Debug Some Information
            mVSM3Log.message("Finding Variable '" + name + "' To Value '" + value + "'");
            // Set The Variable Now
            mVSM3RunTime.setVariable(mSceneFlow, name, value);
            // Debug Some Information
            mVSM3Log.message("Setting Variable '" + name + "' To Value '" + value + "'");
        } else {
            // Debug Some Information
            mVSM3Log.message("SceneMaker Variable '" + name + "' Not Available");
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final void setVariable(final String name, final boolean value) {
        if (mVSM3RunTime.hasVariable(mSceneFlow, name)) {
            // Debug Some Information
            mVSM3Log.message("Finding Variable '" + name + "' To Value '" + value + "'");
            // Set The Variable Now
            mVSM3RunTime.setVariable(mSceneFlow, name, value);
            // Debug Some Information
            mVSM3Log.message("Setting Variable '" + name + "' To Value '" + value + "'");
        } else {
            // Debug Some Information
            mVSM3Log.message("SceneMaker Variable '" + name + "' Not Available");
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final void setVariable(final String name, final int value) {
        if (mVSM3RunTime.hasVariable(mSceneFlow, name)) {
            // Debug Some Information
            mVSM3Log.message("Finding Variable '" + name + "' To Value '" + value + "'");
            // Set The Variable Now
            mVSM3RunTime.setVariable(mSceneFlow, name, value);
            // Debug Some Information
            mVSM3Log.message("Setting Variable '" + name + "' To Value '" + value + "'");
        } else {
            // Debug Some Information
            mVSM3Log.message("SceneMaker Variable '" + name + "' Not Available");
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    protected abstract void handle(final VSMAgentClient client);
}
