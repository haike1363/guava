package pers.haike.demo;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class GuavaCache {

    static class Key {
    }

    static class Graph {
    }

    static Graph createExpensiveGraph(Key key) {
        return new Graph();
    }

    @Test
    public void testLoadingCache() throws Exception {
        LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
                //.maximumSize(10)// size based Eviction
                .maximumWeight(10)
                .weigher(new Weigher<Key, Graph>() {
                    @Override
                    public int weigh(Key key, Graph value) {
                        return 1;
                    }
                })
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .refreshAfterWrite(1, TimeUnit.MINUTES)
                .removalListener(new RemovalListener<Key, Graph>() {
                    @Override
                    public void onRemoval(RemovalNotification<Key, Graph> notification) {
                        System.out.println(notification.getKey());
                    }
                })
                .build(new CacheLoader<Key, Graph>() {
                    @Override
                    public Graph load(Key key) throws Exception {
                        // create new Value
                        return createExpensiveGraph(key);
                    }

                    @Override
                    public ListenableFuture<Graph> reload(Key key, Graph oldValue) throws Exception {
                        // create new value use oldValue
                        return super.reload(key, oldValue);
                    }

                    @Override
                    public Map<Key, Graph> loadAll(Iterable<? extends Key> keys) throws Exception {
                        return super.loadAll(keys);
                    }
                });
        Key key = new Key();
        // 直接put
        graphs.put(key, new Graph());

        // 刷新
        graphs.refresh(key);

        // get
        graphs.get(key);// 如果缺少将调用到CacheLoader

        // get 如果不存在,则调用Callable
        try {
            Graph getByCall = graphs.get(new Key(), new Callable<Graph>() {
                @Override
                public Graph call() throws Exception {
                    // 不能返回空
                    return null;
                }
            });
            System.out.println("getByCall: " + getByCall);
        } catch (Throwable e) {
            System.out.println("getByCall: " + e.getMessage());
        }

        // 批量get
        List<Key> keys = new ArrayList<>();
        graphs.getAll(keys);

        // 手动删除
        graphs.invalidate(key);
        graphs.invalidateAll(keys);
        graphs.cleanUp();
    }
}
