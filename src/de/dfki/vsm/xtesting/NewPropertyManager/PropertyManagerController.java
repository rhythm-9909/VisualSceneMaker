package de.dfki.vsm.xtesting.NewPropertyManager;

import de.dfki.vsm.model.config.ConfigFeature;
import de.dfki.vsm.model.project.AgentConfig;
import de.dfki.vsm.model.project.PluginConfig;
import de.dfki.vsm.runtime.project.RunTimeProject;
import de.dfki.vsm.xtesting.propertymanager.TableConfig;
import de.dfki.vsm.xtesting.propertymanager.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by alvaro on 6/2/16.
 */
public class PropertyManagerController implements Initializable, TreeObserver {

    private RunTimeProject mProject = null;
    @FXML
    TreeView<String> treeView;
    @FXML
    AnchorPane contentPane;
    @FXML private TableView pluginsTable ;
    @FXML private TableColumn keyColumn;
    @FXML private TableColumn valueColumn;
    @FXML private TextField txtKey;
    @FXML private TextField txtValue ;
    @FXML private Button btnAddEntry ;
    @FXML private ComboBox cmbExecutor;
    @FXML private Button btnAddDevice;
    @FXML private TextField txtDeviceName;
    @FXML private Label lblClassName;
    @FXML private CheckBox chkLoadPlugin;
    private TreeItem<String> devices;
    private HashMap<String, PluginConfig> plugins= new HashMap<>();

    private static ArrayList<String> mScenePlayersShortNames = new ArrayList<>();
    private static ArrayList<String> mScenePlayersLongNames = new ArrayList<>();

    private ObservableList<TableConfig> data = FXCollections.observableArrayList();

    public PropertyManagerController(RunTimeProject project){
        super();
        mProject = project;
        PluginConfig plugin = null;
        Iterator it = mProject.getProjectConfig().getPluginConfigList().iterator();
        while (it.hasNext() && plugin == null) {
            PluginConfig p = (PluginConfig) it.next();
            plugins.put(p.getPluginName(), p);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> root = new TreeItem<>("VSM Config");
        devices = new TreeItem<>("Devices");
        addInitialPluginToTreeList();
        root.getChildren().add(devices);
        treeView.setRoot(root);
        treeView.setEditable(true);
        final PropertyManagerController controller = this;
        treeView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                TreeCellImpl treeCell = new TreeCellImpl();
                treeCell.registerObserver(controller);
                return treeCell;
            }
        });
    }

    private void addInitialPluginToTreeList(){
        for (PluginConfig plugin:  mProject.getProjectConfig().getPluginConfigList()) {
            ContextTreeItem pluginNode = new ContextTreeItem(plugin.getPluginName());
            pluginNode.registerObserver(this);
            addAgentToPluginNode(plugin, pluginNode);
            devices.getChildren().add(pluginNode);
        }
    }

    private void addAgentToPluginNode(PluginConfig plugin, ContextTreeItem pluginNode){
        for(AgentConfig agent: mProject.getProjectConfig().getAgentConfigList() ) {
            addAgentToCorrespondentPlugin(agent, plugin.getPluginName(), pluginNode);
        }
    }

    private void addAgentToCorrespondentPlugin(AgentConfig agent, String pluginName, ContextTreeItem pluginNode){
        if(agent.getDeviceName().equals(pluginName)){
            ContextTreeItem agentNode = new ContextTreeItem(agent.getAgentName());
            agentNode.registerObserver(this);
            pluginNode.getChildren().add(agentNode);
        }
    }

    @FXML
    public void selectConfig(MouseEvent event){//TODO: refatorizar
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        getContextMenu(event.getButton().toString().equals("SECONDARY"));
        Iterator it = mProject.getProjectConfig().getPluginConfigList().iterator();
        try {
            PluginConfig plugin = getSelectedPlugin(item, it);
            if(item.getValue().equals("Devices")){
                showAddDevice();
            }else{
                showPluginTable(plugin, item, "Plugins");
                setClassNameLabel(plugin.getPluginName());
            }
        } catch (Exception e) {
            if(item != null && item.getParent() != null && plugins.containsKey(item.getParent().getValue())){
                showPluginTable(plugins.get(item.getParent().getValue()), item, "Agents");
                setClassNameLabel(item.getParent().getValue());
            }
        }
    }

    private void getContextMenu(boolean isRightClickPressed){
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if(isRightClickPressed && hasContextMenu()){
            ((ContextTreeItem)item).getMenu();
        }
    }


    private boolean hasContextMenu(){
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        return (item instanceof ContextTreeItem && (item.getParent() != null && item.getParent().getValue().equals("Devices")));
    }

    private PluginConfig getSelectedPlugin(TreeItem<String> item, Iterator it) throws Exception {
        PluginConfig plugin = null;
        if(item == null) {
            throw new Exception("No item selected");
        }
        while (it.hasNext() && plugin == null) {
            PluginConfig p = (PluginConfig) it.next();
            if (p.getPluginName().equals(item.getValue())) {
                plugin = p;
            }
        }

        return plugin;
    }

    private void showAddDevice(){
        System.out.println("To implement!!");
    }

    private void setClassNameLabel(String pluginName){
        lblClassName.setText("Class: " + mProject.getPluginConfig(pluginName).getClassName());
    }

    private void showPluginTable(PluginConfig plugin, TreeItem<String> item, String type){
        pluginsTable.setEditable(true);
        if(type.equals("Plugins")) {
            getPluginData(plugin);
        }else if(type.equals("Agents")) {
            getAgentData(plugin, item.getValue());
        }
    }

    private void getPluginData(PluginConfig plugin){
        data.clear();
        for (ConfigFeature feat : plugin.getEntryList()) {
            TableConfig tFeat = new TableConfig(feat.getKey(), feat.getValue(), plugin.getPluginName());
            data.add(tFeat);
        }
        populatePluginTable(plugin);
    }

    private void getAgentData(PluginConfig plugin, String agentName){
        data.clear();
        for  (AgentConfig agent: mProject.getProjectConfig().getAgentConfigList() ) {
            if(agent.getDeviceName().equals(plugin.getPluginName()) && agentName.equals(agent.getAgentName())){
                addFeatureData(agent, plugin.getPluginName());
            }
        }
        populatePluginTable(plugin);
    }

    private void addFeatureData(AgentConfig agent, String pluginName){
        for (ConfigFeature feat :agent.getEntryList() ) {
            TableConfig tFeat = new TableConfig(feat.getKey(), feat.getValue(), pluginName, agent.getAgentName());
            data.add(tFeat);
        }
    }

    private void populatePluginTable(PluginConfig plugin){
        ObservableList<String> pluginList = FXCollections.observableArrayList();
        setEditablKeyeCell(plugin);
        setEditableValueCell(plugin);
        pluginsTable.setItems(data);
    }

    private void setEditablKeyeCell(PluginConfig plugin){
        keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keyColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TableConfig, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TableConfig, String> t) {
                        ((TableConfig) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setValue(t.getNewValue());
                        saveDevices(plugin.getPluginName());
                        saveAgent(plugin.getPluginName());
                    }
                }
        );
        keyColumn.setCellValueFactory(new PropertyValueFactory("key"));
    }

    private void setEditableValueCell(PluginConfig plugin){
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TableConfig, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TableConfig, String> t) {
                        ((TableConfig) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setValue(t.getNewValue());
                        saveDevices(plugin.getPluginName());
                        saveAgent(plugin.getPluginName());
                    }
                }
        );
        valueColumn.setCellValueFactory(new PropertyValueFactory("value"));
    }



    private boolean addDevice(ActionEvent event){
        if (cmbExecutor.getSelectionModel().getSelectedIndex() > 0 && !txtDeviceName.getText().equals("") )
        { // 0 Is for the default configuration

            String newPlayer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<Project name=\"" + mProject.getProjectName() + "\">"
                    + "<Plugins>"
                    + "<Plugin type=\"device\" name=\"" + txtDeviceName.getText() + "\" class=\"" + mScenePlayersLongNames.get( cmbExecutor.getSelectionModel().getSelectedIndex() - 1) + "\" load=\"true\">"
                    + "</Plugin>"
                    + "</Plugins>"
                    + "</Project>";

            boolean res = mProject.parseProjectConfigFromString(newPlayer);
            if(res){
                ArrayList plugs =  mProject.getProjectConfig().getPluginConfigList();
                plugins.put(txtDeviceName.getText(), (PluginConfig) plugs.get(plugs.size()-1));
            }
            saveConfig();
            return res;
        }
        return false;
    }

    public static void loadClass()
    {
        try
        {
            getClassNamesFromPackage("de.dfki.vsm.xtension");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private static void parseJar(String jarFileName, String packageName){
        JarFile jf;
        Enumeration<JarEntry> jarEntries = null;
        String entryName;
        try {
            jf = new JarFile(jarFileName);
            jarEntries = jf.entries();
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (jarEntries.hasMoreElements())
        {
            entryName = jarEntries.nextElement().getName();
            if (packageName.length() == 0 || (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5))
            {
                try
                {
                    String fullClassName = entryName.replace("/", ".");
                    entryName = fullClassName.substring(0, entryName.lastIndexOf('.'));
                    Class classEntry = Class.forName(entryName);
                    //Class[] interfaces = classEntry.getInterfaces();
                    Class t =classEntry.getSuperclass();
                    if(t != null && t.getSimpleName().equals("ActivityExecutor")){
                        mScenePlayersLongNames.add(entryName);
                        mScenePlayersShortNames.add(entryName.substring(entryName.lastIndexOf('.') + 1));
                    }



                } catch (StringIndexOutOfBoundsException e)
                {
                    System.out.println(entryName);
                    continue;
                } catch (ClassNotFoundException e)
                {
                    System.out.println("Class not found");
                    continue;
                }

            }
        }
    }

    public static void getClassNamesFromPackage(String packageName) throws IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL packageURL;
        ArrayList<String> names = new ArrayList<String>();;

        packageName = packageName.replace(".", "/");
        packageURL = classLoader.getResource(packageName);
        if (mScenePlayersShortNames.size() <= 0)
        {
            mScenePlayersShortNames.add("Non selected");

            if (packageURL.getProtocol().equals("jar"))
            {
                String jarFileName;
                // build jar file name, then loop through zipped entries
                jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
                jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
                System.out.println(">" + jarFileName);
                parseJar(jarFileName, packageName);

                // loop through files in classpath
            }
            else
            {
                URI uri = null;
                try
                {
                    uri = new URI(packageURL.toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                File folder = new File(uri.getPath());
                // won't work with path which contains blank (%20)
                // File folder = new File(packageURL.getFile());
                File[] contenuti = folder.listFiles();
                String entryName;
                for (File actual : contenuti)
                {
                    entryName = actual.getName();
                    entryName = entryName.substring(0, entryName.lastIndexOf('.'));
                    names.add(entryName);
                }
            }
        }
    }


    private void saveDevices(String pluginName){
        PluginConfig plugin = mProject.getProjectConfig().getPluginConfig(pluginName);
        if(plugin != null){
            for (TableConfig tc: data){
                String oldValue = plugin.getProperty(tc.getKey());
                if(oldValue!=null && !oldValue.equals(tc.getValue()) && tc.getPlugin().equals(plugin.getPluginName())){ //Case that the key value pair was changed
                    plugin.setProperty(tc.getKey(), tc.getValue());
                }else if((oldValue == null || oldValue.equals("")) && tc.getPlugin().equals(plugin.getPluginName())){
                    plugin.setProperty(tc.getKey(), tc.getValue());
                }
            }
            saveConfig();
        }else{//It should exists
            System.out.println("Plugin does not exists");
        }


    }

    private void saveAgent(String pluginName){
        for (AgentConfig agent: mProject.getProjectConfig().getAgentConfigList() ) {
            for (TableConfig tc: data){
                String oldValue = agent.getProperty(tc.getKey());
                if(oldValue!=null && !oldValue.equals(tc.getValue()) && tc.getPlugin().equals(agent.getDeviceName()) && agent.getAgentName().equals(tc.getAgent())){ //Case that the key value pair was changed
                    agent.setProperty(tc.getKey(), tc.getValue());
                }else if((oldValue == null || oldValue.equals("")) && tc.getDevice().equals(agent.getDeviceName()) && agent.getAgentName().equals(tc.getAgent())){
                    agent.setProperty(tc.getKey(), tc.getValue());
                }
            }


        }
        saveConfig();
    }

    public void saveConfig(){
        //TODO: Remove later
        File f = new File(mProject.getProjectPath());
        mProject.write(f);
    }

    private String changeXMLAttributeNode(String xml, String tagName, String attr, String oldValue, String newValue){
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        Document doc;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        } catch (SAXException e) {
            e.printStackTrace();
            return xml;
        } catch (IOException e) {
            e.printStackTrace();
            return xml;
        }
        //Node firstChild = doc.getFirstChild();
        Node tag = doc.getElementsByTagName(tagName).item(0);
        // update node attribute
        NamedNodeMap attrs = tag.getAttributes();
        Node nodeAttr = attrs.getNamedItem(attr);
        if(nodeAttr!= null && nodeAttr.getNodeValue().equals(oldValue)) {
            nodeAttr.setTextContent(newValue);
        }

        return getStringFromDoc(doc);

    }

    public String getStringFromDoc(org.w3c.dom.Document doc)    {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch(TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(NotificationObject object) {
        if(object instanceof ContextEvent){//This called when a new agent is added via context menu
            String agent = ((ContextEvent) object).getContextName();
            String pluginName = ((ContextEvent) object).getPluginName();
            String newXMLAgent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<Project name=\"" + mProject.getProjectName() + "\">"
                    + "<Agents>"
                    + "    <Agent name=\"" + agent + "\" device=\"" + pluginName + "\">"
                    + "    </Agent>"
                    + " </Agents>"
                    + "</Project>";
            boolean res = mProject.parseProjectConfigFromString(newXMLAgent);
            saveConfig();
        }
        else if(object instanceof CellEvent){//We change either the name of the agent or device
            String oldValue = ((CellEvent) object).getOldValue();
            String newValue = ((CellEvent) object).getNewValue();
            TreeItem selected = treeView.getSelectionModel().getSelectedItem();
            if(selected !=null && (selected.getParent() != null && selected.getParent().getValue().equals("Devices"))){//Device updated
                Iterator<PluginConfig> itPlug = mProject.getProjectConfig().getPluginConfigList().iterator();
                ArrayList<String> pluginsToAdd = new ArrayList<>();
                ArrayList<String> agentsToAdd = new ArrayList<>();
                while (itPlug.hasNext()){
                    PluginConfig plugin = itPlug.next();
                    if(plugin.getPluginName().equals(oldValue)){
                        String pluginXML =   "<Project name=\"" + mProject.getProjectName() + "\">"
                                + "<Plugins>"
                                + plugin.toString()
                                + "</Plugins>"
                                + "</Project>";
                        String xml = changeXMLAttributeNode(pluginXML, "Plugin", "name", oldValue, newValue);
                        if(xml != null && !xml.equals("")){
                            itPlug.remove();
                            plugins.remove(oldValue);

                        }
                        pluginsToAdd.add(xml);
                    }
                }
                for (String xml: pluginsToAdd ) {
                    boolean res = mProject.parseProjectConfigFromString(xml);
                    if(res) {
                        ArrayList plugs =  mProject.getProjectConfig().getPluginConfigList();
                        plugins.put(newValue, (PluginConfig) plugs.get(plugs.size()-1));
                    }

                }

                //Change all the agents too
                Iterator<AgentConfig> itAgent = mProject.getProjectConfig().getAgentConfigList().iterator();
                while (itAgent.hasNext()){
                    AgentConfig agent = itAgent.next();
                    if(agent.getDeviceName().equals(oldValue) ){
                        String agentXML = "<Project name=\"" + mProject.getProjectName() + "\">"
                                + "<Agents>"
                                + agent.toString()
                                + "</Agents>"
                                + "</Project>";
                        String xml = changeXMLAttributeNode(agentXML, "Agent", "device", oldValue, newValue);
                        if(xml != null && !xml.equals("")){
                            itAgent.remove();

                        }
                        agentsToAdd.add(xml);
                    }
                }

                for (String xml: agentsToAdd ) {
                    boolean res = mProject.parseProjectConfigFromString(xml);
                }

            }
            else if(selected !=null && (selected.getParent() != null)) {
                TreeItem selectedPlugin = treeView.getSelectionModel().getSelectedItem().getParent();
                ArrayList<String> agentsToAdd = new ArrayList<>();
                Iterator<AgentConfig> itAgent = mProject.getProjectConfig().getAgentConfigList().iterator();
                while (itAgent.hasNext()){
                    AgentConfig agent = itAgent.next();
                    if(agent.getDeviceName().equals(selectedPlugin.getValue()) && agent.getAgentName().equals(oldValue)){
                        String agentXML = "<Project name=\"" + mProject.getProjectName() + "\">"
                                + "<Agents>"
                                + agent.toString()
                                + "</Agents>"
                                + "</Project>";
                        String xml = changeXMLAttributeNode(agentXML, "Agent", "name", oldValue, newValue);
                        if(xml != null && !xml.equals("")){
                            itAgent.remove();
                        }
                        agentsToAdd.add(xml);
                    }
                }

                for (String xml: agentsToAdd ) {
                    boolean res = mProject.parseProjectConfigFromString(xml);
                }

            }
            saveConfig();
        }
    }








}