package com.yueyue.rxjavademo.utils;

import com.yueyue.rxjavademo.model.GankBeauty;
import com.yueyue.rxjavademo.model.GankBeautyResult;
import com.yueyue.rxjavademo.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Function;

/**
 * author : yueyue on 2018/4/18 00:18
 * desc   :
 */
public class GankBeautyResultToItemsMapper implements Function<GankBeautyResult, List<Item>> {
    private static GankBeautyResultToItemsMapper INSTANCE = new GankBeautyResultToItemsMapper();

    private GankBeautyResultToItemsMapper() {
    }

    public static GankBeautyResultToItemsMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Item> apply(GankBeautyResult gankBeautyResult) {
        List<GankBeauty> gankBeauties = gankBeautyResult.beauties;
        List<Item> items = new ArrayList<>(gankBeauties.size());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.CHINA);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss", Locale.CHINA);
        for (GankBeauty gankBeauty : gankBeauties) {
            Item item = new Item();
            try {
                Date date = inputFormat.parse(gankBeauty.createdAt);
                item.description = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                item.description = "unknown date";
            }
            item.imageUrl = gankBeauty.url;
            items.add(item);
        }
        return items;
    }
}