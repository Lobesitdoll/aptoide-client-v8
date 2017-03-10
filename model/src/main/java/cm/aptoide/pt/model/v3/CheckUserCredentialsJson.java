/*
 * Copyright (c) 2016.
 * Modified by Neurophobic Animal on 26/04/2016.
 */

package cm.aptoide.pt.model.v3;

import cm.aptoide.pt.model.v3.BaseV3Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by brutus on 09-12-2013.
 */
@Data @EqualsAndHashCode(callSuper = true) public class CheckUserCredentialsJson
    extends BaseV3Response {

  public int id;
  public String token;
  public String repo;
  public String avatar;
  public String username;
  public String email;
  public Settings settings;
  public String access;
  @JsonProperty("access_confirmed") public boolean accessConfirmed;
  public String ravatarHd;

  @Data public static class Settings {
    @JsonProperty("matureswitch") public String matureswitch;
  }
}