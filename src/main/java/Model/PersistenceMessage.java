package Model;

import Utils.DateUtil;

import java.util.Date;


public class PersistenceMessage {

    public PersistenceMessage(Date date){
        _date = date;
    }

    Date _date;

    public String getDate() {
        return DateUtil.Parse(_date);
    }
}
