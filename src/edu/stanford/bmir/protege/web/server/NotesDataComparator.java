package edu.stanford.bmir.protege.web.server;

import java.util.Date;

import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;

public class NotesDataComparator implements java.util.Comparator<NotesData> {

    public int compare(NotesData o1, NotesData o2) {
        Date lDate1 = o1.getLatestUpdate();
        Date lDate2 = o2.getLatestUpdate();
        
        if(lDate1 != null && lDate2 != null){
            if(lDate1.after(lDate2)){
                return -1;
            } else {
                return +1;
            }
        } else {
            return -1;
        }
    }

}
