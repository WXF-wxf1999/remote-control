package cn.tomo.puppet.robot.filesearch.task;

import cn.tomo.puppet.robot.filesearch.LocalFileSearcher;
import cn.tomo.puppet.robot.filesearch.bean.filter.KeyWordFilterConfig;

import java.io.File;

/**
 * Created by ______DancingWolf on 2017/11/16.
 */
public class KeyWordSearchTask extends SearchTask {
    //key word
    private String keyWord;

    //filter config
    private KeyWordFilterConfig keyWordFilterConfig;

    public KeyWordSearchTask(LocalFileSearcher searcher, File folder, KeyWordFilterConfig keyWordFilterConfig, String keyWord) {
        super(searcher, folder, keyWordFilterConfig);
        this.keyWord = keyWord;
        this.keyWordFilterConfig = keyWordFilterConfig;
    }


    @Override
    public SearchTask newInstance(File folder) {
        return new KeyWordSearchTask(this.searcher, folder, this.keyWordFilterConfig, this.keyWord);
    }

    @Override
    public boolean isMatch(File subFile) {
        if(null == subFile){
            return false;
        }

        //current search file
        String fileName = subFile.getName();
        String keyWord = this.keyWord;

        //ignore case
        if(!this.keyWordFilterConfig.isCaseSensitive()){
            fileName = fileName.toUpperCase();
            keyWord = keyWord.toUpperCase();
        }

        //compare
        if(this.keyWordFilterConfig.isMatchStart()){
           return fileName.startsWith(keyWord);
        }
        if(this.keyWordFilterConfig.isMatchEnd()){
            return fileName.endsWith(keyWord);
        }
        return fileName.contains(keyWord);
    }
}
