package cm.aptoide.pt.home;

import java.util.List;
import rx.Single;

/**
 * Created by jdandrade on 08/03/2018.
 */

public interface BundleDataSource {

  Single<List<HomeBundle>> getBundles();
}
