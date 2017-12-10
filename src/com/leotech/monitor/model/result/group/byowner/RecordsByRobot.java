package com.leotech.monitor.model.result.group.byowner;

import com.leotech.monitor.model.modifier.Modifier;
import com.leotech.monitor.model.result.group.GroupByCount;
import java.util.List;

public class RecordsByRobot extends GroupByCount {

  public RecordsByRobot(String byOwnerType, List<Modifier> modifiers) {
    super("Robot_Name", "re_" + (byOwnerType == null && !byOwnerType.isEmpty() ?
        "fsbo_front" : byOwnerType.toLowerCase()) + ".re_fsbo", "Robot_Name", modifiers);
    super.setCountColumnMask("Records");
  }

}
