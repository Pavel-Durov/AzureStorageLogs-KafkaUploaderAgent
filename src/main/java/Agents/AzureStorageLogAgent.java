package Agents;

import Model.KafkaMessage;
import Model.PersistenceMessage;
import Services.AzureStorage.AzureStorageService;
import Services.PersistenceService;
import Services.Kafka.KafkaService;
import Services.LogService;
import Utils.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.storage.analytics.LogRecord;
import com.microsoft.azure.storage.analytics.StorageService;

import java.util.Date;

public class AzureStorageLogAgent {

    public void AzureStorageLogAgent() {
    }

    GsonBuilder _gsonBuilder;
    KafkaService _kafkaService;
    AzureStorageService _azureStorageService;

    public void Run() {


        _kafkaService = new KafkaService();
        _gsonBuilder = new GsonBuilder();

        System.out.println("BigBanana AzureStorage Agent Running...");

        _azureStorageService = new AzureStorageService();

        if (_azureStorageService.Connect()) {
            LogService.Info("Connection to Azure Storage established");

            PersistenceMessage message = PersistenceService.ReadFromPersistanceFile();

            if (message != null) {
                String lastDate = PersistenceService.ReadFromPersistanceFile().getDate();


                if (lastDate != null) {
                    Date parsed = DateUtil.Parse(lastDate);
                    FetchLogs(parsed);
                }
            } else {
                FetchLogs();
            }
        }
    }

    private void FetchLogs() {
        UpdateLastLogFetchTime();
        Date current = new Date();

        LogService.Info("Fetching logs from Azure Storage");

        Iterable<LogRecord> blobLogs = _azureStorageService.FetchLogs(StorageService.BLOB);
        Iterable<LogRecord> fileLogs = _azureStorageService.FetchLogs(StorageService.FILE);
        Iterable<LogRecord> queueLogs = _azureStorageService.FetchLogs(StorageService.QUEUE);
        Iterable<LogRecord> tableLogs = _azureStorageService.FetchLogs(StorageService.TABLE);

        SendToKafka(blobLogs, current);
        SendToKafka(fileLogs, current);
        SendToKafka(queueLogs, current);
        SendToKafka(tableLogs, current);
    }

    private void FetchLogs(Date lastDate) {

        Date current = UpdateLastLogFetchTime();

        LogService.Info("Fetching logs from Azure Storage");

        Iterable<LogRecord> blobLogs = _azureStorageService.FetchLogs(StorageService.BLOB, lastDate, current);
        Iterable<LogRecord> fileLogs = _azureStorageService.FetchLogs(StorageService.FILE, lastDate, current);
        Iterable<LogRecord> queueLogs = _azureStorageService.FetchLogs(StorageService.QUEUE, lastDate, current);
        Iterable<LogRecord> tableLogs = _azureStorageService.FetchLogs(StorageService.TABLE, lastDate, current);

        SendToKafka(blobLogs, current);
        SendToKafka(fileLogs, current);
        SendToKafka(queueLogs, current);
        SendToKafka(tableLogs, current);
    }


    private Date UpdateLastLogFetchTime() {
        Date date = new Date();
        PersistenceService.WriteToPersistanceFile(new PersistenceMessage(date));
        return date;
    }

    private void SendToKafka(Iterable<LogRecord> blobLogs, Date fetchedTime) {
        LogService.Info("Sending log message to Kafka");

        Gson gson = new GsonBuilder().create();

        for (LogRecord item : blobLogs) {
            KafkaMessage message = new KafkaMessage(item, fetchedTime);
            String JString = gson.toJson(message);
            System.out.println("Sending message to Kafka : " + JString);

            _kafkaService.SendMessage(JString, KafkaService.LOG_TOPIC);//KafkaService.LOG_TOPIC);
        }
    }


}
