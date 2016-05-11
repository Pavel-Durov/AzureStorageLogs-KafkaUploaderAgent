package Runnables;

import Agents.AzureStorageLogAgent;

import java.util.concurrent.CountDownLatch;

public class AzureStorageLogsRunnable implements Runnable {

    AzureStorageLogAgent _azureStorageLogAgent;
    public CountDownLatch latch = new CountDownLatch(1);

    public void run() {

        latch.countDown();
        _azureStorageLogAgent = new AzureStorageLogAgent();

        System.out.println("Running BigBanana AzureStorage Agent");

        _azureStorageLogAgent.Run();

    }

}
