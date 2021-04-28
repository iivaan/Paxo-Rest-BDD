package com.paxovision.rest.bdd.steps;

public class RestDriverFactory {
    private static RestDriverFactory instance;
    private ThreadLocal<RestDriver> driverCollection = new ThreadLocal<RestDriver>();


    public static RestDriverFactory getInstance(){
        if(instance == null){
            instance = new RestDriverFactory();
        }

        if(instance.driverCollection.get() == null) {
            RestDriver driver = new RestDriver();
            instance.driverCollection.set(driver);
        }
        return  instance;
    }

    public RestDriver getDriver() {
        return driverCollection.get();
    }

    public void quite() {
        try {
            //driverCollection.get().close();
            //driverCollection.get().quit();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        finally {
            driverCollection.remove();
        }

    }
}
