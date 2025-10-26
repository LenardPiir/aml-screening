package com.lhv.amlscreening.adaptor.input.api;

import com.lhv.amlscreening.common.api.CommonApi;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SanctionedListApi {
  public static final String ROOT_SANCTIONED_LIST = "/sanctioned-list";
  public static final String ROOT_SANCTIONED_LIST_V1 = ROOT_SANCTIONED_LIST + CommonApi.V1;

  public static final String PATH_CHECK_NAME = "/check-name";
  public static final String PATH_ADD_NAME = "/add-name";
  public static final String PATH_UPDATE_NAME = "/update-name";
  public static final String PATH_DELETE_NAME = "/delete-name";
}
