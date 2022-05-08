package cn.tomo.puppet.robot.filesearch;

import cn.tomo.puppet.robot.filesearch.bean.SearchResult;

import java.io.File;

/**
 * Created by ______DancingWolf on 2017/11/16.
 */
public class SearchEventHandler {
    //search main
    private LocalFileSearcher searcher = null;

    //search result
    private SearchResult searchResult = null;

    public SearchEventHandler(LocalFileSearcher searcher) {
        this.searcher = searcher;
        this.searchResult = new SearchResult();
    }

    /*
         * @description: search events
         *
         * @author: ______DancingWolf
         * @date: 2017/11/16
         * @param: [event] event name
         * @param: [args] arguments
         * @return:
         */
    public void on(String event, Object[] args) {
        if (null == event) {
            return;
        }

        //search starts
        if("START_SEARCH".equals(event)){
            this.searchResult.setSearchStartMilis(System.currentTimeMillis());
            return;
        }

        //matched result found
        if ("FILE_FOUND".equals(event)) {
            //System.out.println("success: " + ((File) args[0]).getAbsolutePath());
            this.searchResult.getSearchSuccessCount().addAndGet(1);
            this.searcher.getFilePathSet().add(((File) args[0]).getAbsolutePath());
            return;
        }

        //folder searched
        if("END_SEARCH_FOLDER".equals(event)){
            this.searchResult.getSearchFolderCount().addAndGet(1);

            //check whether the search
            boolean hasSubFolders = (Boolean)(args[1]);
            if(!hasSubFolders){
                if(this.searcher.isTerminated()){ // search tasks terminated
                    this.searchResult.setSearchEndMilis(System.currentTimeMillis());
                    this.searcher.doSearchFinished(this.searchResult);
                }
            }
            return;
        }

        //file searched
        if("FILE_SEARCHED".equals(event)){
            this.searchResult.getSearchFileCount().addAndGet(1);
            return;
        }
    }
}
