/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.najda.hystrixthreadpoolfixpoc;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patnaj
 */
public class TestHystrixApp {
    
    private static final String GROUP_KEY = "hystrix_reset_poc";
    private static final String HYSTRIX_RESET_HINT =
            "if you don't execute Hystrix.reset() after the HystrixCommand.execute, then you'll get a stuck thread";
    private static final String HYSTRIX_STUCK_THREAD = 
            "you can identify it easilly be running: 'jstack %s | grep hystrix-%s-%d'";

    public static void main(String args[]) {
        
        String pid = null;
        
        try {
            pid = new File("/proc/self").getCanonicalFile().getName();
        } catch (IOException ex) {
            Logger.getLogger(TestHystrixApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.getLogger(TestHystrixApp.class.getName()).log(Level.INFO, new TestCommand(GROUP_KEY).execute());
        Logger.getLogger(TestHystrixApp.class.getName()).log(Level.INFO,HYSTRIX_RESET_HINT);
        Logger.getLogger(TestHystrixApp.class.getName()).log(Level.INFO, String.format(HYSTRIX_STUCK_THREAD, pid, GROUP_KEY, 1));

        //Hystrix.reset();

        // the reset allows us to start using Hystrix again
        Logger.getLogger(TestHystrixApp.class.getName()).log(Level.INFO, new TestCommand(GROUP_KEY).execute());
        Logger.getLogger(TestHystrixApp.class.getName()).log(Level.INFO,HYSTRIX_RESET_HINT);
        Logger.getLogger(TestHystrixApp.class.getName()).log(Level.INFO, String.format(HYSTRIX_STUCK_THREAD, pid, GROUP_KEY, 2));

        Logger.getLogger(TestHystrixApp.class.getName()).log(Level.INFO, "Please see the following URL for further details: 'https://github.com/Netflix/Hystrix/issues/102'");
        
        // http://appcrawler.com/wordpress/2013/05/06/one-way-to-tell-if-a-thread-pool-is-hung/
        // or it allows a clean shutdown
        //Hystrix.reset();
    }

    private static class TestCommand extends HystrixCommand<String> {

        public TestCommand(final String key) {
            super(HystrixCommandGroupKey.Factory.asKey(key));
        }

        @Override
        protected String run() throws Exception {
            return "TestCommand being run";
        }
    }
}
