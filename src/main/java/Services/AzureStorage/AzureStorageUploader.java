package Services.AzureStorage;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.table.CloudTableClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AzureStorageUploader {

    private CloudStorageAccount _storageAccount;
    private CloudBlobClient _blobClient;
    private CloudFileClient _fileClient;
    private CloudQueueClient _queueClient;
    private CloudTableClient _tableClient;

    final String ContainerName = "mycontainer";

    public AzureStorageUploader(CloudStorageAccount _storageAccount) {
        this._storageAccount = _storageAccount;
        _blobClient = _storageAccount.createCloudBlobClient();
        _fileClient = _storageAccount.createCloudFileClient();
        _queueClient = _storageAccount.createCloudQueueClient();
        _tableClient = _storageAccount.createCloudTableClient();
    }

    public boolean UploadFileToBlob(String fullFilePath, String cloudName) {
        boolean result = false;
        try {
            CloudBlobContainer container = _blobClient.getContainerReference("mycontainer");

            container.createIfNotExists();

            CloudBlockBlob blockBlob = container.getBlockBlobReference(cloudName);

            InputStream stream = GetStream(fullFilePath);
            blockBlob.upload(stream , stream .available());
            stream.close();
            result = true;
        } catch (StorageException ex) {
            result = false;
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    private InputStream GetStream(String filePath) {
        InputStream is = null;

        try {
            is = new java.io.FileInputStream(filePath);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return is;
    }

}
