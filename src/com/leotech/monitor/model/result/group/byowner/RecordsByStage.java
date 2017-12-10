package com.leotech.monitor.model.result.group.byowner;

import com.leotech.monitor.model.result.group.GroupByCount;

public class RecordsByStage extends GroupByCount {

  public RecordsByStage(String byOwnerType) {
    super("stage", "re_" + (byOwnerType == null && !byOwnerType.isEmpty() ?
        "fsbo_front" : byOwnerType.toLowerCase()) + ".re_fsbo", "stage");
    super.setCountColumnMask("Records");
  }
}
