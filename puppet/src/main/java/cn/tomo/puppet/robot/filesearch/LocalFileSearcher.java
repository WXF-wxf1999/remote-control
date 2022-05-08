package cn.tomo.puppet.robot.filesearch;

import cn.tomo.puppet.common.Configure;
import cn.tomo.puppet.common.Log;
import cn.tomo.puppet.robot.filesearch.bean.SearchConfig;
import cn.tomo.puppet.robot.filesearch.bean.SearchResult;
import cn.tomo.puppet.robot.filesearch.bean.filter.ConditionFilterConfig;
import cn.tomo.puppet.robot.filesearch.bean.filter.KeyWordFilterConfig;
import cn.tomo.puppet.robot.filesearch.task.ConditionSearchTask;
import cn.tomo.puppet.robot.filesearch.task.KeyWordSearchTask;
import cn.tomo.puppet.robot.filesearch.task.SearchTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ______DancingWolf on 2017/11/16.
 */
public class LocalFileSearcher {
    //thread pool
    private ThreadPoolExecutor taskThreadPool = null;

    // content
    private List<String> filePathSet = new ArrayList<>();

    private final Semaphore semaphore = new Semaphore(0,true);

    //search config
    private SearchConfig searchConfig = null;

    //search event handler
    private SearchEventHandler eventHandler = new SearchEventHandler(this);

    public LocalFileSearcher() {
        this.searchConfig = new SearchConfig();//use default config
    }

    public LocalFileSearcher(SearchConfig config) {
        this.searchConfig = null != config ? config : new SearchConfig();
    }

    /*
     * @description: search the key word
     *
     * @author: ______DancingWolf
     * @date: 2017/11/20
     * @param: [keyWord]
     * @return:
     */
    public void doSearch(String keyWord) {
        this.doSearch(keyWord, null);
    }

    /*
     * @description: search the key word with config
     *
     * @author: ______DancingWolf
     * @date: 2017/11/20
     * @param: [keyWord, keyWordConfig]
     * @return:
     */
    public void doSearch(String keyWord, KeyWordFilterConfig keyWordConfig) {
        System.out.println("Start search key word: " + keyWord);
        if (null == keyWordConfig) {
            keyWordConfig = new KeyWordFilterConfig();
        }

        //check
        if (null == keyWord || "".equals(keyWord.trim())) {
            System.out.println("Not a valid keyword");
            return;
        }

        //task main
        SearchTask mainTaskThread = new KeyWordSearchTask(this, null, keyWordConfig, keyWord);
        this.doSearchStart(mainTaskThread);
    }

    /*
     * @description: search files with conditions
     *
     * @author: ______DancingWolf
     * @date: 2017/11/20
     * @param: [conditionFilterConfig] condition config
     * @return:
     */
    public void doSearch(ConditionFilterConfig conditionFilterConfig) {
        if (null == conditionFilterConfig) {
            System.out.println("Condition is not set");
        }

        //check
        if (!conditionFilterConfig.isValid()) {
            return;
        }

        SearchTask mainTaskThread = new ConditionSearchTask(this, null, conditionFilterConfig);
        this.doSearchStart(mainTaskThread);
    }

    public List<String> getFilePathSet() {
        return filePathSet;
    }
    /*
     * @description: do a search process with key word
     *
     * @author: ______DancingWolf
     * @date: 2017/11/16
     * @param: [mainTaskThread] task thread
     * @return:
     */
    private void doSearchStart(SearchTask mainTaskThread) {
        //search start folder
        File startSearchFolder = this.searchConfig.getStartFolder();

        //do the search process
        //System.out.println("Start search keyword: " + keyWord);
        this.fireEvent("START_SEARCH", startSearchFolder);

        //init the thread pool
        this.taskThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.searchConfig.getThreadNum());

        //check the search config
        if (null == startSearchFolder) {
            System.out.println("Start search all drivers!");

            //start the search for all drivers
            File[] drivers = File.listRoots();
            if (null == drivers || 0 == drivers.length) {
                System.out.println("No valid driver found!");
                return;
            }

            //start search task
            for (File driver : drivers) {
                this.startNewTask(mainTaskThread.newInstance(driver));
            }
            return;
        }

        //if the target folder(4 search) is specified
        System.out.println("Start search in folder: " + startSearchFolder.getAbsolutePath());
        this.startNewTask(mainTaskThread.newInstance(startSearchFolder));
    }

    /*
     * @description: search task finished
     *
     * @author: ______DancingWolf
     * @date: 2017/11/20
     * @param: [searchResult] result info
     * @return:
     */
    public void doSearchFinished(SearchResult searchResult) {
        if (null == searchResult) {
            System.out.println("Invalied search result");
            return;
        }
        //end all threads
        this.taskThreadPool.shutdown();

        //print search result
        System.out.println("Search costs   : " + (searchResult.getSearchEndMilis() - searchResult.getSearchStartMilis()) + "(ms)");
        System.out.println("File searched  : " + searchResult.getSearchFileCount());
        System.out.println("Folder searched: " + searchResult.getSearchFolderCount());
        System.out.println("Success match  : " + searchResult.getSearchSuccessCount());

        semaphore.release();
    }

    public void waiting() {

        try {
            semaphore.acquire();
        } catch (Exception e) {
            Log.error(e.toString());
        }

    }

    /*
     * @description: check is all search task finished
     *
     * @author: ______DancingWolf
     * @date: 2017/11/20
     * @param:
     * @return:
     */
    public synchronized boolean isTerminated() {
        int activeCount = this.taskThreadPool.getActiveCount();
        return 1 == activeCount;
    }

    /*
     * @description: start a new thread task for a folder
     *
     * @author: ______DancingWolf
     * @date: 2017/11/16
     * @param: [folder] search folder
     * @param: [keyWord] key word
     * @return:
     */
    public void startNewTask(SearchTask task) {
        this.taskThreadPool.submit(task);
        //this.taskThreadPool
    }

    /*
     * @description: event fired during the searching
     *
     * @author: ______DancingWolf
     * @date: 2017/11/16
     * @param: [event] event fired
     * @param: [args] arguments
     * @return:
     */
    public void fireEvent(String event, Object... args) {
        eventHandler.on(event, args);
    }

    /*
     * @description: test
     *
     * @author: ______DancingWolf
     * @date: 2017/11/16
     * @param:
     * @return:
     */
    public static void main(String[] as) {
        //default
        LocalFileSearcher searcher = new LocalFileSearcher();
        searcher.doSearch("test");

        //setting
//        SearchConfig searchConfig = new SearchConfig();
//        searchConfig.setStartFolder(null);
//        searchConfig.setThreadNum(50);
//        LocalFileSearcher searcher = new LocalFileSearcher(searchConfig);
//        KeyWordFilterConfig filterConfig = new KeyWordFilterConfig();
//        filterConfig.setMatchStart(false);
//        filterConfig.setMatchEnd(true);
//        filterConfig.setCaseSensitive(false);
//        searcher.doSearch("TEST", filterConfig);

        //condition
//        SearchConfig searchConfig = new SearchConfig();
//        searchConfig.setStartFolder(null);
//        searchConfig.setThreadNum(100);
//        LocalFileSearcher searcher = new LocalFileSearcher(searchConfig);
//
//        ConditionFilterConfig filterConfig = new ConditionFilterConfig();
//        filterConfig.setMinFileSize(500 * 1024 * 1024L);
//        searcher.doSearch(filterConfig);
    }
}
