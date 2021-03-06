package cm.aptoide.pt.social.view;

import cm.aptoide.accountmanager.Account;
import cm.aptoide.pt.presenter.View;
import cm.aptoide.pt.social.data.CardTouchEvent;
import cm.aptoide.pt.social.data.Post;
import cm.aptoide.pt.social.data.PostComment;
import cm.aptoide.pt.social.data.SocialAction;
import cm.aptoide.pt.social.data.SocialCardTouchEvent;
import cm.aptoide.pt.social.data.share.ShareEvent;
import java.util.List;
import rx.Completable;
import rx.Observable;
import rx.Single;

/**
 * Created by jdandrade on 31/05/2017.
 */

public interface TimelineView extends View {

  void showCards(List<Post> cards);

  void showGeneralProgressIndicator();

  void hideGeneralProgressIndicator();

  void hideRefresh();

  void showMoreCards(List<Post> cards);

  void showGenericViewError();

  Observable<Void> refreshes();

  Observable<Object> reachesBottom();

  Observable<CardTouchEvent> postClicked();

  Observable<ShareEvent> shareConfirmation();

  Observable<PostComment> commentPosted();

  Observable<Void> retry();

  void showLoadMoreProgressIndicator();

  void hideLoadMoreProgressIndicator();

  Observable<Void> floatingActionButtonClicked();

  Completable showFloatingActionButton();

  Completable hideFloatingActionButton();

  Observable<Direction> scrolled();

  void showRootAccessDialog();

  void updatePost(int cardPosition);

  void swapPost(Post post, int postPosition);

  void showStoreSubscribedMessage(String storeName);

  void showStoreUnsubscribedMessage(String storeName);

  void showSharePreview(Post post, Account account);

  void showSharePreview(Post originalPost, Post card, Account account);

  void showShareSuccessMessage();

  void showCommentDialog(SocialCardTouchEvent touchEvent);

  void showGenericError();

  void showLoginPromptWithAction();

  Observable<Void> loginActionClick();

  void showCreateStoreMessage(SocialAction socialAction);

  void showSetUserOrStorePublicMessage();

  void showPostProgressIndicator();

  void hidePostProgressIndicator();

  void removePost(Post post);

  Observable<Post> getVisibleItems();

  void showUser(TimelineUser user);

  void showUserLoading();

  void hideUser();

  void showEmptyState();

  Observable<Integer> getScrollEvents();

  Single<String> takeFeedbackScreenShot();

  void showUserUnsubscribedMessage(String userName);

  void showLastComment(String comment);

  void sendCommentSuccessAnalytics(String postId);

  void sendCommentErrorAnalytics(String postId);
}
