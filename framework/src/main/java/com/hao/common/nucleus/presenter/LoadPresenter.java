package com.hao.common.nucleus.presenter;

import com.hao.common.exception.ErrorMessageFactory;
import com.hao.common.exception.NoMoreDataException;
import com.hao.common.exception.NotFoundDataException;
import com.hao.common.nucleus.view.loadview.ILoadDataView;
import com.hao.common.nucleus.view.loadview.ILoadPageListDataView;
import com.hao.common.rx.RxUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;

/**
 * @Package com.hao.common.base
 * @作 用:加载数据的Presenter
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月29日  17:55
 */


public class LoadPresenter extends RxPresenter {
    public int page = 1;//索引
    public int page_size = 15;
    protected boolean isShowing = true;//是否显示加载view

    private final static int LOAD_MODEL = 0;
    private final static int LOAD_LIST = 1;

    //####################################### Start加载Model #######################################################
    private class DefaultModelAction2<View extends ILoadDataView<Model>, Model> implements Action2<View, Model> {
        @Override
        public void call(View view, Model model) {
            view.loadDataToUI(model);
            view.showContentView();
        }
    }

    public LoadPresenter restPageSize() {
        this.page = 1;
        return this;
    }

    public LoadPresenter setShowing(boolean showing) {
        this.isShowing = showing;
        return this;
    }

    public LoadPresenter setPageSize() {
        this.page += 1;
        return this;
    }

    private class DefaultModelErrorAction2<View extends ILoadDataView> implements Action2<View, Throwable> {
        @Override
        public void call(View view, Throwable e) {
            if (isShowing) {
                if (e instanceof NotFoundDataException) {
                    view.showEmptyView();
                } else {
                    view.showFailView();
                    view.showError(ErrorMessageFactory.create(view.getContext(), (Exception) e));
                }
            }
        }
    }


    public <View extends ILoadDataView<Model>, Model> void loadModel(Observable<Model> observable) {
        restartableFirst(LOAD_MODEL, new Func0<Observable<Model>>() {
            @Override
            public Observable<Model> call() {
                return observable;
            }
        }, new DefaultModelAction2<View, Model>(), new DefaultModelErrorAction2<View>());
        start(LOAD_MODEL);
    }
    //####################################### End加载Model #######################################################

    //####################################### Start加载Lisst #######################################################
    private class DefaultListErrorAction2<View extends ILoadPageListDataView> implements Action2<View, Throwable> {

        @Override
        public void call(View view, Throwable e) {
            page--;
            if (e instanceof NotFoundDataException) {
                if (isShowing) {
                    view.showEmptyView();
                } else {
                    view.onNoDate();
                }
            } else if (e instanceof NoMoreDataException) {
                view.onNoMoreLoad();
            } else {
                view.showError(ErrorMessageFactory.create(view.getContext(), (Exception) e));
            }
        }
    }

    private class DefaultListAction2<View extends ILoadPageListDataView<Model>, Model> implements Action2<View, List<Model>> {

        @Override
        public void call(View view, List<Model> models) {
            if (isShowing) {
                view.showContentView();
            }
            if (isRefresh()) {
                view.onRefreshDataToUI(models);
                view.onRefreshComplete();
            } else {
                view.onLoadMoreDataToUI(models);
                view.onLoadComplete();
            }
        }
    }

    /**
     * 是否是刷新
     *
     * @return
     */
    protected boolean isRefresh() {
        return page == 1;
    }


    public <View extends ILoadPageListDataView<Model>, Model> void loadList(Observable<List<Model>> observable) {
        restartableFirst(LOAD_LIST, new Func0<Observable<List<Model>>>() {
            @Override
            public Observable<List<Model>> call() {
                return observable;
            }
        }, new DefaultListAction2<View, Model>(), new DefaultListErrorAction2<View>());
        start(LOAD_LIST);
    }
    //####################################### End加载Lisst #######################################################

}
