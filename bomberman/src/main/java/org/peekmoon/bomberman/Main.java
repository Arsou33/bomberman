package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.net.DatagramSocket;
import java.net.InetAddress;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.peekmoon.bomberman.Stage.Name;
import org.peekmoon.bomberman.network.CommandSender;
import org.peekmoon.bomberman.network.HeartBeat;
import org.peekmoon.bomberman.network.StatusReceiver;
import org.peekmoon.bomberman.opengl.GLUtils;
import org.peekmoon.bomberman.shader.TextShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        log.info("Bomberman starting...");
        String server = "localhost";
        int portServer = 8232;
        Integer portLocal = null;
        boolean upnp = true;
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
                    break;
                case "-upnp" :
                    upnp = Boolean.parseBoolean(args[++i]);
                    break;

            }
        }
        Thread.setDefaultUncaughtExceptionHandler((thread, ex)->log.error("thread " + thread.getName() + " throw exception", ex));
        new Main().start(server, portServer, portLocal, upnp);
        log.info("Bomberman finished...");
    }

    private long window;
    private GLFWErrorCallback errorCallback;

    private void start(String server, int portServer, Integer portLocal, boolean upnp) throws Exception {
        initOpenGlWindow();

        TextShader textShader = new TextShader();
        TextRenderer textRenderer = new TextRenderer();
        FpsRenderer fpsRenderer = new FpsRenderer(textRenderer);
        
        // Bound to an ephemeral port if portLocal is null
        try (DatagramSocket socket = (portLocal==null?new DatagramSocket():new DatagramSocket(portLocal));
             PortForwarder portForwarder = new PortForwarder(upnp, InetAddress.getLocalHost().getHostAddress(), socket.getLocalPort());
                StatusReceiver statusReceiver = new StatusReceiver(socket)) {
            
            Thread statusReceiverThread = new Thread(statusReceiver, "Status receiver");
            statusReceiverThread.setDaemon(true); // TODO : clean stop
            statusReceiverThread.start();

            CommandSender commandSender = new CommandSender(socket, server, portServer);
            commandSender.register();
            
            Thread heartBeatThread = new Thread(new HeartBeat(commandSender), "Heartbeat sender");
            heartBeatThread.setDaemon(true);
            heartBeatThread.start();
            
            StageFactory stageFactory = new StageFactory(window, commandSender, statusReceiver, textRenderer);
            Stage currentStage = stageFactory.get(Name.CREATE_OR_JOIN_MENU);
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
                    log.info("Releasing stage " + currentStage + "...");
                    currentStage.release();
                    currentStage = stageFactory.get(nextStageName);
                    log.info("Initializing stage " + currentStage + "...");
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
            log.info("Stopping statusReceiver...");
            statusReceiver.close();
            try {
                statusReceiverThread.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            release();
        }
        

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
        glfwDestroyWindow(window);
        Callbacks.glfwFreeCallbacks(window);
        // TODO :release shader ?
        glfwTerminate();
        errorCallback.free();
    }

}
