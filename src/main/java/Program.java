
import Runnables.AzureStorageLogsRunnable;

public class Program {

    static Thread _azureLogsThread;

    public static void main(String[] args) {

        System.out.println("Starting AzureStorageLogsRunnable with Thread");

        _azureLogsThread = new Thread(new AzureStorageLogsRunnable());
        try{
            System.out.println("Sleeping Forever");
            Thread.sleep(Integer.MAX_VALUE);
        }catch(InterruptedException ex){

        }
    }

}
