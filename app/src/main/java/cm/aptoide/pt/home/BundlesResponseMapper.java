package cm.aptoide.pt.home;

import android.support.annotation.NonNull;
import cm.aptoide.pt.dataprovider.model.v2.GetAdsResponse;
import cm.aptoide.pt.dataprovider.model.v7.GetStoreWidgets;
import cm.aptoide.pt.dataprovider.model.v7.Layout;
import cm.aptoide.pt.dataprovider.model.v7.ListApps;
import cm.aptoide.pt.dataprovider.model.v7.Type;
import cm.aptoide.pt.dataprovider.model.v7.listapp.App;
import cm.aptoide.pt.view.app.Application;
import cm.aptoide.pt.view.app.FeatureGraphicApplication;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.functions.Func1;

/**
 * Created by jdandrade on 08/03/2018.
 */

public class BundlesResponseMapper {
  @NonNull Func1<GetStoreWidgets, List<HomeBundle>> map() {
    return bundlesResponse -> fromWidgetsToBundles(bundlesResponse.getDataList()
        .getList());
  }

  private List<HomeBundle> fromWidgetsToBundles(List<GetStoreWidgets.WSWidget> widgetBundles) {

    List<HomeBundle> appBundles = new ArrayList<>();

    for (GetStoreWidgets.WSWidget widget : widgetBundles) {
      GetStoreWidgets.WSWidget.Data data = widget.getData();
      if (data == null) {
        continue;
      }
      AppBundle.BundleType type = bundleTypeMapper(widget.getType(), data.getLayout());
      if (type.equals(HomeBundle.BundleType.APPS) || type.equals(HomeBundle.BundleType.EDITORS)) {
        try {
          appBundles.add(new AppBundle(widget.getTitle(), applicationsToApps(
              ((ListApps) widget.getViewObject()).getDataList()
                  .getList(), type), type));
        } catch (Exception ignore) {
        }
      } else if (type.equals(HomeBundle.BundleType.ADS)) {
        appBundles.add(
            new AdBundle(widget.getTitle(), ((GetAdsResponse) widget.getViewObject()).getAds()));
      }
    }

    return appBundles;
  }

  private AppBundle.BundleType bundleTypeMapper(Type type, Layout layout) {
    switch (type) {
      case APPS_GROUP:
        if (layout.equals(Layout.BRICK)) {
          return HomeBundle.BundleType.EDITORS;
        } else {
          return HomeBundle.BundleType.APPS;
        }
      default:
        return HomeBundle.BundleType.APPS;
    }
  }

  private List<Application> applicationsToApps(List<App> apps, AppBundle.BundleType type) {
    if (apps == null || apps.isEmpty()) {
      return Collections.EMPTY_LIST;
    }
    List<Application> applications = new ArrayList<>();
    for (App app : apps) {
      if (type.equals(HomeBundle.BundleType.EDITORS)) {
        applications.add(new FeatureGraphicApplication(app.getName(), app.getIcon(), app.getStats()
            .getRating()
            .getAvg(), app.getStats()
            .getPdownloads(), app.getPackageName(), app.getId(), app.getGraphic()));
      } else {
        applications.add(new Application(app.getName(), app.getIcon(), app.getStats()
            .getRating()
            .getAvg(), app.getStats()
            .getPdownloads(), app.getPackageName(), app.getId()));
      }
    }

    return applications;
  }
}
