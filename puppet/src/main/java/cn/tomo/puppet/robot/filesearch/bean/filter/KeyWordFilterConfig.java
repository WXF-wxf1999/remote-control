package cn.tomo.puppet.robot.filesearch.bean.filter;

/**
 * Created by ______DancingWolf on 2017/11/20.
 */
public class KeyWordFilterConfig extends FilterConfig {
    //private String keyWord = null;//key word

    private boolean matchStart = false; //only match the start of the file name
    private boolean matchEnd = false; // only match the end of the file name
    private boolean caseSensitive = false;//whether ignore the case

//    public KeyWordFilterConfig(String keyWord){
//        this.keyWord = keyWord;
//    }
//
//    public String getKeyWord() {
//        return keyWord;
//    }
//
//    public void setKeyWord(String keyWord) {
//        this.keyWord = keyWord;
//    }

    public boolean isMatchStart() {
        return matchStart;
    }

    public void setMatchStart(boolean matchStart) {
        this.matchStart = matchStart;
    }

    public boolean isMatchEnd() {
        return matchEnd;
    }

    public void setMatchEnd(boolean matchEnd) {
        this.matchEnd = matchEnd;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
