package com.leotech.monitor.model.modifier;

import java.util.ArrayList;
import java.util.List;

public abstract class ModifierContainer {

  public static final String DEFAULT_COLUMN_PARAM = "%sParam";
  //TODO:: K FAM. All of this needs to be less retarded... need to use pre-made library for query
  //generation or just make mods more intuitive, cuz there's already way too many issues with just the time mod
  //ideas: just get rid of the query component of modifiers (ie the column part with the ?) --> just have them store
  //the "pure modifier" (again though, issue with this would be a mod like yesterday)

  public static String generateModifierString(List<Modifier> modifiers) {
    StringBuilder generated = new StringBuilder("");

    if (modifiers != null && modifiers.size() > 0) {
      generated.append(modifiers.get(0).getStandardWhereClause());
      if (modifiers.size() > 1) {
        for (int i = 1; i < modifiers.size(); i++) {
          generated.append(" ").append(modifiers.get(i).getConcatenatedClause());
        }
      }
    }

    return generated.toString();
  }

  public static String generateClauselessModifierString(List<Modifier> modifiers) {
    StringBuilder generated = new StringBuilder("");

    if (modifiers != null && modifiers.size() > 0) {
      generated.append(modifiers.get(0).getPureModifier());
      for (int i = 1; i < modifiers.size(); i++) {
        generated.append(" ").append(modifiers.get(i).getConcatenatedClause());
      }
    }

    return generated.toString();
  }

  public static boolean containsModifier(List<Modifier> mods, Modifier modifierToFind) {
    for (Modifier mod : mods) {
      if (mod.getClass().equals(modifierToFind.getClass())) {
        return true;
      }
    }
    return false;
  }

  /**
   * FORMAT: "m=%MODIFIERNAME%--%MODIFIERVALUE%,%SECOND_MODIFIER%--%SECONDVALUE%", etc
   * EX: "m=time--TWO_DAY,arg--firstExtractionDate"
   * arg should immediately follow the modifier it applies to. You can list multiple modifiers concurrently
   * if the modifier you're using supports it
   */
  public static List<Modifier> getModifiers(String httpArg) {
    System.out.println("===getting modifiers00");
    if (httpArg == null || httpArg.length() < 1) {
      return null;
    }
    System.out.println("===getting modifiers");
    String[] modifierStrs = httpArg.split(",");
    List<Modifier> mods = null;

    mods = new ArrayList<>();
    //get all the key-value pairings into an easier-to-manage list:
    List<String[]> keyValueModPairs = new ArrayList<>();
    for (String modifierStr : modifierStrs) {
      String[] keyValue = modifierStr.split("--");
      keyValueModPairs.add(keyValue);
    }
    //now process input:
    boolean modAdded = false;
    List<String> modArgs = new ArrayList<>();
    for (String[] keyValue : keyValueModPairs) {
      if (keyValue.length > 1) {
        if (modAdded && keyValue[0].equalsIgnoreCase("arg")) {
          modArgs.add(keyValue[1]);
        } else {
          if (modAdded && !modArgs.isEmpty()) {
            mods.get(mods.size() - 1).setParam(modArgs.toArray(new String[modArgs.size()]));
            modAdded = false; //done with adding args for this modifier
          }
          Modifier mod = createModifier(keyValue[0], keyValue[1]);
          modArgs = new ArrayList<>();
          if (mod != null) {
            modAdded = true; //set to true so we keep track of the arguments to add to this mod
            mods.add(mod);
          }
        }
      }
    }
    System.out.println("===modifier get complete");

    return mods;
  }

  private static Modifier createModifier(String modType, String subType) {
    if (modType.equalsIgnoreCase(Time_Modifier.getQueryName())) {
      return Time_Modifier.getTimeModifier(subType);
    }
    return null;
    //add additional query-name modifier checks here
  }

  public boolean isModifier(Object o) {
    boolean isModifier = false;
    if (o instanceof Time_Modifier) {
      isModifier = true;
    }

    return isModifier;
  }

  /**
   * The time modifier allows you to modify a query
   * to return results in a given time interval compared against
   * TODO::Have a set param function for these? You're using them as objects so it prolly makes sense
   */
  public enum Time_Modifier implements Modifier {
    ALL {
      private final String baseClause = "? < NOW()"; //TODO is this valid?
      private String column = DEFAULT_COLUMN_PARAM;

      @Override
      public Modifier setParam(String[] parameterOrColumn) {
        column = parameterOrColumn[0] != null ? parameterOrColumn[0] : DEFAULT_COLUMN_PARAM;
        return this;
      }

      @Override
      public String getPureModifier() {
        return baseClause.substring(1);
      }

      @Override
      public String getStandardWhereClause() {
        return WHERE + baseClause.replace("?", column);
      }

      @Override
      public String getConcatenatedClause() {
        return AND + baseClause.replace("?", column);
      }
    },
    DAY {
      private final String baseClause = "? > CURDATE()";
      private String column = DEFAULT_COLUMN_PARAM;

      @Override
      public Modifier setParam(String[] parameterOrColumn) {
        column = parameterOrColumn[0] != null ? parameterOrColumn[0] : DEFAULT_COLUMN_PARAM;
        return this;
      }

      @Override
      public String getPureModifier() {
        return baseClause.substring(1);
      }

      @Override
      public String getStandardWhereClause() {
        return WHERE + baseClause.replace("?", column);
      }

      @Override
      public String getConcatenatedClause() {
        return AND + baseClause.replace("?", column);
      }
    },
    YESTERDAY {
      private final String baseClause = "(? > DATE_ADD(CURDATE(), INTERVAL -1 DAY) AND ? < CURDATE())";
      private String column = DEFAULT_COLUMN_PARAM;

      @Override
      public Modifier setParam(String[] parameterOrColumn) {
        column = parameterOrColumn[0] != null ? parameterOrColumn[0] : DEFAULT_COLUMN_PARAM;
        return this;
      }

      /**
       * NOTE!!
       * With this return you'll still have a ? after the and
       * @return just the clause without the first ?
       */
      @Override
      public String getPureModifier() {
        return baseClause.substring(1);
      }

      @Override
      public String getStandardWhereClause() {
        return WHERE + baseClause.replaceAll("\\?", column);
      }

      @Override
      public String getConcatenatedClause() {
        return AND + baseClause.replaceAll("\\?", column);
      }
    },
    TWO_DAY {
      private final String baseClause = "? > DATE_ADD(CURDATE(), INTERVAL -1 DAY)";
      private String column = DEFAULT_COLUMN_PARAM;

      @Override
      public Modifier setParam(String[] parameterOrColumn) {
        column = parameterOrColumn[0] != null ? parameterOrColumn[0] : DEFAULT_COLUMN_PARAM;
        return this;
      }

      @Override
      public String getPureModifier() {
        return baseClause.substring(1);
      }

      @Override
      public String getStandardWhereClause() {
        return WHERE + baseClause.replace("?", column);
      }

      @Override
      public String getConcatenatedClause() {
        return AND + baseClause.replace("?", column);
      }
    },
    HOUR_24 {
      private final String baseClause = "? > DATE_SUB(NOW(), INTERVAL 24 HOUR)";
      private String column = DEFAULT_COLUMN_PARAM;

      @Override
      public Modifier setParam(String[] parameterOrColumn) {
        column = parameterOrColumn[0] != null ? parameterOrColumn[0] : DEFAULT_COLUMN_PARAM;
        return this;
      }

      @Override
      public String getPureModifier() {
        return baseClause.substring(1);
      }

      @Override
      public String getStandardWhereClause() {
        return WHERE + baseClause.replace("?", column);
      }

      @Override
      public String getConcatenatedClause() {
        return AND + baseClause.replace("?", column);
      }
    },
    HOUR_48 {
      private final String baseClause = "? > DATE_SUB(NOW(), INTERVAL 48 HOUR)";
      private String column = DEFAULT_COLUMN_PARAM;

      @Override
      public Modifier setParam(String[] parameterOrColumn) {
        column = parameterOrColumn[0] != null ? parameterOrColumn[0] : DEFAULT_COLUMN_PARAM;
        return this;
      }

      @Override
      public String getPureModifier() {
        return baseClause.substring(1);
      }

      @Override
      public String getStandardWhereClause() {
        return WHERE + baseClause.replace("?", column);
      }

      @Override
      public String getConcatenatedClause() {
        return AND + baseClause.replace("?", column);
      }
    },
    WEEK {
      private final String baseClause = "? > DATE_SUB(CURDATE(), INTERVAL 6 DAY)";
      private String column = DEFAULT_COLUMN_PARAM;

      @Override
      public Modifier setParam(String[] parameterOrColumn) {
        column = parameterOrColumn[0] != null ? parameterOrColumn[0] : DEFAULT_COLUMN_PARAM;
        return this;
      }

      @Override
      public String getPureModifier() {
        return baseClause.substring(1);
      }

      @Override
      public String getStandardWhereClause() {
        return WHERE + baseClause.replace("?", column);
      }

      @Override
      public String getConcatenatedClause() {
        return AND + baseClause.replace("?", column);
      }
    };

    private static final String WHERE = "WHERE ";
    private static final String AND = "AND ";

    public static String getQueryName() {
      return "time";
    }

    public static Modifier getTimeModifier(String subType) {
      if (subType.equalsIgnoreCase(Time_Modifier.ALL.name())) {
        return ALL;
      } else if (subType.equalsIgnoreCase(Time_Modifier.DAY.name())) {
        return DAY;
      } else if (subType.equalsIgnoreCase(Time_Modifier.YESTERDAY.name())) {
        return YESTERDAY;
      } else if (subType.equalsIgnoreCase(Time_Modifier.TWO_DAY.name())) {
        return TWO_DAY;
      } else if (subType.equalsIgnoreCase(Time_Modifier.HOUR_24.name())) {
        return HOUR_24;
      } else if (subType.equalsIgnoreCase(Time_Modifier.HOUR_48.name())) {
        return HOUR_48;
      } else if (subType.equalsIgnoreCase(Time_Modifier.WEEK.name())) {
        return WEEK;
      }
      return null;
    }
  }
}


