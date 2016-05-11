package Model;


import com.microsoft.azure.storage.analytics.LogRecord;

import java.util.Date;

public class KafkaMessage {

    public KafkaMessage(LogRecord logRecord, Date date) {
        log = logRecord;
        timeStamp = date;
    }


    LogRecord log;
    Date timeStamp;
}
