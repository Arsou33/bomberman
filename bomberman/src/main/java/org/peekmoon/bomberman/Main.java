package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAspectRatio;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glViewport;
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
import org.peekmoon.bomberman.board.BoardStage;
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
        int port = 8232;
        for (int i=0; i<args.length; i++) {
            switch (args[i]) {
                case "-server" :
                    if (args[i+1].startsWith("-")) throw new IllegalArgumentException("Server can't start by -");
                    server = args[++i];
                    break;
                case "-port" :
                    port = Integer.parseInt(args[++i]);
                    break;
            }
        }
        Thread.setDefaultUncaughtExceptionHandler((thread, ex)->log.error("thread " + thread.getName() + " throw exception", ex));
        new Main().start(server, port);
        log.info("Bomberman finished...");
    }

    private long window;
    private GLFWErrorCallback errorCallback;

    private DatagramSocket socket;



    private void start(String server, int port) throws IOException, URISyntaxException, InterruptedException {
        initOpenGlWindow();

        TextShader textShader = new TextShader();

        FpsRenderer fpsRenderer = new FpsRenderer(textShader);

        socket = new DatagramSocket(); // Bound to an ephemeral port
        
        // Open ephemeral port on nat
        String localAddress = InetAddress.getLocalHost().getHostAddress();
        int localPort = socket.getLocalPort();
        log.info("Create port mapping for {}:{} ", localAddress, localPort);
        PortMapping portMapping = new PortMapping(localPort, localAddress, PortMapping.Protocol.UDP, "Bomberman port mapping");
        UpnpServiceImpl upnpService = new UpnpServiceImpl(new PortMappingListener(portMapping));
        upnpService.getControlPoint().search();     
        
        
        Stage boardStage = new BoardStage(window, socket, server, port);
        boardStage.init();
        glfwSetKeyCallback(window, boardStage.getKeyManager());

        while (!glfwWindowShouldClose(window)) {

            GLUtils.checkError();
            glClearColor(0.5f, 0.5f, 0.6f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            boardStage.getKeyManager().update(); 
            boardStage.render();

            textShader.use();
            fpsRenderer.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        
        boardStage.release();

        log.info("Stopping upnpService");
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
