package cn.tomo.puppet.robot.filesearch.bean.filter;

/**
 * Created by ______DancingWolf on 2017/11/20.
 */
public class ConditionFilterConfig extends FilterConfig {
    //for file search only(null : not affected)
    private Long maxFileSize = null;
    private Long minFileSize = null;

    //file type(0:text file, 1:picture, 2:video)
    private int fileType = -1;//enum

    //check is the filter parameter valid
    public boolean isValid(){
        if(null == maxFileSize
                && null == minFileSize
                && -1 == fileType){
            //no filter
            System.out.println("No filter parameter set");
            return false;
        }

        //file size check
//        if(minFileSize>0 && maxFileSize>0 && minFileSize>maxFileSize){
//            System.out.println("File size error");
//            return false;
//        }
        return true;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public Long getMinFileSize() {
        return minFileSize;
    }

    public void setMinFileSize(Long minFileSize) {
        this.minFileSize = minFileSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
