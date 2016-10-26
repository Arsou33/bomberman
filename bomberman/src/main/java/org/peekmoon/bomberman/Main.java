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
import org.peekmoon.bomberman.board.GameRenderer;
import org.peekmoon.bomberman.key.KeyManager;
import org.peekmoon.bomberman.network.CommandSender;
import org.peekmoon.bomberman.network.StatusReceiver;
import org.peekmoon.bomberman.opengl.GLUtils;
import org.peekmoon.bomberman.shader.FragmentShader;
import org.peekmoon.bomberman.shader.ProgramShader;
import org.peekmoon.bomberman.shader.VertexShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
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

    private GameRenderer gameRenderer;
    private Camera camera;

    private void start(String server, int port) throws IOException, URISyntaxException {
        initOpenGlWindow();

        ProgramShader shader = new ProgramShader(new VertexShader("shader"), new FragmentShader("shader"));
        ProgramShader textShader = new ProgramShader(new VertexShader("text"), new FragmentShader("text"));

        gameRenderer = new GameRenderer(shader);
        FpsRenderer fpsRenderer = new FpsRenderer(textShader);

        camera = new Camera(shader);

        socket = new DatagramSocket(); // Bound to an ephemeral port
        
        // Open ephemeral port on nat
        PortMapping portMapping = new PortMapping(socket.getLocalPort(), InetAddress.getLocalHost().getHostAddress(), PortMapping.Protocol.UDP,"My Port Mapping2");
        UpnpServiceImpl upnpService = new UpnpServiceImpl(new PortMappingListener(portMapping));
        upnpService.getControlPoint().search();     
        
        CommandSender commandSender = new CommandSender(socket, server, port);
        commandSender.register();
        StatusReceiver statusReceiver = new StatusReceiver(socket);
        statusReceiver.next();
        Thread statusReceiverThread = new Thread(statusReceiver, "Status receiver");
        statusReceiverThread.setDaemon(true); // TODO : clean stop
        statusReceiverThread.start();
        KeyManager keyManager = new KeyManager(window, commandSender, camera);
        glfwSetKeyCallback(window, keyManager);


        while (!glfwWindowShouldClose(window)) {

            GLUtils.checkError();

            glClearColor(0.5f, 0.5f, 0.6f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            GLUtils.checkError();
            shader.use();
            camera.update();
            GLUtils.checkError();

            keyManager.update(100); // TODO : Remove elpased ??
            GLUtils.checkError();

            glEnable(GL_DEPTH_TEST);
            gameRenderer.render(statusReceiver.getStatus());

            glDisable(GL_DEPTH_TEST);
            textShader.use();
            fpsRenderer.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

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
        gameRenderer.release();
        glfwDestroyWindow(window);
        Callbacks.glfwFreeCallbacks(window);
        // TODO :release shader ?
        glfwTerminate();
        errorCallback.free();
    }

}
