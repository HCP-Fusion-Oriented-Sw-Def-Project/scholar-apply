package com.example.gbdpuserac.dto;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class StaticByTimeDto {

    private String time;

    private Integer num;

    public static SimpleDateFormat hour=new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static SimpleDateFormat day=new SimpleDateFormat("yyyy-MM-dd HH:00:00");

    public static SimpleDateFormat all=new SimpleDateFormat("yyyy-MM-dd");

    public StaticByTimeDto() {
    }

    public StaticByTimeDto(String time) {
        this.time = time;
        this.num = 0;
    }

    public static List<StaticByTimeDto> calHourList(List<StaticByTimeDto> dtos){
        Map<String,StaticByTimeDto> map = new HashMap<>(dtos.size());
        for(StaticByTimeDto tmp:dtos){
            map.put(tmp.getTime(),tmp);
        }

        Calendar calendar = Calendar.getInstance();
        Long now = new Date().getTime();
        calendar.setTime(new Date());
        int minute = calendar.get(Calendar.MINUTE);
        minute = minute%10;
        now = now - minute*60*1000;
        List<StaticByTimeDto> timeDtos = new ArrayList<>();
        for(int i=0;i<6;i++){
            String timeStr = hour.format(new Date(now));
            if(map.containsKey(timeStr)){
                timeDtos.add(map.get(timeStr));
            }else{
                timeDtos.add(new StaticByTimeDto(timeStr));
            }
            now = now - 10*60*1000;
        }
        return timeDtos;
    }

    public static List<StaticByTimeDto> calDayList(List<StaticByTimeDto> dtos){
        Map<String,StaticByTimeDto> map = new HashMap<>(dtos.size());
        for(StaticByTimeDto tmp:dtos){
            map.put(tmp.getTime(),tmp);
        }

        Calendar calendar = Calendar.getInstance();
        Long now = new Date().getTime();
        calendar.setTime(new Date());
        int minute = calendar.get(Calendar.MINUTE);
        now = now - minute*60*1000;
        List<StaticByTimeDto> timeDtos = new ArrayList<>();
        for(int i=0;i<24;i++){
            String timeStr = day.format(new Date(now));
            if(map.containsKey(timeStr)){
                timeDtos.add(map.get(timeStr));
            }else{
                timeDtos.add(new StaticByTimeDto(timeStr));
            }
            now = now - 60*60*1000;

        }
        return timeDtos;
    }

    public static List<StaticByTimeDto> calAllList(List<StaticByTimeDto> dtos,String first) throws ParseException {
        if(first==null){
            return new ArrayList<>();
        }


        Map<String,StaticByTimeDto> map = new HashMap<>(dtos.size());
        for(StaticByTimeDto tmp:dtos){
            map.put(tmp.getTime(),tmp);
        }

        Long beginDate = all.parse(first).getTime();
        Long now = new Date().getTime();
        List<StaticByTimeDto> timeDtos = new ArrayList<>();
        while (beginDate<=now){
            String timeStr = all.format(new Date(beginDate));
            if(map.containsKey(timeStr)){
                timeDtos.add(map.get(timeStr));
            }else{
                timeDtos.add(new StaticByTimeDto(timeStr));
            }
            beginDate +=24*60*60*1000;
        }
        String timeStr = all.format(new Date(now));
        if(!timeDtos.get(timeDtos.size()-1).getTime().equals(timeStr)){
            if(map.containsKey(timeStr)){
                timeDtos.add(map.get(timeStr));
            }else{
                timeDtos.add(new StaticByTimeDto(timeStr));
            }
        }
        return timeDtos;
    }

    public static void main(String[] args){
        calDayList(new ArrayList<>());
    }
}
