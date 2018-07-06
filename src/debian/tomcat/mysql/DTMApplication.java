package debian.tomcat.mysql;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

@ApplicationPath("service")
public class DTMApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register root resource
        classes.add(DTMService.class);
        
        // Add additional features such as support for Multipart.
        classes.add(MultiPartFeature.class);
        System.out.println("DTMService added");
        return classes;
    }
}
