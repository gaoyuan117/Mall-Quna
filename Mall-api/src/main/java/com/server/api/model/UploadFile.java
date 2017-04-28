
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class UploadFile extends BaseEntity {
    @SerializedName("data")
    public Data Data;

    public static class Data {
        public FileInfo file;
    }

    public static class FileInfo {

        public String id;

        public String ext;

        public String key;

        public String path;

        public String name;

        public String savename;

        public String savepath;

        public String mime;

        public String create_time;

        public String size;

        public String type;

        public String sha1;

    }
}
