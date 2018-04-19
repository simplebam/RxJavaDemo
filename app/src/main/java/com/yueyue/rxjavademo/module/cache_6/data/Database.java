package com.yueyue.rxjavademo.module.cache_6.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yueyue.rxjavademo.App;
import com.yueyue.rxjavademo.model.Item;
import com.yueyue.rxjavademo.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * author : yueyue on 2018/4/19 09:57
 * desc   : 这里存储的是图片的路径,存储内容查看project/art/data.db(使用AS/文本编辑器打开即可)
 * 图片缓存是Glide实现了
 */
public class Database {

    private static String DATA_FILE_NAME = "data.db";

    private static Database INSTANCE;

    private File dataFile = FileUtil.getDiskCacheDir(App.getContext(), DATA_FILE_NAME);

    private Database() {
    }

    public static Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

    public List<Item> readItems() {
        // Hard code adding some delay, to distinguish reading from memory and reading disk clearly
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Reader reader = new FileReader(dataFile);
            return new Gson().fromJson(reader, new TypeToken<List<Item>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeItems(List<Item> items) {
        String json = new Gson().toJson(items);
        try {
            if (!dataFile.exists()) {
                try {
                    dataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        if (dataFile != null) {
            dataFile.delete();
        }
    }
}
