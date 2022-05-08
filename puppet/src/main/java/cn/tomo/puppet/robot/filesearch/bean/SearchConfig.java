package cn.tomo.puppet.robot.filesearch.bean;

import java.io.File;

/**
 * Created by ______DancingWolf on 2017/11/16.
 *
 *  search config from a search process
 */
public class SearchConfig {
    private File startFolder = null; //search start folder, default null(will search all drivers)
    private int threadNum = 100;//thread pool size

    public File getStartFolder() {
        return startFolder;
    }

    public void setStartFolder(File startFolder) {
        this.startFolder = startFolder;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }
}
