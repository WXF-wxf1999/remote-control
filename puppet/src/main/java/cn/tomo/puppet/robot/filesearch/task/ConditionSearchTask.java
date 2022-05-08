package cn.tomo.puppet.robot.filesearch.task;

import cn.tomo.puppet.robot.filesearch.LocalFileSearcher;
import cn.tomo.puppet.robot.filesearch.bean.filter.ConditionFilterConfig;

import java.io.File;

/**
 * Created by ______DancingWolf on 2017/11/20.
 */
public class ConditionSearchTask extends SearchTask{
    //filter config
    private ConditionFilterConfig filterConfig;

    public ConditionSearchTask(LocalFileSearcher searcher, File folder, ConditionFilterConfig filterConfig) {
        super(searcher, folder, filterConfig);
        this.filterConfig = filterConfig;
    }

    @Override
    public SearchTask newInstance(File folder) {
        return new ConditionSearchTask(this.searcher, folder, this.filterConfig);
    }

    @Override
    public boolean isMatch(File subFile) {
        //skip folders
        if(null == subFile){
            return false;
        }
        if(subFile.isDirectory()){
            return false;
        }

        //check file size
        long fileSize = subFile.length();
        if(null != this.filterConfig.getMaxFileSize()
                && this.filterConfig.getMaxFileSize() < fileSize){
            return false;
        }
        if(null != this.filterConfig.getMinFileSize()
                && this.filterConfig.getMinFileSize() > fileSize){
            return false;
        }

        //check file type
//        if(-1 != this.filterConfig.getFileType()){
//
//        }
        return true;
    }
}
