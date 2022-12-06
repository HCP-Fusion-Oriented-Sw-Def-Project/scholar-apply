package com.example.gbdpuserac.config;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class DateProcess {

    public static SimpleDateFormat format_year_month = new SimpleDateFormat("yyyy-MM");
    public static SimpleDateFormat formatter_full= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentHour(){
        Date date = new Date(System.currentTimeMillis());
        return formatter_full.format(date).split(" ")[1].split(":")[0];
    }

    public static String get24String(){
        StringBuilder builder = new StringBuilder();
        int currentHour = Integer.parseInt(getCurrentHour());
        for(int i=0;i<24;i++){
            if(currentHour==i){
                builder.append("1");
            }else{
                builder.append("0");
            }
            builder.append(",");
        }
        return builder.toString().substring(0,47);
    }

    public static String set24String(String old){
        String[] oldArray = old.split(",");
        StringBuilder builder = new StringBuilder();
        int currentHour = Integer.parseInt(getCurrentHour());
        for(int i=0;i<oldArray.length;i++){
            if(i==currentHour){
                builder.append(Integer.parseInt(oldArray[i])+1);
            }else{
                builder.append(oldArray[i]);
            }
            if(i!=oldArray.length-1)
                builder.append(",");
        }
        return builder.toString();
    }

    public static String mergeStr(String... params){
        StringBuilder sb = new StringBuilder();
        Arrays.asList(params).stream()
                .filter(param -> param!=null&&!param.isEmpty())
                .forEach(param ->sb.append(param));
        return sb.toString();
    }

    public static int lastIndexOf(String String,char a){
        char[] chars = String.toCharArray();
        int index =-1;
        for(int i=0;i<chars.length;i++){
            if(chars[i]==a){
                index = i;
            }
        }
        return index;
    }


}

