package Services.AzureStorage;

import java.net.URISyntaxException;
import java.util.Date;

import Consts.AgentConfig;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.analytics.CloudAnalyticsClient;
import com.microsoft.azure.storage.analytics.LogRecord;
import com.microsoft.azure.storage.analytics.StorageService;
import com.microsoft.azure.storage.blob.*;
public class AzureStorageService{

    CloudStorageAccount _storageAccount;
    CloudAnalyticsClient _cloudAnalyticsClient;
    AzureStorageUploader _azureStorageUploader;

    public boolean Connect() {
        boolean result = false;
        try {
            String connectionString = AgentConfig.newInstance().GetConnectionString();
            _storageAccount = CloudStorageAccount.parse(connectionString);
            _cloudAnalyticsClient = _storageAccount.createCloudAnalyticsClient();

            _azureStorageUploader = new AzureStorageUploader(_storageAccount);

            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean UploadFileToBlob(String filePath, String clouName) {
        return _azureStorageUploader.UploadFileToBlob(filePath, clouName);
    }

    public Iterable<LogRecord> FetchLogs(StorageService service) {
        Iterable<LogRecord> result = null;
        try {
            result = _cloudAnalyticsClient.listLogRecords(service);

        } catch (URISyntaxException uri_ex) {
        } catch (StorageException ex) {
        }
        return result;
    }

    public Iterable<LogRecord> FetchLogs(StorageService service, Date start, Date end) {
        Iterable<LogRecord> result = null;
        try {

            BlobRequestOptions options = new BlobRequestOptions();
            OperationContext operationContext = new OperationContext();
            result = _cloudAnalyticsClient.listLogRecords(service, start, end, options, operationContext);
        } catch (URISyntaxException uri_ex) {
        } catch (StorageException ex) {
        }
        return result;
    }
}
