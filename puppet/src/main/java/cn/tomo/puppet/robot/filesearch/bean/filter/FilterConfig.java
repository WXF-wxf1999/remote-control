package cn.tomo.puppet.robot.filesearch.bean.filter;

/**
 * Created by ______DancingWolf on 2017/11/20.
 */
public class FilterConfig {
    private boolean searchFile = true; //search file name
    private boolean searchFolder = false;//default, ignore folder

    public boolean isSearchFile() {
        return searchFile;
    }

    public void setSearchFile(boolean searchFile) {
        this.searchFile = searchFile;
    }

    public boolean isSearchFolder() {
        return searchFolder;
    }

    public void setSearchFolder(boolean searchFolder) {
        this.searchFolder = searchFolder;
    }
}
