package cm.aptoide.pt.spotandshareapp.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cm.aptoide.pt.spotandshareapp.AppModel;
import cm.aptoide.pt.spotandshareapp.Header;
import cm.aptoide.pt.spotandshareapp.R;
import java.util.List;

/**
 * Created by filipe on 19-06-2017.
 */

public class SpotAndShareAppSelectionAdapter extends RecyclerView.Adapter<ViewHolder> {

  private static final int TYPE_HEADER = 0;
  private static final int TYPE_ITEM = 1;

  private Header header;
  private List<AppModel> installedApps;

  public SpotAndShareAppSelectionAdapter(Header header, List<AppModel> installedApps) {
    this.header = header;
    this.installedApps = installedApps;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == TYPE_HEADER) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.fragment_spotandshare_app_selection_header, parent, false);
      return new ViewHolderHeader(view);
    } else {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.fragment_spotandshare_app_selection_item, parent, false);
      return new ViewHolderItem(view);
    }
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    if (holder instanceof ViewHolderHeader) {
      ViewHolderHeader viewHolderHeader = (ViewHolderHeader) holder;
      viewHolderHeader.headerTextView.setText(header.getTitle());
    } else if (holder instanceof ViewHolderItem) {

      ViewHolderItem viewHolderItem = (ViewHolderItem) holder;
      viewHolderItem.appIcon.setImageDrawable(installedApps.get(position - 1)
          .getAppIcon());
    }
  }

  @Override public int getItemViewType(int position) {
    if (isPositionHeader(position)) {
      return TYPE_HEADER;
    }
    return TYPE_ITEM;
  }

  @Override public int getItemCount() {
    return installedApps.size() + 1;
  }

  public boolean isPositionHeader(int position) {
    return position == 0;
  }

  class ViewHolderHeader extends ViewHolder {

    private TextView headerTextView;

    public ViewHolderHeader(View itemView) {
      super(itemView);
      headerTextView = (TextView) itemView.findViewById(R.id.app_item_text_view);
    }
  }

  class ViewHolderItem extends ViewHolder {

    private ImageView appIcon;

    public ViewHolderItem(View itemView) {
      super(itemView);
      appIcon = (ImageView) itemView.findViewById(R.id.app_item_image_view);
    }
  }
}
