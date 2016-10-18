package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAspectRatio;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.URISyntaxException;

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

    public static void main(String[] args) throws IOException, URISyntaxException {
        log.info("Bomberman starting...");
        new Main().start();
        log.info("Bomberman finished...");
    }

    private long window;
    private GLFWErrorCallback errorCallback;

    private DatagramSocket socket;

    private GameRenderer gameRenderer;
    private Camera camera;

    private void start() throws IOException, URISyntaxException {
        initOpenGlWindow();


        ProgramShader shader = new ProgramShader(new VertexShader("shader"), new FragmentShader("shader"));
        ProgramShader textShader = new ProgramShader(new VertexShader("text"), new FragmentShader("text"));

        gameRenderer = new GameRenderer(shader);
        FpsRenderer fpsRenderer = new FpsRenderer(textShader);

        camera = new Camera(shader);

        socket = new DatagramSocket(); // Bound to an ephemeral port
        CommandSender commandSender = new CommandSender(socket);
        commandSender.register();
        StatusReceiver statusReceiver = new StatusReceiver(socket);
        statusReceiver.next();
        Thread statusReceiverThread = new Thread(statusReceiver, "Status receiver");
        statusReceiverThread.setDaemon(true); // TODO : clean stop
        statusReceiverThread.start();
        KeyManager keyManager = new KeyManager(window, commandSender, camera);
        glfwSetKeyCallback(window, keyManager);

        double lastTime = glfwGetTime();
        double statEvery = 5;
        double timeToStat = statEvery;
        int nbFrame = 0;
        while (!glfwWindowShouldClose(window)) {
            double time = glfwGetTime();
            float elapsed = (float) (time - lastTime);
            lastTime = time;
            timeToStat -= elapsed;
            nbFrame++;
            if (timeToStat < 0) {
                log.debug("FPS : {}", nbFrame / statEvery);
                nbFrame = 0;
                timeToStat = statEvery;
            }
            GLUtils.checkError();

            glClearColor(0.5f, 0.5f, 0.6f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            GLUtils.checkError();
            shader.use();
            camera.update();
            GLUtils.checkError();

            keyManager.update(elapsed);
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
        errorCallback = GLFWErrorCallback.create(
                (error, description) -> log.error("LWJGL Error - Code: {}, Description: {}",
                Integer.toHexString(error), 
                GLFWErrorCallback.getDescription(description))
        );
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
        // glfwSwapInterval(1);
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
