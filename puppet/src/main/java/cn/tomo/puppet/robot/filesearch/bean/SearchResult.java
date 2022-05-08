package cn.tomo.puppet.robot.filesearch.bean;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ______DancingWolf on 2017/11/16.
 */
public class SearchResult {
    private Long searchStartMilis = 0L;//search start time in milis
    private Long searchEndMilis = 0L;

    //search files count
    private AtomicLong searchFileCount = new AtomicLong(0);
    private AtomicLong searchFolderCount = new AtomicLong(0);
    private AtomicLong searchSuccessCount= new AtomicLong(0);

    public Long getSearchStartMilis() {
        return searchStartMilis;
    }

    public void setSearchStartMilis(Long searchStartMilis) {
        this.searchStartMilis = searchStartMilis;
    }

    public Long getSearchEndMilis() {
        return searchEndMilis;
    }

    public void setSearchEndMilis(Long searchEndMilis) {
        this.searchEndMilis = searchEndMilis;
    }

    public AtomicLong getSearchFileCount() {
        return searchFileCount;
    }

    public void setSearchFileCount(AtomicLong searchFileCount) {
        this.searchFileCount = searchFileCount;
    }

    public AtomicLong getSearchFolderCount() {
        return searchFolderCount;
    }

    public void setSearchFolderCount(AtomicLong searchFolderCount) {
        this.searchFolderCount = searchFolderCount;
    }

    public AtomicLong getSearchSuccessCount() {
        return searchSuccessCount;
    }

    public void setSearchSuccessCount(AtomicLong searchSuccessCount) {
        this.searchSuccessCount = searchSuccessCount;
    }
}
