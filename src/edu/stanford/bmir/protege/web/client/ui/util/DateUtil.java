package edu.stanford.bmir.protege.web.client.ui.util;

import java.util.Date;

public class DateUtil {
    
    public static String getAbbreviatedDate(Date creationDate){
        Date d = new Date();
        
        long diff = d.getTime() - creationDate.getTime();
        int mins = (int)(diff/(1000.0 * 60.0));
        
        if(mins ==0 ){
            return "0 mins ago";
        }
        
        int hrs = (int)mins / 60;
        if(hrs == 0){
            String suffix = " mins ago";
            if(mins == 1){
                suffix = " min ago";
            }
            return mins + suffix;
        }
        
        int days = (int)hrs / 24;
        if(days == 0){
            String suffix = " hrs ago";
            if(hrs == 1){
                suffix = " hr ago";
            }
            return (hrs + suffix);
        }
        
        int months = (int)days / 30;
        if(months == 0){
            String suffix = " days ago";
            if(days == 1){
                suffix = " day ago";
            }
            return (days + suffix);
        }
        
        int years = (int) months / 12;
        if(years == 0){
            String suffix = " months ago";
            if(months == 1){
                suffix = " month ago";
            }
            return (months + suffix);
        } else {
            String suffix = " years ago";
            if(years == 1){
                suffix = " year ago";
            }
            return (years + suffix);
        }
    }
}
