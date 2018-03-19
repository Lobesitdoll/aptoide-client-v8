package cm.aptoide.pt.home;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import cm.aptoide.accountmanager.AptoideAccountManager;
import cm.aptoide.pt.dataprovider.exception.NoNetworkConnectionException;
import cm.aptoide.pt.dataprovider.interfaces.TokenInvalidator;
import cm.aptoide.pt.dataprovider.model.v7.GetStoreWidgets;
import cm.aptoide.pt.dataprovider.ws.BodyInterceptor;
import cm.aptoide.pt.dataprovider.ws.v2.aptwords.AdsApplicationVersionCodeProvider;
import cm.aptoide.pt.dataprovider.ws.v7.BaseBody;
import cm.aptoide.pt.dataprovider.ws.v7.BaseRequestWithStore;
import cm.aptoide.pt.dataprovider.ws.v7.WSWidgetsUtils;
import cm.aptoide.pt.dataprovider.ws.v7.home.GetHomeBundlesRequest;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import rx.Observable;
import rx.Single;

/**
 * Created by jdandrade on 08/03/2018.
 */

public class RemoteBundleDataSource implements BundleDataSource {
  private final BodyInterceptor<BaseBody> bodyInterceptor;
  private final OkHttpClient okHttpClient;
  private final Converter.Factory converterFactory;
  private final BundlesResponseMapper mapper;
  private final TokenInvalidator tokenInvalidator;
  private final SharedPreferences sharedPreferences;
  private final WSWidgetsUtils widgetsUtils;
  private final BaseRequestWithStore.StoreCredentials storeCredentials;
  private final String clientUniqueId;
  private final boolean isGooglePlayServicesAvailable;
  private final String partnerId;
  private final AptoideAccountManager accountManager;
  private final String filters;
  private final Resources resources;
  private final WindowManager windowManager;
  private final ConnectivityManager connectivityManager;
  private final AdsApplicationVersionCodeProvider versionCodeProvider;
  private final int limit;
  private int total;
  private boolean loading;

  public RemoteBundleDataSource(int limit, int initialTotal,
      BodyInterceptor<BaseBody> bodyInterceptor, OkHttpClient okHttpClient,
      Converter.Factory converterFactory, BundlesResponseMapper mapper,
      TokenInvalidator tokenInvalidator, SharedPreferences sharedPreferences,
      WSWidgetsUtils widgetsUtils, BaseRequestWithStore.StoreCredentials storeCredentials,
      String clientUniqueId, boolean isGooglePlayServicesAvailable, String partnerId,
      AptoideAccountManager accountManager, String filters, Resources resources,
      WindowManager windowManager, ConnectivityManager connectivityManager,
      AdsApplicationVersionCodeProvider versionCodeProvider) {
    this.limit = limit;
    this.total = initialTotal;
    this.bodyInterceptor = bodyInterceptor;
    this.okHttpClient = okHttpClient;
    this.converterFactory = converterFactory;
    this.mapper = mapper;
    this.tokenInvalidator = tokenInvalidator;
    this.sharedPreferences = sharedPreferences;
    this.widgetsUtils = widgetsUtils;
    this.storeCredentials = storeCredentials;
    this.clientUniqueId = clientUniqueId;
    this.isGooglePlayServicesAvailable = isGooglePlayServicesAvailable;
    this.partnerId = partnerId;
    this.accountManager = accountManager;
    this.filters = filters;
    this.resources = resources;
    this.windowManager = windowManager;
    this.connectivityManager = connectivityManager;
    this.versionCodeProvider = versionCodeProvider;
  }

  private Single<HomeBundlesModel> getBundles(int offset, int limit, boolean invalidateHttpCache) {
    if (loading) {
      return Single.just(new HomeBundlesModel(true));
    }
    final boolean adultContentEnabled = accountManager.enabled()
        .first()
        .toSingle()
        .toBlocking()
        .value();

    return GetHomeBundlesRequest.of(limit, offset, okHttpClient, converterFactory, bodyInterceptor,
        tokenInvalidator, sharedPreferences, widgetsUtils, storeCredentials, clientUniqueId,
        isGooglePlayServicesAvailable, partnerId, adultContentEnabled, filters, resources,
        windowManager, connectivityManager, versionCodeProvider)
        .observe(invalidateHttpCache, false)
        .doOnSubscribe(() -> loading = true)
        .doOnUnsubscribe(() -> loading = false)
        .doOnTerminate(() -> loading = false)
        .flatMap(this::mapHomeResponse)
        .toSingle()
        .onErrorReturn(this::createErrorAppsList);
  }

  @NonNull private HomeBundlesModel createErrorAppsList(Throwable throwable) {
    if (throwable instanceof NoNetworkConnectionException) {
      return new HomeBundlesModel(HomeBundlesModel.Error.NETWORK);
    } else {
      return new HomeBundlesModel(HomeBundlesModel.Error.GENERIC);
    }
  }

  @NonNull private Observable<HomeBundlesModel> mapHomeResponse(GetStoreWidgets homeResponse) {
    if (homeResponse.isOk()) {
      List<HomeBundle> homeBundles = mapper.fromWidgetsToBundles(homeResponse.getDataList()
          .getList());
      total = homeResponse.getDataList()
          .getTotal();
      return Observable.just(new HomeBundlesModel(homeBundles, false, homeResponse.getDataList()
          .getNext()));
    }
    return Observable.error(
        new IllegalStateException("Could not obtain home bundles from server."));
  }

  @Override public Single<HomeBundlesModel> loadFreshHomeBundles() {
    return getBundles(0, limit, true);
  }

  @Override public Single<HomeBundlesModel> loadNextHomeBundles(int offset, int limit) {
    return getBundles(offset, limit, false);
  }

  @Override public boolean hasMore(Integer offset) {
    return offset < total && !loading;
  }
}