package cn.tomo.puppet.robot.filesearch.task;

import cn.tomo.puppet.robot.filesearch.LocalFileSearcher;
import cn.tomo.puppet.robot.filesearch.bean.filter.FilterConfig;

import java.io.File;

/**
 * Created by ______DancingWolf on 2017/11/16.
 */
public abstract class SearchTask implements Runnable{

    //search instance
    protected LocalFileSearcher searcher = null;

    //current search folder
    protected File folder = null;

    //search filter config
    protected FilterConfig filterConfig = null;

    public SearchTask(LocalFileSearcher searcher, File folder, FilterConfig filterConfig){
        this.searcher = searcher;
        this.folder = folder;
        this.filterConfig = filterConfig;
    }

    /*
     * @description: search file
     *
     * @author: ______DancingWolf
     * @date: 2017/11/16
     * @param:
     * @return:
     */
    public void run(){
        if(null==this.searcher || null==this.folder || null==this.filterConfig){
            System.out.print("参数异常");
            return;
        }
        //this.searcher.fireEvent("START_SEARCH_FOLDER", this.folder);

        //list sub-files and sub-folders and check
        File[] subFiles = this.folder.listFiles();
        if(null==subFiles || 0==subFiles.length){
            return;
        }

        //check whether the folder has sub-folders
        boolean hasSubFolders = false;

        //loop the sub files
        boolean isSubFileADirectory;
        for(File subFile : subFiles){
            isSubFileADirectory = subFile.isDirectory();

            //generate a new thread for folder(after check)
            if(isSubFileADirectory){
                hasSubFolders = true;
                SearchTask newTask = this.newInstance(subFile);
                this.searcher.startNewTask(newTask);
            } else{
                //file searched
                this.searcher.fireEvent("FILE_SEARCHED", subFile);
            }

            //filter
            if(!this.filterConfig.isSearchFile() && !isSubFileADirectory){
                continue;
            }
            if(!this.filterConfig.isSearchFolder() && isSubFileADirectory){
                continue;
            }
            if(this.isMatch(subFile)){
                this.searcher.fireEvent("FILE_FOUND", subFile);
            }
        }

        //exit
        this.searcher.fireEvent("END_SEARCH_FOLDER", this.folder, hasSubFolders);
    }

    //new thread
    public abstract SearchTask newInstance(File folder);

    //is subFile match the search condition
    public abstract boolean isMatch(File subFile);
}
