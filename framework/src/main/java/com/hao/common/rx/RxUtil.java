/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hao.common.rx;

import com.hao.common.exception.ApiException;
import com.hao.common.exception.NoMoreDataException;
import com.hao.common.exception.NotFoundDataException;
import com.hao.common.executor.JobExecutor;
import com.hao.common.executor.UIThread;
import com.hao.common.net.result.ErrorCode;
import com.hao.common.net.result.RESTResult;
import com.hao.common.net.result.Result;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.LifecycleProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxUtil {
    private RxUtil() {
    }

    public static <T> Observable.Transformer<T, T> applySchedulersJobUI() {
        return observable -> observable
                .subscribeOn(Schedulers.from(JobExecutor.newInstance()))
                .observeOn(UIThread.newInstance().getScheduler());
    }

    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable.Transformer<T, T> applySchedulersBindToLifecycle(LifecycleProvider lifecycleProvider) {
        if (lifecycleProvider == null) {
            return observable -> observable.compose(RxUtil.applySchedulers());
        } else {
            return observable -> observable.compose(RxUtil.applySchedulers()).compose(lifecycleProvider.bindToLifecycle());
        }
    }

    public static <T> Observable<T> runInUIThread(T t) {
        return Observable.just(t).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> runInUIThreadDelay(T t, long delayMillis) {
        return Observable.just(t).delaySubscription(delayMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> runInUIThreadDelay(T t, long delayMillis, LifecycleProvider lifecycleProvider) {
        return Observable.just(t).delaySubscription(delayMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).compose(lifecycleProvider.bindToLifecycle());
    }

    public static Observable<Void> runInUIThread() {
        return runInUIThread(null);
    }

    public static Observable<Void> runInUIThreadDelay(long delayMillis) {
        return runInUIThreadDelay(null, delayMillis);
    }

    public static Observable<Void> runInUIThreadDelay(long delayMillis, LifecycleProvider lifecycleProvider) {
        return runInUIThreadDelay(null, delayMillis, lifecycleProvider);
    }

    public static <T> Observable<T> runInIoThread(T t) {
        return Observable.just(t).observeOn(Schedulers.io());
    }

    public static Observable<Void> runInIoThread() {
        return runInIoThread(null);
    }

    public static <T> Observable<T> runInIoThreadDelay(T t, long delayMillis) {
        return Observable.just(t).delaySubscription(delayMillis, TimeUnit.MILLISECONDS, Schedulers.io());
    }

    public static Observable<Void> runInIoThreadDelay(long delayMillis) {
        return runInIoThreadDelay(null, delayMillis);
    }


    /**
     * 将RESTResult<T>转换为T
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<RESTResult<T>, T> transformationData() {
        return new Observable.Transformer<RESTResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<RESTResult<T>> restResultObservable) {
                return restResultObservable.flatMap(new Func1<RESTResult<T>, Observable<T>>() {

                    @Override
                    public Observable<T> call(RESTResult<T> trestResult) {
                        Logger.e(trestResult.toString());
                        if (trestResult.getErrCode() == 1000) {
                            return Observable.just(trestResult.getResult());
                        } else {
                            return Observable.error(new ApiException(ErrorCode.getMsg(trestResult.getErrCode()), trestResult.getErrCode()));
                        }
                    }
                });
            }
        };
    }


    /**
     * 将RESTResult<T>转换为pageList<T>
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<Result<T>, List<T>> transtormationPageList() {
        return new Observable.Transformer<Result<T>, List<T>>() {
            @Override
            public Observable<List<T>> call(Observable<Result<T>> resultObservable) {
                return resultObservable.flatMap(new Func1<Result<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(Result<T> tResult) {
                        if (tResult.getTotal() == 0) {
                            return Observable.error(new NotFoundDataException());
                        } else if (tResult.getCurrently() > tResult.getTotal()) {
                            return Observable.error(new NoMoreDataException());
                        } else {
                            return Observable.just(tResult.getList());
                        }

                    }
                });
            }
        };
    }


    /**
     * 将RESTResult<T>转换为List<T>
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<Result<T>, List<T>> transtormationList() {
        return new Observable.Transformer<Result<T>, List<T>>() {
            @Override
            public Observable<List<T>> call(Observable<Result<T>> resultObservable) {
                return resultObservable.flatMap(new Func1<Result<T>, Observable<List<T>>>() {

                    @Override
                    public Observable<List<T>> call(Result<T> tResult) {
                        if (tResult.getList() == null || tResult.getList().size() == 0) {
                            return Observable.error(new NotFoundDataException());
                        } else {
                            return Observable.just(tResult.getList());
                        }
                    }
                });
            }
        };
    }


}