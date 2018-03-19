package cm.aptoide.pt.store.view.my;

import cm.aptoide.pt.home.AptoideBottomNavigator;
import cm.aptoide.pt.home.BottomNavigationItem;
import cm.aptoide.pt.home.BottomNavigationMapper;
import cm.aptoide.pt.presenter.Presenter;
import cm.aptoide.pt.presenter.View;
import rx.Scheduler;
import rx.exceptions.OnErrorNotImplementedException;

/**
 * Created by D01 on 14/03/18.
 */

public class MyStoresPresenter implements Presenter {

  private final MyStoresView view;
  private final AptoideBottomNavigator aptoideBottomNavigator;
  private final Scheduler viewSchedulers;
  private final BottomNavigationMapper bottomNavigationMapper;

  public MyStoresPresenter(MyStoresView view, AptoideBottomNavigator aptoideBottomNavigator,
      Scheduler viewSchedulers, BottomNavigationMapper bottomNavigationMapper) {
    this.view = view;
    this.aptoideBottomNavigator = aptoideBottomNavigator;
    this.viewSchedulers = viewSchedulers;
    this.bottomNavigationMapper = bottomNavigationMapper;
  }

  @Override public void present() {
    view.getLifecycle()
        .filter(lifecycleEvent -> lifecycleEvent.equals(View.LifecycleEvent.CREATE))
        .flatMap(created -> aptoideBottomNavigator.navigationEvent()
            .filter(navigationEvent -> bottomNavigationMapper.mapItemClicked(navigationEvent)
                .equals(BottomNavigationItem.STORES))
            .observeOn(viewSchedulers)
            .doOnNext(navigated -> view.scrollToTop())
            .retry())
        .compose(view.bindUntilEvent(cm.aptoide.pt.presenter.View.LifecycleEvent.DESTROY))
        .subscribe(__ -> {
        }, throwable -> {
          throw new OnErrorNotImplementedException(throwable);
        });
  }
}