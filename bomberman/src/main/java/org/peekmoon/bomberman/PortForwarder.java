package org.peekmoon.bomberman;

import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class PortForwarder implements AutoCloseable {
    
    private final static Logger log = LoggerFactory.getLogger(PortForwarder.class);
    
    {
        // Redirect JUL log to slf4j (cling use jul)
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
    
    
    private UpnpServiceImpl upnpService = null;
    
    public PortForwarder(boolean enabled, String localAddress, int localPort) {
        if (!enabled) {
            log.info("Upnp service is disable");
            return;
        }
        
        log.info("Create port mapping for {}:{}... ", localAddress, localPort);
        PortMapping portMapping = new PortMapping(localPort, localAddress, PortMapping.Protocol.UDP, "Bomberman port mapping");
        upnpService = new UpnpServiceImpl(new PortMappingListener(portMapping));
        upnpService.getControlPoint().search();
    }
    
    @Override
    public void close() throws Exception {
        if (upnpService != null) {
            log.info("Stopping upnpService...");
            upnpService.shutdown();
        }        
    }

}
