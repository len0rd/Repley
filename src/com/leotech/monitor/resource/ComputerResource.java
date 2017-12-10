package com.leotech.monitor.resource;


import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Veeresh Rudrappa - http://www.veereshr.com/Java/ProcInfo
 */
public class ComputerResource {

  public static void getInfo() {
    try {
      Process p = Runtime.getRuntime().exec("top -l 1 -n 0");
      BufferedReader pReader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line;
      while ((line = pReader.readLine()) != null) {
        System.out.println(line);
      }
      pReader.close();
      p.waitFor();


    } catch (Exception e) {

    }
  }
}