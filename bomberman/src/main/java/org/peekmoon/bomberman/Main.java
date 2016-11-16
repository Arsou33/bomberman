package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URISyntaxException;

import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.peekmoon.bomberman.Stage.Name;
import org.peekmoon.bomberman.opengl.GLUtils;
import org.peekmoon.bomberman.shader.TextShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Main {

    private final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        // Redirect JUL log to slf4j (cling use jul)
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        
        log.info("Bomberman starting...");
        String server = "localhost";
        int portServer = 8232;
        Integer portLocal = null;
        for (int i=0; i<args.length; i++) {
            switch (args[i]) {
                case "-server" :
                    if (args[i+1].startsWith("-")) throw new IllegalArgumentException("Server can't start by -");
                    server = args[++i];
                    break;
                case "-portServer" :
                    portServer = Integer.parseInt(args[++i]);
                    break;
                case "-portLocal" :
                    portLocal = Integer.parseInt(args[++i]);
            }
        }
        Thread.setDefaultUncaughtExceptionHandler((thread, ex)->log.error("thread " + thread.getName() + " throw exception", ex));
        new Main().start(server, portServer, portLocal);
        log.info("Bomberman finished...");
    }

    private long window;
    private GLFWErrorCallback errorCallback;

    private DatagramSocket socket;

    private void start(String server, int portServer, Integer portLocal) throws IOException, URISyntaxException, InterruptedException {
        initOpenGlWindow();

        TextShader textShader = new TextShader();
        TextRenderer textRenderer = new TextRenderer();
        FpsRenderer fpsRenderer = new FpsRenderer(textRenderer);

        if (portLocal == null) {
            socket = new DatagramSocket(); // Bound to an ephemeral port
        } else {
            socket = new DatagramSocket(portLocal);
        }
        
        // Open ephemeral port on nat
        String localAddress = InetAddress.getLocalHost().getHostAddress();
        int localPort = socket.getLocalPort();
        log.info("Create port mapping for {}:{} ", localAddress, localPort);
        PortMapping portMapping = new PortMapping(localPort, localAddress, PortMapping.Protocol.UDP, "Bomberman port mapping");
        UpnpServiceImpl upnpService = new UpnpServiceImpl(new PortMappingListener(portMapping));
        upnpService.getControlPoint().search();     
        
        StageFactory stageFactory = new StageFactory(window, socket, server, portServer, textRenderer);
        //Stage menuStage = new MenuStage(window, textRenderer);
        //Stage boardStage = new BoardStage(window, socket, server, port);
        Stage currentStage = stageFactory.get(Name.MENU);
        currentStage.init();
        glfwSetKeyCallback(window, currentStage.getKeyManager());

        while (!glfwWindowShouldClose(window)) {

            GLUtils.checkError();
            glClearColor(0.5f, 0.5f, 0.6f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            currentStage.getKeyManager().update(); 
            currentStage.render();
            Name nextStageName = currentStage.next();
            if (!currentStage.is(nextStageName)) {
                currentStage.release();
                currentStage = stageFactory.get(nextStageName);
                currentStage.init();
                glfwSetKeyCallback(window, currentStage.getKeyManager());
            }
            
            textShader.use();
            fpsRenderer.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        
        log.info("Releasing resources...");
        currentStage.release();
        textRenderer.release();
        log.info("Stopping upnpService...");
        upnpService.shutdown();
        release();
    }

    private void initOpenGlWindow() {
        errorCallback = GLFWErrorCallback
                .create((error, description) -> log.error("LWJGL Error - Code: {}, Description: {}",
                        Integer.toHexString(error), GLFWErrorCallback.getDescription(description)));
        glfwSetErrorCallback(errorCallback);

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        window = glfwCreateWindow(896, 504, "Simple example", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetWindowAspectRatio(window, 16, 9);
        glfwSetFramebufferSizeCallback(window, (win, width, height) -> glViewport(0, 0, width, height));
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        GL.createCapabilities();
        glDepthFunc(GL_LESS);
    }

    private void release() {
        socket.close();
        glfwDestroyWindow(window);
        Callbacks.glfwFreeCallbacks(window);
        // TODO :release shader ?
        glfwTerminate();
        errorCallback.free();
    }

}
