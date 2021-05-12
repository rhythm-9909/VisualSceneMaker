package de.dfki.vsm.xtension.um;

import de.dfki.vsm.model.project.PluginConfig;
import de.dfki.vsm.runtime.activity.AbstractActivity;
import de.dfki.vsm.runtime.activity.AbstractActivity.Type;
import de.dfki.vsm.runtime.activity.executor.ActivityExecutor;
import de.dfki.vsm.runtime.interpreter.value.IntValue;
import de.dfki.vsm.runtime.interpreter.value.StringValue;
import de.dfki.vsm.runtime.project.RunTimeProject;
import de.dfki.vsm.util.log.LOGConsoleLogger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Patrick
 */
public class EmmaUserModel extends ActivityExecutor {

    // List of all users
    private JSONObject mUserProfiles = new JSONObject();
    // current user
    private JSONObject mUser = null;
    // days at which the user had a diary conversation
    private List<String> mDiaryDays = new LinkedList<>();
    // current day and daily items
    private int mSelectedDay = -1;
    private List<Integer> mDailyItemReferences = new LinkedList<>();

    // sceneflow variables
    private final String mVSM_DiaryDay = "diaryDay";
    private final String mVSM_DiaryDailyItemsNumber = "diaryDailyItemsNum";
    private final String mVSM_DiaryItemProducer = "diaryItemProducer";
    private final String mVSM_DiaryItemText = "diaryItemText";

    // File stuff
    private String umDir = "";
    private String umFile = "";

    // The singleton logger instance
    private final LOGConsoleLogger mLogger = LOGConsoleLogger.getInstance();

    public EmmaUserModel(PluginConfig config, RunTimeProject project) {
        super(config, project);
    }

    @Override
    public final synchronized String marker(final long id) {
        // Bracket style bookmarks
        return "$(" + id + ")";
    }

    @Override
    public void launch() {
        mLogger.message("Loading EmmA UserModel Executor ...");

        // load user profiles
        loadUserModel();
    }

    @Override
    public void execute(AbstractActivity activity) {
        // Get action information
        final Type activity_type = activity.getType();
        final String activity_text = activity.getText();
        final String activity_name = activity.getName();
        //final String activity_mode = activity.getMode();
        final String activity_actor = activity.getActor();
        final List activity_features = activity.getFeatures();
        // set all activities blocking
        activity.setType(Type.blocking);

        final String name = activity.getName();
        if (name.equalsIgnoreCase("set")) {
            setUserValue("name", activity);
            setUserValue("introduction", activity);
            setUserValue("break", activity);
            setUserValue("type", activity);
            setUserValue("therapy", activity);
            setUserValue("icd", activity);
            setUserValue("contact", activity);
            setUserValue("contactphone", activity);
            setUserValue("therapist", activity);
            setUserValue("therapistphone", activity);
            setUserValue("nextworktime_mo", activity);
            setUserValue("nextworktime_tu", activity);
            setUserValue("nextworktime_we", activity);
            setUserValue("nextworktime_th", activity);
            setUserValue("nextworktime_fr", activity);
            setUserValue("actworktime_mo", activity);
            setUserValue("actworktime_tu", activity);
            setUserValue("actworktime_we", activity);
            setUserValue("actworktime_th", activity);
            setUserValue("actworktime_fr", activity);
            setUserValue("pos_activitiy", activity);

            // add/update to user profiles
            saveUserModel();
        }

        if (name.equalsIgnoreCase("get")) {
            if ((activity.get("strVar") != null) && (mProject.hasVariable(activity.get("strVar")))) {
                String val = getUserStrValue(activity);
                mProject.setVariable(activity.get("strVar"), new StringValue(val));
            }
            if ((activity.get("intVar") != null) && (mProject.hasVariable(activity.get("intVar")))) {
                Integer val = getUserIntValue(activity);
                mProject.setVariable(activity.get("intVar"), new IntValue(val));
            }
        }

        // load creates a new user model, if there is no user with the specific name.
        if (name.equalsIgnoreCase("load")) {
            if ((activity.get("name") != null)) {
                String userName = activity.get("name");
                mUser = loadUserData(userName);

                if (mUser != null) {
                    mLogger.message("Data from user " + userName + " found!");
                } else {
                    JSONArray users = mUserProfiles.getJSONArray("users");
                    mUser = createUser(userName, users.length() + 1);
                    users.put(mUser);
                    saveUserModel();
                    mLogger.warning("Data from user " + userName + " not found - new user created!");
                }
            } else { // load the latest
                mUser = loadUserData();
                String userName = mUser.getString("name");
                mLogger.message("Data from user " + userName + " loaded!");
            }
            // at this point, there is a user model, a freshly created or loaded
            diaryDaysManagement();
        }

        // PlayAction("[um diaryday no=0]") stores a day string (e.g., "2. November 2020")
        // in a sceneflow var (mVSM_DiaryDay)
        if (name.equalsIgnoreCase("diaryday")) {
            if ((activity.get("no") != null)) {
                int day = Integer.parseInt(activity.get("no"));

                if (mProject.hasVariable(mVSM_DiaryDay)) {
                    if (mDiaryDays.get(day) != null) {
                        mProject.setVariable(mVSM_DiaryDay, mDiaryDays.get(day).replace(",", " ")); //replace "," with " "
                    } else {
                        mLogger.failure("Requested diary day (" + day + ") does not exist");
                    }
                }
            }
        }

        // PlayAction ( "[um dailyitems day=0]" ) stores the number of dialog parts
        // in a sceneflow var (mVSM_DiaryDailyItemsNumber)
        if (name.equalsIgnoreCase("dailyitems")) {
            if ((activity.get("day") != null)) {
                mSelectedDay = Integer.parseInt(activity.get("day"));

                if (mProject.hasVariable(mVSM_DiaryDailyItemsNumber)) {
                    if ((mSelectedDay < mDiaryDays.size()) && (mDiaryDays.get(mSelectedDay) != null)) {
                        String dayStr = mDiaryDays.get(mSelectedDay);
                        mDailyItemReferences = collectDailyItems(dayStr);

                        mProject.setVariable(mVSM_DiaryDailyItemsNumber, mDailyItemReferences.size());
                    } else {
                        mLogger.failure("Requested diary day (" + mSelectedDay + ") does not exist");
                    }
                }
            } else {
                mLogger.failure("Required 'day' information (int) is missing for loading daily items.");
            }
        }

        //PlayAction ( "[um item no=" + dcnt + "]" ) saves producer and text of a diary item (no) in sceneflow vars
        //(mVSM_DiaryItemProducer, mVSM_DiaryItemText) containing the informatio about the dialog item
        if (name.equalsIgnoreCase("item")) {
            if (activity.get("no") != null) {
                int no = Integer.parseInt(activity.get("no"));

                if (mProject.hasVariable(mVSM_DiaryItemProducer) && mProject.hasVariable(mVSM_DiaryItemText)) {
                    if ((mSelectedDay < mDiaryDays.size()) && (mDiaryDays.get(mSelectedDay) != null)) {
                        String dayStr = mDiaryDays.get(mSelectedDay);
                        int itemNum = mDailyItemReferences.get(no);
                        mLogger.message("Retrieving dialog part represented with no " + itemNum);

                        JSONArray diary = mUser.getJSONArray("diary");
                        JSONObject entry;
                        for (int i = 0; i < diary.length(); i++) {
                            if (diary.getJSONObject(i).getInt("no") == itemNum) {
                                entry = diary.getJSONObject(i);
                                if ((entry.has("entry")) && (!entry.getString("entry").isEmpty())) { // for now, do only consider entries with key "entry"
                                    mProject.setVariable(mVSM_DiaryItemProducer, new StringValue(entry.getString("producer")));
                                    mProject.setVariable(mVSM_DiaryItemText, new StringValue(entry.getString("entry")));
                                }
                                return;
                            }
                        }
                        mProject.setVariable(mVSM_DiaryItemProducer, new StringValue("unknown"));
                        mProject.setVariable(mVSM_DiaryItemText, new StringValue("No entry referred by " + mSelectedDay));
                        mLogger.failure("Requested diary day (" + mSelectedDay + ") does not exist");
                    }
                }
            } else {
                mLogger.failure("Required 'no' information (int) is missing for loading item.");
            }
        }

        if (name.equalsIgnoreCase("diary_emotion")) {
            if (mUser != null) {
                storeDiaryEntry(activity, "user", "emotion", "value");
            } else {
                mLogger.warning("No user specified, diary emotion value will not be stored.");
            }
        }

        if (name.equalsIgnoreCase("diary_mood")) {
            if (mUser != null) {
                storeDiaryEntry(activity, "user", "mood", "value");
            } else {
                mLogger.warning("No user specified, diary mood value will not be stored.");
            }
        }

        if (name.equalsIgnoreCase("diary")) {
            if (mUser != null) {
                storeDiaryEntry(activity, "producer", "entry", "entry");
            } else {
                mLogger.warning("No user specified, diary entry will not be stored.");
            }
        }

        if (name.equalsIgnoreCase("utterancestart")) {
            if (mUser != null) {
                storeDiaryEntry(activity, "producer", "utterancestart", "value");
            } else {
                mLogger.warning("No user specified, diary entry will not be stored.");
            }
        }

        if (name.equalsIgnoreCase("utteranceduration")) {
            if (mUser != null) {
                storeDiaryEntry(activity, "producer", "utteranceduration", "value");
            } else {
                mLogger.warning("No user specified, diary entry will not be stored.");
            }
        }
    }

    // set also overrides previously set values.
    private void setUserValue(String key, AbstractActivity activity) {
        if ((activity.get(key) != null) && (mUser.get(key) != "")) {
            String value = activity.get(key).replace("'", "");
            mUser.put(key, value);
        }
    }

    // get String value.
    private String getUserStrValue(AbstractActivity activity) {
        final String key = "value";
        if ((activity.get(key) != null) && (mUser.get(activity.get(key)) != "")) {
            return mUser.getString(activity.get(key));
        } else {
            mLogger.failure("Failed to retrieve user model information " + activity.get(key) + " with activity key " + key);
            mLogger.failure(mUser.toString());
            return "";
        }
    }

    // get Integer value.
    private Integer getUserIntValue(AbstractActivity activity) {
        final String key = "value";
        if ((activity.get(key) != null) && (mUser.get(activity.get(key)) != "")) {
            return Integer.parseInt(mUser.getString(activity.get(key)));
        } else {
            mLogger.failure("Failed to retrieve user model information " + activity.get(key) + " with activity key " + key);
            mLogger.failure(mUser.toString());
            return -1;
        }
    }

    private void storeDiaryEntry(AbstractActivity activity, String producer, String key, String value) {
        JSONObject diaryentry = new JSONObject();

        long dateMillis = System.currentTimeMillis();

        diaryentry.put("date", dateMillis);
        diaryentry.put("no", getLastDiaryEntryNumber() + 1);
        diaryentry.put("producer", (activity.get(producer) != null) ? activity.get(producer) : "");
        diaryentry.put(key, (activity.get(value) != null) ? activity.get(value).replace("'", "").replace("\n", " ").replace("  ", " ") : "");

        JSONArray diary = mUser.getJSONArray("diary");
        diary.put(diaryentry);

        saveUserModel();
        diaryDaysManagement();
    }

    private void diaryDaysManagement() {
        // do diary management
        collectDiaryDays();

        if (mProject.hasVariable("diaryDaysNum")) {
            mProject.setVariable("diaryDaysNum", mDiaryDays.size());
        }
    }

    private void collectDiaryDays() {
        mDiaryDays = new LinkedList<>();
        Map<Date, JSONArray> dateDiaryEntries = new HashMap<>();
        JSONArray diary = mUser.getJSONArray("diary");

        if (diary.length() > 0) {
            mLogger.message("Found " + diary.length() + " diary entries.");

            for (int i = 0; i < diary.length(); i++) {
                JSONObject diaryItem = diary.getJSONObject(i);

                // make a proper date
                long dateMillis = diaryItem.getLong("date");
                Date exactDate = new Date(dateMillis);
                // date format
                DateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.GERMANY);
                // reduce it to month day year to "collect" all item for one day!
                String dateStr = df.format(exactDate);
                Date reducedDate = null;
                try {
                    reducedDate = df.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // sort it
                if (dateDiaryEntries.containsKey(reducedDate)) { // if there is an entry for this particular date, add diary entry
                    JSONArray dateEntries = dateDiaryEntries.get(reducedDate);
                    dateEntries.put(diaryItem);
                    dateDiaryEntries.replace(reducedDate, dateEntries);
                } else { // if there is not an entry for this particular date, create diary entry array and add diary entrey
                    JSONArray dateEntries = new JSONArray();
                    dateEntries.put(diaryItem);
                    dateDiaryEntries.put(reducedDate, dateEntries);
                }
            }
            for (Map.Entry<Date, JSONArray> entry : dateDiaryEntries.entrySet()) {
                Date k = entry.getKey();
                DateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.GERMANY);
                String dateStr = df.format(k);
                mDiaryDays.add(dateStr);
            }
        }
        mLogger.message("Found entries for " + dateDiaryEntries.keySet().size() + " different days.");
    }

    private List<Integer> collectDailyItems(String dayStr) {
        JSONArray diary = mUser.getJSONArray("diary");
        List<Integer> entries = new LinkedList<>();

        // Date format
        DateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.GERMANY);
        Date targetDate = Calendar.getInstance().getTime(); // initialize it with today

        try { // try to parse dayStr and create Date object
            targetDate = df.parse(dayStr);
        } catch (ParseException e) {
            mLogger.failure(dayStr + " is not a valid day (date)!");
        }
        if (diary.length() > 0) {
            mLogger.message("Found " + diary.length() + " diary entries.");
            for (int i = 0; i < diary.length(); i++) {
                JSONObject diaryItem = diary.getJSONObject(i);
                if ((diaryItem.has("entry")) && (!diaryItem.getString("entry").isEmpty())) { // for now, do only consider entries with key "entry"
                    // make a proper date
                    long dateMillis = diaryItem.getLong("date");
                    // get the number of the dialog entry
                    Date exactDate = new Date(dateMillis);
                    // reduce it to month day year to "collect" all item for one day!
                    String dateStr = df.format(exactDate);
                    Date reducedDate = null;
                    try {
                        reducedDate = df.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (reducedDate.equals(targetDate)) {
                        entries.add(diaryItem.getInt("no"));
                    }
                }
            }
        }
        return entries;
    }

    private long getLastDiaryEntryNumber() {
        JSONArray diary = mUser.getJSONArray("diary");
        long biggestNo = -1;

        for (int i = 0; i < diary.length(); i++) {
            JSONObject item = diary.getJSONObject(i);
            biggestNo = (item.getLong("no") > biggestNo) ? item.getLong("no") : biggestNo;
        }
        return biggestNo;
    }

    private JSONObject loadUserData(String name) {
        JSONArray users = mUserProfiles.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            JSONObject item = users.getJSONObject(i);
            if (item.getString("name").equalsIgnoreCase(name)) return item;
        }
        return null;
    }

    private JSONObject loadUserData() { // load data from device user
        JSONArray users = mUserProfiles.getJSONArray("users");

        if (users.length() > 0) {
            return users.getJSONObject(0);
        } else {
            loadUserModel();
            return users.getJSONObject(0);
        }
    }

    private JSONObject createUser(String name, long id) {
        JSONObject user = new JSONObject();

        // initial user data
        user.put("name", name);
        user.put("id", id);
        user.put("introduction", "unknown");
        user.put("break", "unknown");
        user.put("type", "unknown");
        user.put("therapy", "unknown");
        user.put("icd", "unknown");
        user.put("contact", "unknown");
        user.put("contactphone", "unknown");
        user.put("therapist", "unknown");
        user.put("therapistphone", "unknown");
        user.put("nextworktime_mo", "unknown");
        user.put("nextworktime_tu", "unknown");
        user.put("nextworktime_we", "unknown");
        user.put("nextworktime_th", "unknown");
        user.put("nextworktime_fr", "unknown");
        user.put("actworktime_mo", "unknown");
        user.put("actworktime_tu", "unknown");
        user.put("actworktime_we", "unknown");
        user.put("actworktime_th", "unknown");
        user.put("actworktime_fr", "unknown");
        user.put("posactivity", "unknown");

        // intial diary entry
        JSONArray diary = new JSONArray();
        JSONObject diaryentry = new JSONObject();

        String dateStr = Long.toString(System.currentTimeMillis());

        diaryentry.put("date", dateStr);
        diaryentry.put("no", 0);
        diaryentry.put("producer", "system");
        diaryentry.put("entry", "created");

        //diary.put(diaryentry);
        user.put("diary", diary);

        return user;
    }

    private void loadUserModel() {
        mLogger.message("Loading EmmA User Model ...");

        if ((mConfig.getProperty("umdir") != null) && (!mConfig.getProperty("umdir").isEmpty())) {
            umDir = mConfig.getProperty("umdir");
        } else {
            mLogger.failure("<Feature key=\"umdir\" val=\"<directory>\"/> is not specified in VSM project file. Aborting!");
            System.exit(0);
        }
        if ((mConfig.getProperty("umfile") != null) && (!mConfig.getProperty("umfile").isEmpty())) {
            umFile = mConfig.getProperty("umfile");
        } else {
            mLogger.failure("<Feature key=\"umfile\" val=\"<file name>\"/> is not specified in VSM project file. Aborting!");
            System.exit(0);
        }

        String umf = (mProject.getProjectPath() + File.separator + umDir + File.separator + umFile).replace("\\", "/");
        String input = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(umf));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            input = sb.toString();
        } catch (FileNotFoundException e) {
            mLogger.warning("No User Model found in " + umf + ", creating new.");

            // create first entry, with the first user - id 0
            //out object and user array object
            JSONObject jsonOut = new JSONObject();
            JSONArray jsonUserArray = new JSONArray();

            // user
            jsonUserArray.put(createUser("unknown", 0));
            jsonOut.put("users", jsonUserArray);

            input = jsonOut.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mUserProfiles = new JSONObject(input);

        saveUserModel();

        JSONArray users = mUserProfiles.getJSONArray("users");

        mLogger.message("Found user " + users.get(0).toString());
    }

    private void saveUserModel() {
        String umf = (mProject.getProjectPath() + File.separator + umDir + File.separator + umFile).replace("\\", "/");
        try {
            FileWriter umfw = new FileWriter(umf);

            umfw.write(mUserProfiles.toString());
            umfw.flush();
            umfw.close();
        } catch (IOException e) {
            mLogger.failure("Error writing UM to " + umf);
        }
    }

    @Override
    public void unload() {
        // save user data in user Model
        saveUserModel();
    }
}
