package lbcy.com.cn.wristband.entity;

import java.util.List;

/**
 * Created by chenjie on 2017/10/10.
 */

public class HardwareUpdateBean {

    /**
     * result : 1
     * param : [{"type":"mcu","url":"http:","newVersion":"0.06","oldVersion":"0.01","fileSize":0}]
     */

    private int result;
    private List<ParamBean> param;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<ParamBean> getParam() {
        return param;
    }

    public void setParam(List<ParamBean> param) {
        this.param = param;
    }

    public static class ParamBean {
        /**
         * type : mcu
         * url : http:
         * newVersion : 0.06
         * oldVersion : 0.01
         * fileSize : 0
         */

        private String type;
        private String url;
        private String newVersion;
        private String oldVersion;
        private int fileSize;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getNewVersion() {
            return newVersion;
        }

        public void setNewVersion(String newVersion) {
            this.newVersion = newVersion;
        }

        public String getOldVersion() {
            return oldVersion;
        }

        public void setOldVersion(String oldVersion) {
            this.oldVersion = oldVersion;
        }

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }
    }
}
