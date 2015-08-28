package com.troy.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class JianyueDaoGenerator {
    private static final int VERSION = 1;
    private static final String DEFAULT_PACKAGE = "com.troy.greendao";
    private static final String OUT_DIR = "./app/src/main/java-gen";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(VERSION, DEFAULT_PACKAGE);
        addCache(schema, "WeiXinCache");
        addCache(schema, "PictureCache");
        addCache(schema, "VideoCache");
        new DaoGenerator().generateAll(schema, OUT_DIR);
    }

    private static void addCache(Schema schema, String className) {
        Entity entity = schema.addEntity(className);
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("result");
        entity.addIntProperty("page");
    }
}
