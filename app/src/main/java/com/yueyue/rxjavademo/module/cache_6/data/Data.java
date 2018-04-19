package com.yueyue.rxjavademo.module.cache_6.data;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.yueyue.rxjavademo.App;
import com.yueyue.rxjavademo.R;
import com.yueyue.rxjavademo.model.Item;
import com.yueyue.rxjavademo.network.NetworkService;
import com.yueyue.rxjavademo.utils.GankBeautyResultToItemsMapper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * author : yueyue on 2018/4/19 09:45
 * desc   :
 */
public class Data {
    private static Data instance;
    private static final int DATA_SOURCE_MEMORY = 1;//从内存
    private static final int DATA_SOURCE_DISK = 2;//从磁盘
    private static final int DATA_SOURCE_NETWORK = 3;//从网络

    private Data() {

    }

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    @IntDef({DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK})
    @interface DataSource {
    }

    private int dataSource;

    BehaviorSubject<List<Item>> cache;

    private void setDataSource(@DataSource int dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSourceText() {
        int dataSourceTextRes;
        switch (dataSource) {
            case DATA_SOURCE_MEMORY:
                dataSourceTextRes = R.string.data_source_memory;
                break;
            case DATA_SOURCE_DISK:
                dataSourceTextRes = R.string.data_source_disk;
                break;
            case DATA_SOURCE_NETWORK:
                dataSourceTextRes = R.string.data_source_network;
                break;
            default:
                dataSourceTextRes = R.string.data_source_network;
        }
        return App.getContext().getString(dataSourceTextRes);
    }


    public Disposable subscribeData(@NonNull Consumer<List<Item>> onNext,
                                    @NonNull Consumer<Throwable> onError) {
        if (cache == null) {
            //说明内存中还没有图片
            cache = BehaviorSubject.create();

            Observable.create(new ObservableOnSubscribe<List<Item>>() {
                @Override
                public void subscribe(ObservableEmitter<List<Item>> e) throws Exception {
                    List<Item> items = Database.getInstance().readItems();//从磁盘拿出图片路径,以此判断是否加载过
                    if (items == null) {
                        //网络
                        setDataSource(DATA_SOURCE_NETWORK);
                        loadFromNetwork();
                    } else {
                        //磁盘
                        setDataSource(DATA_SOURCE_DISK);
                        e.onNext(items);
                    }
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe(cache);

        } else {
            setDataSource(DATA_SOURCE_MEMORY);
        }
        return cache.doOnError(throwable -> cache = null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    private void loadFromNetwork() {
        Disposable disposable = NetworkService.getGankApi()
                .getBeauties(100, 1)
                .subscribeOn(Schedulers.io())
                .map(GankBeautyResultToItemsMapper.getInstance())
                .doOnNext(items -> Database.getInstance().writeItems(items))
                .subscribe(
                        items -> cache.onNext(items),
                        throwable -> {
                            cache.onError(throwable);
                            throwable.printStackTrace();
                        }
                );
    }


    public void clearMemoryCache() {
        cache = null;
    }

    public void clearMemoryAndDiskCache() {
        clearMemoryCache();
        Database.getInstance().delete();
    }

}
