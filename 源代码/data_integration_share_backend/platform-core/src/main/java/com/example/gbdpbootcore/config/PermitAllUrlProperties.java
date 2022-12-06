package com.example.gbdpbootcore.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 获取yml配置信息
 */
@Data
@Component
public class PermitAllUrlProperties {

    private static List<Pattern> permitallUrlPattern;

    private List<url> permitall;

    public String[] getPermitallPatterns() {
        List<String> urls = new ArrayList();
        Iterator<url> iterator = permitall.iterator();
        while (iterator.hasNext()) {
            urls.add(iterator.next().getPattern());
        }
        return urls.toArray(new String[0]);
    }

    public static List<Pattern> getPermitallUrlPattern() {
        return permitallUrlPattern;
    }

    public static class url {
        private String pattern;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }

    @PostConstruct
    public void init() {
        if (permitall != null && permitall.size() > 0) {
            PermitAllUrlProperties.permitallUrlPattern = new ArrayList();
            Iterator<url> iterator = permitall.iterator();
            while (iterator.hasNext()) {
                String currentUrl = iterator.next().getPattern().replaceAll("\\*\\*", "(.*?)");
                Pattern currentPattern = Pattern.compile(currentUrl, Pattern.CASE_INSENSITIVE);
                permitallUrlPattern.add(currentPattern);
            }

        }
    }

    public boolean isPermitAllUrl(String url) {
        if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".ico")) {
            return true;
        }
        for (Pattern pattern : permitallUrlPattern) {
            if (pattern.matcher(url).find()) {
                return true;
            }
        }
        return false;
    }
}

