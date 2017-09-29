package com.daoda.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Schema;

/**
 * @Package com.daoda.main
 * @作 用:
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年09月26日  15:09
 */

public class Main {
    public static void main(String[] args) throws Exception {
        String url="http://192.168.1.11/User/Terminal/00000000-0000-0000-0000-000000000000";
        String s[]=url.split("/");
        System.out.println(s[s.length-1]);
    }
    public static String parseTerminalGuidGruiByUrl(String result){
        String deviceId;

        if(result!=null&&!result.equals("")){
            String pattern = "\\W{0,36}";
            Pattern r = Pattern.compile(pattern);
            Matcher matcher=r.matcher(pattern);
            if(matcher.find()){
                deviceId= matcher.group();
                return deviceId;
            }
        }
        return null;
    }

}
